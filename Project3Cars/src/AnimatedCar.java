
/**
 * AnimatedCar.java
 * Date: 10.10.23
 * @author Charles Kresho
 * Purpose: AnimatedCar is essentially a BufferedImage of a car (that I drew) that can move across the screen from left to right at a predetermined pace.
 *          It also listens for stop lights (and when the number of stop lights change) so the car knows when to stop.
 */
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.imageio.ImageIO;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class AnimatedCar implements ChangeListener, Runnable {
    private BufferedImage car; // Car image
    private double xPos;
    private double yPos;
    private double curXVelocity, baseXVelocity; // Cur velocity is the current velocity, baseVelocity whatever speed the car moves at when moving
    private boolean isPaused, terminate; // Flags for indicating what actions need to be taken
    private static final String CAR_IMAGE_LOCATION = "imageFiles" + File.separator + "car.png"; // Static location of the car image file

    // Locks for the thread
    private final Lock lock = new ReentrantLock();
    private final Condition unpauseCondition = lock.newCondition();

    // Track where the car needs to stop
    private int finishLine;
    private ArrayList<Integer> stoppingLocs = new ArrayList<Integer>();

    // Theoretical "frame rate" of the car
    private static final double REFRESH_RATE = 144.0;

    /**
     * 
     * @param x X starting position of the car
     * @param y Y starting position of the car
     * @param velocity Velocity of the car when moving
     * @param finishLinePixel Current position of the finish line for the car (x coordinate)
     * @throws IOException When the car image file can't be loaded
     */
    public AnimatedCar(int x, int y, double velocity, int finishLinePixel) throws IOException {
        xPos = x;
        yPos = y;
        curXVelocity = velocity; // Pixels / second
        baseXVelocity = velocity;
        this.isPaused = false;
        car = ImageIO.read(new File(CAR_IMAGE_LOCATION));
        terminate = false;
        this.finishLine = finishLinePixel - car.getWidth(); // This is because we want the car to stop AT the finish line, not after
    }

    @Override
    public void run() {
        // Run the car to the right forever when not paused or stopped
        while (true) {
            // If the thread has been requested to terminate, break out of the run loop
            if (terminate) {
                break;
            }

            // Lock when checking to see if anything has changed
            lock.lock();
            try {
                // If the car has been paused, await unpause
                while (isPaused) {
                    unpauseCondition.await();
                }

                // While the car runs, sleep for the duration of a "frame"
                try {
                    Thread.sleep((long) (1000.0 / REFRESH_RATE));
                } catch (InterruptedException e) {
                }

                // If the car has reached the finish line, terminate the thread
                if (xPos >= finishLine) {
                    break;
                }

                // Reset for logic below; only works because the car is either moving or it's not moving
                curXVelocity = baseXVelocity;

                for (int stopLoc : stoppingLocs) {
                    // As the car approaches the stop light, this number will approach 0 from the positive side
                    // If the number is positive, the car is in front of the stop light
                    // If the number is negative, the car has passed the stop light
                    int pixelsFromStopLoc = stopLoc - (int) xPos;

                    // Will stop the car if the car is 5 or less pixels in front of the RED LIGHT
                    if ((pixelsFromStopLoc > 0) && (pixelsFromStopLoc < 5)) {
                        curXVelocity = 0;
                    }
                }

                // Over the course of a second, the car will move it's full velocity in pixels
                xPos += curXVelocity / REFRESH_RATE;
            } catch (InterruptedException e) {
            } finally {
                // When done updating the car, release the lock
                lock.unlock();
            }
        }
    }

    // Listens for lights changing to red or green or for if the finish line changes place
    @Override
    public void stateChanged(ChangeEvent e) {
        Object o = e.getSource();

        // If a light threw the state change, check to see if it turned red or green, and add/remove the requisit stopping point
        if (o instanceof AnimatedLight) {
            AnimatedLight curLight = (AnimatedLight) o;
            switch (curLight.getLightState()) {
                case GREEN:
                    int index = stoppingLocs.indexOf(curLight.getxPos() - car.getWidth());
                    // Prevents out of bounds errors...shouldn't happen, but just in case
                    if (index >= 0) {
                        stoppingLocs.remove(index);
                    }
                    break;
                case RED:
                    stoppingLocs.add(curLight.getxPos() - car.getWidth());
                    break;
                default:
                    break;
            }
        }
        // If the panel threw the change, check to see if the finish line moved back or forward
        else if (o instanceof CarsPanel) {
            CarsPanel carPanel = (CarsPanel) o;
            finishLine = carPanel.getFinishLinePixel() - car.getWidth();

            // If the finish line moved "back", move the car "back"
            if (xPos > finishLine) {
                xPos = finishLine;
            }

            // If the light removed was red, the index will be a non-negative index; in that case, remove the stop line
            int finishLineIndex = stoppingLocs.indexOf(finishLine);
            if (finishLineIndex >= 0) {
                stoppingLocs.remove(finishLineIndex);
            }
        }
    }

    /**
     * Pause the current car
     */
    public void pause() {
        isPaused = true;
    }

    /**
     * Unpause the current car
     */
    public void unpause() {
        while (isPaused) {
            // Lock while unpausing so that the car doesn't attempt to change itself whle the pause is being released
            lock.lock();
            isPaused = false;
            unpauseCondition.signalAll();
            lock.unlock();
        }
    }

    /**
     * Add a stopping point for a red light manually (only to be done with extreme care)
     * @param redLightLocPixel
     */
    public void addRedLightLocation(int redLightLocPixel) {
        stoppingLocs.add(redLightLocPixel);
    }

    /**
     * Terminate the thread the current car is running on
     */
    public void terminate() {
        terminate = true;
        // Unpause so that the thread can terminate
        unpause();
    }

    /**
     * Return the car's image
     * @return BufferedImage of the car
     */
    public BufferedImage getCar() {
        return car;
    }

    /**
     * Get the xPosition of the car
     * @return x pixel of the car
     */
    public int getxPos() {
        return (int) xPos;
    }

    /**
     * Set the xPosition of the car
     * @param xPos x pixel of the car
     */
    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    /**
     * Get the yPosition of the car
     * @return y pixel of the car
     */
    public int getyPos() {
        return (int) yPos;
    }

    /**
     * Set the yPosition of the car
     * @param yPos y pixel of the car
     */
    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    /**
     * Return the file location of the car's image
     * @return
     */
    public static String getCarImageLocation() {
        return CAR_IMAGE_LOCATION;
    }
}