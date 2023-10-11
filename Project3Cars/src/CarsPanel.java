
/**
 * CarPanel.java
 * Date: 10.10.23
 * @author Charles Kresho
 * Purpose: Most of the logic happens here. CarsPanel holds the cars and the lights, and handles button presses from ButtonsPanel.
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

public class CarsPanel extends JPanel implements ChangeListener, Runnable {

    private ArrayList<AnimatedCar> cars;
    private ArrayList<AnimatedLight> lights;
    private ArrayList<Thread> carThreads, lightThreads;
    private boolean paused, running; // Flags for thread logic
    private int finishLinePixel; // Current location of the finish line in pixels

    // Button panel that supplies the state of the simulation.
    private ButtonsPanel sourcePanel;

    // Event listener stuff
    private ChangeEvent changeEvent = null;
    private EventListenerList windowListenerList = new EventListenerList();
    private EventListenerList carListenerList = new EventListenerList();

    // Lock for thread logic
    private final Lock lock = new ReentrantLock();

    /**
     * Space between the car images vertically.
     */
    private static final int CAR_SPACING_PIXELS = 100;

    /**
     * Vertical y coordinate of the first car; is the width of the car image to supply buffer space.
     */
    private static int CAR_VERT_START_PIXELS;

    /**
     * Space between the lights horizontally.
     */
    private static final int LIGHT_SPACING_PIXELS = 200; // This # pixels = 1000m 

    /**
     * Horizontal x coordinate of the first light. Will be the light spacing + the width of the car.
     */
    private static int LIGHT_HORI_START_PIXELS;

    /**
     * Size of the colored ball of stop light.
     */
    private static final int STOP_LIGHT_DIAMETER = 10;

    /**
     * Max pixels / second of the cars.
     */
    private static final int CAR_MAX_SPEED_PIXELS = 40; // This (# * 1000 / LIGHT_SPACING_PIXELS) = m/s

    /**
     * Min pixels / second of the cars.
     */
    private static final int CAR_MIN_SPEED_PIXELS = 20;// This (# * 1000 / LIGHT_SPACING_PIXELS) = m/s

    /**
     * Theoretical "frame rate".
     */
    private static final double REFRESH_RATE = 144.0;

    /**
     * Create a new CarsPanel.
     * @param source ButtonsPanel supplying the information for this panel.
     */
    public CarsPanel(ButtonsPanel source) {
        try {
            BufferedImage img = ImageIO.read(new File(AnimatedCar.getCarImageLocation()));
            LIGHT_HORI_START_PIXELS = LIGHT_SPACING_PIXELS + img.getWidth();
            CAR_VERT_START_PIXELS = img.getWidth();
        } catch (IOException e) {
            System.out.println("Error getting car image.");
        }

        sourcePanel = source;
        sourcePanel.addChangeListener(this);

        cars = new ArrayList<AnimatedCar>();
        lights = new ArrayList<AnimatedLight>();
        carThreads = new ArrayList<Thread>();
        lightThreads = new ArrayList<Thread>();

        paused = false;
        running = false;

        int carImgWidth = LIGHT_HORI_START_PIXELS - LIGHT_SPACING_PIXELS;
        finishLinePixel = (carImgWidth + ((source.getNumLights() + 1) * LIGHT_SPACING_PIXELS));

        for (int i = 0; i < source.getNumLights(); i++) {
            lightFactory(i);
        }

        for (int i = 0; i < source.getNumCars(); i++) {
            carFactory(i);
        }

    }

    @Override
    public void run() {
        // Roughly try and force the window to repain every "frame".
        while (true) {
            try {
                Thread.sleep((long) (1000.0 / REFRESH_RATE));
            } catch (InterruptedException e) {
            }
            repaint();
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        // First, aquire the lock so that the panel won't attempt to paint as we're removing and adding elements to the arrays
        try {
            lock.tryLock(5, TimeUnit.SECONDS);
        } catch (InterruptedException exception) {
            return;
        }

        // If there are less cars then specified, add more cars to make up for it.
        for (int i = cars.size(); i < sourcePanel.getNumCars(); i++) {
            carFactory(i);
        }

        // If there are more cars than specified, remove ones off the end
        for (int i = cars.size(); i > sourcePanel.getNumCars(); i--) {
            cars.get(i - 1).terminate();
            cars.remove(i - 1);
            carThreads.remove(i - 1);
        }

        // Flag for if the number of lights is about to change
        boolean lightsChanged = lights.size() != sourcePanel.getNumLights();

        // If there are less lights than specified, add more lights ot make up for it. 
        for (int i = lights.size(); i < sourcePanel.getNumLights(); i++) {
            lightFactory(i);
        }

        // If there are more lights than specified, remove ones off the end
        for (int i = lights.size(); i > sourcePanel.getNumLights(); i--) {
            lights.get(i - 1).terminate();
            lights.remove(i - 1);
            lightThreads.remove(i - 1);
        }

        // If the number of lights changed, the cars need to be notified so they can move and restart their threads if necessary.
        if (lightsChanged) {
            fireFinishLightChanged();
            for (int i = 0; i < carThreads.size(); i++) {
                Thread curThread = carThreads.get(i);
                if (!curThread.isAlive()) {
                    curThread = new Thread(cars.get(i));
                    carThreads.remove(i);
                    carThreads.add(i, curThread);
                    if (running) {
                        curThread.start();
                    }
                }
            }
        }

        // If the simulation has been starteed/stopped/paused/unpaused, call helper method, then update the flags
        if (!running && sourcePanel.isRunning()) {
            startHelper();
            running = true;
            paused = false;
        } else if (running && !sourcePanel.isRunning()) {
            resetHelper();
            running = false;
            paused = false;
        } else if (!paused && sourcePanel.isPaused()) {
            pauseHelper();
            paused = true;
        } else if (paused && !sourcePanel.isPaused()) {
            unpauseHelper();
            paused = false;
        }

        // Finally release the lock
        lock.unlock();
        // This repaint and resize are important because for some reason the root window doesn't detect it until another button is pressed.
        repaint();
        setSize(getPreferredSize());

        // Fire a panel change update to force the root window to pack itself
        firePanelChanged();
    }

    /**
     * Helper method that resets the state of the simulation.
     */
    private void resetHelper() {

        // Remove all existing lights, terminating their threads first
        for (int i = lights.size() - 1; i >= 0; i--) {
            AnimatedLight curLight = lights.get(i);
            curLight.terminate();
            lights.remove(i);
        }
        lightThreads = new ArrayList<Thread>();

        // Add in new lights (resets to green and resets timers)
        for (int i = 0; i < sourcePanel.getNumLights(); i++) {
            lightFactory(i);
        }

        // Remove all existing cars, terminating their threads first
        for (int i = cars.size() - 1; i >= 0; i--) {
            AnimatedCar curCar = cars.get(i);
            curCar.terminate();
            cars.remove(i);
        }
        carThreads = new ArrayList<Thread>();

        // Add in new cars (moves to 0 and resets speeds)
        for (int i = 0; i < sourcePanel.getNumCars(); i++) {
            carFactory(i);
        }
    }

    /**
     * Pause all the lights and cars.
     */
    private void pauseHelper() {
        for (AnimatedLight curLight : lights) {
            curLight.pause();
        }

        for (AnimatedCar curCar : cars) {
            curCar.pause();
        }
    }

    /**
     * Unpause all the lights and cars.
     */
    private void unpauseHelper() {
        for (AnimatedLight curLight : lights) {
            curLight.unpause();
        }

        for (AnimatedCar curCar : cars) {
            curCar.unpause();
        }
    }

    /**
     * Start all the lights and cars.
     */
    private void startHelper() {
        for (Thread curThread : lightThreads) {
            curThread.start();
        }

        for (Thread curThread : carThreads) {
            curThread.start();
        }
    }

    /**
     * Create a new car. Factory sets up all threads and listeners. Speed of car is randomized.
     * @param carNum number of the car in the simulation (used for placement purposes)
     * @return you get a car, you get a car, everybody gets a car!
     */
    private AnimatedCar carFactory(int carNum) {
        Random rand = new Random();
        try {
            int y = ((carNum * CAR_SPACING_PIXELS) + CAR_VERT_START_PIXELS);
            double velocity = ((rand.nextDouble() * (CAR_MIN_SPEED_PIXELS))
                    + CAR_MAX_SPEED_PIXELS - CAR_MIN_SPEED_PIXELS);

            AnimatedCar tempCar = new AnimatedCar(0, y, velocity, finishLinePixel);

            for (AnimatedLight curLight : lights) {
                // Add stopping points for any lights that are currently red
                if (curLight.getLightState() == LightState.RED) {
                    tempCar.addRedLightLocation(curLight.getxPos() - CAR_VERT_START_PIXELS);
                }
                // Add listeners for all the lights to the new car
                curLight.addChangeListener(tempCar);
            }

            // Add a listener for the finish line moving to the new car
            addFinishLightListener(tempCar);
            cars.add(tempCar);

            // If the simulation is running and/or paused, update the car's status when creating the new thread. Start if necessary.
            Thread tempThread = new Thread(tempCar, "Car Thread " + carNum);
            if (sourcePanel.isPaused()) {
                tempCar.pause();
            }
            if (sourcePanel.isRunning()) {
                tempThread.start();
            }
            carThreads.add(tempThread);
            return tempCar;
        } catch (IOException e) {
            System.out.println("Couldn't get car image.");
        }

        // Pray this never gets reached
        return null;
    }

    /**
     * Create a new light
     * @param lightNum Number of the light in the simulation (used for placement purposes)
     * @return a new light
     */
    private AnimatedLight lightFactory(int lightNum) {
        AnimatedLight tempLight = new AnimatedLight(((lightNum * LIGHT_SPACING_PIXELS) + LIGHT_HORI_START_PIXELS), 0);

        // Add new listeners for the light to every existing car
        for (AnimatedCar curCar : cars) {
            tempLight.addChangeListener(curCar);
        }

        // If the simulation is running and/or paused, set up the proper state for the new thread
        lights.add(tempLight);
        Thread tempThread = new Thread(tempLight, "Light Thread " + lightNum);
        if (sourcePanel.isPaused()) {
            tempLight.pause();
        }
        if (sourcePanel.isRunning()) {
            tempThread.start();
        }
        lightThreads.add(tempThread);

        return tempLight;
    }

    @Override
    public Dimension getPreferredSize() {
        // Just a wee bit bigger than the space needed to fit all the cars and finish line
        int x = LIGHT_HORI_START_PIXELS + 10;
        int y = CAR_VERT_START_PIXELS * 2;

        x += (lights.size()) * LIGHT_SPACING_PIXELS;
        y += (cars.size() - 1) * CAR_SPACING_PIXELS;

        return new Dimension(x, y);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // If the lock cannot be aquired, then a reset is in progress, check back later
        if (!lock.tryLock()) {
            return;
        }

        // For each car, paint it where it shows up on the panel
        for (int i = 0; i < cars.size(); i++) {
            AnimatedCar curCar = cars.get(i);
            g.drawImage(curCar.getCar(), curCar.getxPos(), curCar.getyPos(), this);
        }

        // For each light, paint a circle of the specified color where it shows up on the panel
        for (int i = 0; i < lights.size(); i++) {
            AnimatedLight curLight = lights.get(i);
            switch (curLight.getLightState()) {
                case GREEN:
                    g.setColor(Color.GREEN);
                    break;
                case YELLOW:
                    g.setColor(Color.YELLOW);
                    break;
                case RED:
                    g.setColor(Color.RED);
                    break;
                default:
                    System.out.println("Default case on light switch happened. Should ever happen...");
                    break;

            }
            g.fillOval(curLight.getxPos(), curLight.getyPos(), STOP_LIGHT_DIAMETER, STOP_LIGHT_DIAMETER);
        }

        g.setColor(Color.BLACK);
        Dimension windowSize = getPreferredSize();
        int carImgWidth = LIGHT_HORI_START_PIXELS - LIGHT_SPACING_PIXELS;

        /*
         * For:
         *      1) The starting line
         *      2) Every light
         *      3) The finish line
         * Draw a verticle black line the height of the window (not the full height, starts at the top of the first car)
         */
        for (int i = 0; i < lights.size() + 2; i++) {
            g.drawLine((carImgWidth + (i * LIGHT_SPACING_PIXELS)), CAR_VERT_START_PIXELS,
                    (carImgWidth + (i * LIGHT_SPACING_PIXELS)), (int) windowSize.getHeight());
        }

        // Finally, release the lock to indicate the painting is over
        lock.unlock();
    }

    /**
     * Get the x position of the finish line
     * @return
     */
    public int getFinishLinePixel() {
        return finishLinePixel;
    }

    /*
     * The rest of this is event handling code copied from
     * DefaultBoundedRangeModel.
     */
    public void addWindowListener(ChangeListener l) {
        windowListenerList.add(ChangeListener.class, l);
    }

    public void removeWindowListener(ChangeListener l) {
        windowListenerList.remove(ChangeListener.class, l);
    }

    protected void firePanelChanged() {
        Object[] listeners = windowListenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChangeListener.class) {
                if (changeEvent == null) {
                    changeEvent = new ChangeEvent(this);
                }
                ((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
            }
        }
    }

    // This is modified from the above method

    public void addFinishLightListener(ChangeListener l) {
        carListenerList.add(ChangeListener.class, l);
    }

    public void removeFinishLightListener(ChangeListener l) {
        carListenerList.remove(ChangeListener.class, l);
    }

    protected void fireFinishLightChanged() {
        int carImgWidth = LIGHT_HORI_START_PIXELS - LIGHT_SPACING_PIXELS;
        finishLinePixel = (carImgWidth + ((lights.size() + 1) * LIGHT_SPACING_PIXELS));
        Object[] listeners = carListenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChangeListener.class) {
                if (changeEvent == null) {
                    changeEvent = new ChangeEvent(this);
                }
                ((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
            }
        }
    }
}
