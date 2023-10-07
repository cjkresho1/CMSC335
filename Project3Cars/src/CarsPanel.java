import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

// TODO Document

public class CarsPanel extends JPanel implements ChangeListener {
    // Stores a list of all cars currently in the program
    private ArrayList<AnimatedCar> cars;
    private ArrayList<AnimatedLight> lights;
    private ButtonsPanel sourcePanel;
    private ChangeEvent changeEvent = null;
    private EventListenerList listenerList = new EventListenerList();

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
        // TODO Finish CarsPanel constructor
        sourcePanel = source;
        sourcePanel.addChangeListener(this);

        cars = new ArrayList<AnimatedCar>();
        lights = new ArrayList<AnimatedLight>();

        Random rand = new Random();
        try {
            for (int i = 0; i < source.getNumCars(); i++) {
                cars.add(new AnimatedCar(0, ((i * CAR_SPACING_PIXELS) + CAR_VERT_START_PIXELS), (rand.nextDouble() * 5),
                        true));
            }
        } catch (IOException e) {
            System.out.println("Error getting car image.");
        }

        for (int i = 0; i < source.getNumLights(); i++) {
            lights.add(new AnimatedLight(((i * LIGHT_SPACING_PIXELS) + LIGHT_HORI_START_PIXELS), 0));
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        // If there are less cars then specified, add more cars to make up for it.
        Random rand = new Random();
        System.out.println("Pre-change:");
        System.out.println("cars.size(): " + cars.size());
        System.out.println("sourcePanel.getNumCars(): " + sourcePanel.getNumCars());
        System.out.println("lights.size(): " + lights.size());
        System.out.println("sourcePanel.getNumLights(): " + sourcePanel.getNumLights());
        for (int i = cars.size(); i < sourcePanel.getNumCars(); i++)
        {
            try {
                cars.add(new AnimatedCar(0, ((i * CAR_SPACING_PIXELS) + CAR_VERT_START_PIXELS), (rand.nextDouble() * CAR_MAX_SPEED_PIXELS),
                            true));
            } catch (IOException e1) {
                System.out.println("Error getting car image file.");
            }
        }

        // If there are more cars than specified, remove ones off the end
        for (int i = cars.size(); i > sourcePanel.getNumCars(); i--)
        {
            cars.remove(i - 1);
        }

        // If there are less lights than specified, add more lights ot make up for it. 
        for (int i = lights.size(); i < sourcePanel.getNumLights(); i++)
        {
            lights.add(new AnimatedLight(((i * LIGHT_SPACING_PIXELS) + LIGHT_HORI_START_PIXELS), 0));
        }

        // If there are more lights than specified, remove ones off the end
        for (int i = lights.size(); i > sourcePanel.getNumLights(); i--)
        {
            lights.remove(i - 1);
        }
        // TODO Lights and Cars all pause and start when needed

        System.out.println("Post-change:");
        System.out.println("cars.size(): " + cars.size());
        System.out.println("sourcePanel.getNumCars(): " + sourcePanel.getNumCars());
        System.out.println("lights.size(): " + lights.size());
        System.out.println("sourcePanel.getNumLights(): " + sourcePanel.getNumLights() + System.lineSeparator());
        
        repaint();
        setSize(getPreferredSize());
        fireStateChanged();
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

        // For each car, paint it where it shows up on the panel
        for (int i = 0; i < cars.size(); i++) {
            AnimatedCar curCar = cars.get(i);
            g.drawImage(curCar.getCar(), curCar.getxPos(), curCar.getyPos(), this);
        }

        // For each light, paint a circle of the specified color where it shows up on the panel
        // TODO paint a line that indicates the location of each light???
        for (int i = 0; i < lights.size(); i++) {
            AnimatedLight curLight = lights.get(i);
            switch (curLight.getLightState()) {
                case GREEN:
                    g.setColor(Color.GREEN);
                    break;
                case RED:
                    g.setColor(Color.RED);
                    break;
                case YELLOW:
                    g.setColor(Color.YELLOW);
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
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        for (int i = 0; i < lights.size() + 2; i++) {
            g.drawLine((carImgWidth + (i * LIGHT_SPACING_PIXELS)), carImgHeight,
                    (carImgWidth + (i * LIGHT_SPACING_PIXELS)), (int) windowSize.getHeight());
        }
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
