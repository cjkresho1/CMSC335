import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
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

// TODO Document

public class CarsPanel extends JPanel implements ChangeListener, Runnable {
    // Stores a list of all cars currently in the program
    private ArrayList<AnimatedCar> cars;
    private ArrayList<AnimatedLight> lights;
    private ArrayList<Thread> carThreads, lightThreads;
    private ButtonsPanel sourcePanel;
    private ChangeEvent changeEvent = null;
    private EventListenerList listenerList = new EventListenerList();
    private boolean paused, running;

    private final Lock lock = new ReentrantLock();

    private static final int CAR_SPACING_PIXELS = 100;
    private static final int CAR_VERT_START_PIXELS = 50;
    private static final int LIGHT_SPACING_PIXELS = 200;
    private static int LIGHT_HORI_START_PIXELS;
    private static final int STOP_LIGHT_DIAMETER = 10;
    private static final int CAR_MAX_SPEED_PIXELS = 5;

    public CarsPanel(ButtonsPanel source) {

        try {
            LIGHT_HORI_START_PIXELS = LIGHT_SPACING_PIXELS
                    + ImageIO.read(new File(AnimatedCar.getCarImageLocation())).getWidth();
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

        Random rand = new Random();
        try {
            for (int i = 0; i < source.getNumCars(); i++) {
                cars.add(new AnimatedCar(0, ((i * CAR_SPACING_PIXELS) + CAR_VERT_START_PIXELS), ((rand.nextDouble() * (CAR_MAX_SPEED_PIXELS - 1)) + 1)));
            }
        } catch (IOException e) {
            System.out.println("Error getting car image.");
        }

        for (int i = 0; i < source.getNumLights(); i++) {
            lights.add(new AnimatedLight(((i * LIGHT_SPACING_PIXELS) + LIGHT_HORI_START_PIXELS), 0));
        }
    }

    @Override
    public void run() {
        for (int i = 0; i < lights.size(); i++) {
            AnimatedLight curLight = lights.get(i);
            Thread tempThread = new Thread(curLight, "Light Thread " + i);
            lightThreads.add(tempThread);
        }

        for (int i = 0; i < cars.size(); i++ ) {
            AnimatedCar curCar = cars.get(i);
            Thread tempThread = new Thread(curCar, "Car Thread " + i);
            carThreads.add(tempThread);
        }

        while(true) {
            try {
                Thread.sleep(100);
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
        Random rand = new Random();
        for (int i = cars.size(); i < sourcePanel.getNumCars(); i++)
        {
            try {
                AnimatedCar tempCar = new AnimatedCar(0, ((i * CAR_SPACING_PIXELS) + CAR_VERT_START_PIXELS), ((rand.nextDouble() * (CAR_MAX_SPEED_PIXELS - 1)) + 1));
                cars.add(tempCar);
                Thread tempThread = new Thread(tempCar, "Car Thread " + i);
                carThreads.add(tempThread);
            } catch (IOException e1) {
                System.out.println("Error getting car image file.");
            }
            
        }

        // If there are more cars than specified, remove ones off the end
        for (int i = cars.size(); i > sourcePanel.getNumCars(); i--)
        {
            cars.get(i - 1).terminate();
            cars.remove(i - 1);
            carThreads.remove(i - 1);
        }

        // If there are less lights than specified, add more lights ot make up for it. 
        for (int i = lights.size(); i < sourcePanel.getNumLights(); i++)
        {
            AnimatedLight tempLight = new AnimatedLight(((i * LIGHT_SPACING_PIXELS) + LIGHT_HORI_START_PIXELS), 0);
            lights.add(tempLight);
            Thread tempThread = new Thread(tempLight, "Light Thread " + i);
            lightThreads.add(tempThread);
        }

        // If there are more lights than specified, remove ones off the end
        for (int i = lights.size(); i > sourcePanel.getNumLights(); i--)
        {
            lights.get(i - 1).terminate();
            lights.remove(i - 1);
            lightThreads.remove(i - 1);
        }
        
        // If the simulation has been starteed/stopped/paused/unpaused, call helper method
        if (!running && sourcePanel.isRunning()) {
            startHelper();
            running = true;
            paused = false;
        }
        else if (running && !sourcePanel.isRunning()) {
            reset();
            running = false;
            paused = false;
        }
        else if (!paused && sourcePanel.isPaused()) {
            pause();
            paused = true;
        }
        else if (paused && !sourcePanel.isPaused()) {
            unpause();
            paused = false;
        }

        // Finally release the lock
        lock.unlock();

        repaint();
        setSize(getPreferredSize());
        fireStateChanged();
    }

    private void reset() {

        // Remove all existing lights, terminating their threads first
        for (int i = lights.size() - 1; i >= 0; i--) {
            AnimatedLight curLight = lights.get(i);
            curLight.terminate();
            lights.remove(i);
        }

        lightThreads = new ArrayList<Thread>();

        // Add in new lights (resets to green and resets timers)
        for (int i = 0; i < sourcePanel.getNumLights(); i++) {
            lights.add(new AnimatedLight(((i * LIGHT_SPACING_PIXELS) + LIGHT_HORI_START_PIXELS), 0));
        }

        // Set back up threads for the lights
        for (int i = 0; i < lights.size(); i++) {
            AnimatedLight curLight = lights.get(i);
            Thread tempThread = new Thread(curLight, "Light Thread " + i);
            lightThreads.add(tempThread);
        }

        // Remove all existing cars, terminating their threads first
        for (int i = cars.size() - 1; i >= 0; i--) {
            AnimatedCar curCar = cars.get(i);
            curCar.terminate();
            cars.remove(i);
        }

        carThreads = new ArrayList<Thread>();

        // Add in new cars (moves to 0 and resets speeds)
        Random rand = new Random();
        try {
            for (int i = 0; i < sourcePanel.getNumCars(); i++) {
                cars.add(new AnimatedCar(0, ((i * CAR_SPACING_PIXELS) + CAR_VERT_START_PIXELS), ((rand.nextDouble() * (CAR_MAX_SPEED_PIXELS - 1)) + 1)));
            }
        } catch (IOException e) {
            System.out.println("Error getting car image.");
        }

        // Set back up threads for the lights
        for (int i = 0; i < cars.size(); i++) {
            AnimatedCar curCar = cars.get(i);
            Thread tempThread = new Thread(curCar, "Car Thread " + i);
            carThreads.add(tempThread);
        }
    }

    private void pause() {
        for (AnimatedLight curLight : lights) {
            curLight.pause();
        }

        for (AnimatedCar curCar : cars) {
            curCar.pause();
        }
    }

    private void unpause() {
        for (AnimatedLight curLight : lights) {
            curLight.unpause();
        }

        for (AnimatedCar curCar : cars) {
            curCar.unpause();
        }
    }

    private void startHelper() {
        for (Thread curThread : lightThreads) {
            curThread.start();
        }

        for (Thread curThread : carThreads) {
            curThread.start();
        }
    }

    @Override
    public Dimension getPreferredSize() {
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
        if (!lock.tryLock())
        {
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
        int carImgHeight = 50;
        int carImgWidth = 100;
        try {
            carImgWidth = ImageIO.read(new File(AnimatedCar.getCarImageLocation())).getWidth();
            carImgHeight = ImageIO.read(new File(AnimatedCar.getCarImageLocation())).getHeight();
        } catch (IOException e) {
            System.out.println("Car image couldn't be loaded.");
        }
        for (int i = 0; i < lights.size() + 2; i++) {
            g.drawLine((carImgWidth + (i * LIGHT_SPACING_PIXELS)), carImgHeight,
                    (carImgWidth + (i * LIGHT_SPACING_PIXELS)), (int) windowSize.getHeight());
        }

        // Finally, release the lock to indicate the painting is over
        lock.unlock();
    }

    /*
     * The rest of this is event handling code copied from
     * DefaultBoundedRangeModel.
     */
    public void addChangeListener(ChangeListener l) {
        listenerList.add(ChangeListener.class, l);
    }

    public void removeChangeListener(ChangeListener l) {
        listenerList.remove(ChangeListener.class, l);
    }

    protected void fireStateChanged() {
        Object[] listeners = listenerList.getListenerList();
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
