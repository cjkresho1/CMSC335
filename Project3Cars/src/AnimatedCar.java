/**
 * AnimatedCar.java
 * Date: 10.10.23
 * @author Charles Kresho
 * Purpose: AnimatedCar is essentially a BufferedImage of a car (that I drew) that can move across the screen from left to right at a predetermined pace.
 *          It also listens for stop lights (and when the number of stop lights change)
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

// TODO Document

public class AnimatedCar implements ChangeListener, Runnable {
    private BufferedImage car;
    private double xPos;
    private double yPos;
    private double curXVelocity, baseXVelocity;
    private boolean isPaused, terminate;
    private static final String CAR_IMAGE_LOCATION = "imageFiles" + File.separator + "car.png";
    private final Lock lock = new ReentrantLock();
    private final Condition unpauseCondition = lock.newCondition();
    private int finishLine;
    private ArrayList<Integer> stoppingLocs = new ArrayList<Integer>();

    private static final double REFRESH_RATE = 144.0;

    /**
     * Create a new AnimatedCar
     * @param x Starting y position of the car
     * @param y Starting y position of the car
     * @param velocity Rate at which the car will left and right (positive = left; negative = right)
     * @param isPaused
     * @throws IOException
     */
    public AnimatedCar(int x, int y, double velocity, int finishLinePixel) throws IOException {
        xPos = x;
        yPos = y;
        curXVelocity = velocity; // Pixels / second
        baseXVelocity = velocity;
        this.isPaused = false;
        car = ImageIO.read(new File(CAR_IMAGE_LOCATION));
        terminate = false;
        this.finishLine = finishLinePixel - car.getWidth();
    }

    @Override
    public void run() {
        while (true) {
            if (terminate) {
                break;
            }

            lock.lock();
            try {
                while (isPaused) {
                    unpauseCondition.await();
                }
                try {
                    Thread.sleep((long) (1000.0 / REFRESH_RATE));
                } catch (InterruptedException e) {
                }

                if (xPos >= finishLine) {
                    break;
                }

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

                xPos += curXVelocity / REFRESH_RATE;
            } catch (InterruptedException e) {
            } finally {
                lock.unlock();
            }
        }
    }

    // Listens for lights changing to red or green or for if the finish line changes place
    @Override
    public void stateChanged(ChangeEvent e) {
        Object o = e.getSource();
        if (o instanceof AnimatedLight) {
            AnimatedLight curLight = (AnimatedLight) o;
            switch (curLight.getLightState()) {
                case GREEN:
                    stoppingLocs.remove(stoppingLocs.indexOf(curLight.getxPos() - car.getWidth()));
                    break;
                case RED:
                    stoppingLocs.add(curLight.getxPos() - car.getWidth());
                    break;
                default:
                    break;
            }
        } else if (o instanceof CarsPanel) {
            CarsPanel carPanel = (CarsPanel) o;
            finishLine = carPanel.getFinishLinePixel() - car.getWidth();
            if (xPos > finishLine) {
                xPos = finishLine;
            }
            int finishLineIndex = stoppingLocs.indexOf(finishLine);
            if (finishLineIndex >= 0) {
                stoppingLocs.remove(finishLineIndex);
            }
        }
    }

    public void pause() {
        while (!isPaused) {
            isPaused = true;
        }
    }

    public void unpause() {
        while (isPaused) {
            lock.lock();
            isPaused = false;
            unpauseCondition.signalAll();
            lock.unlock();
        }
    }

    public void terminate() {
        terminate = true;
        unpause();
    }

    public BufferedImage getCar() {
        return car;
    }

    public int getxPos() {
        return (int) xPos;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public int getyPos() {
        return (int) yPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    public double getCurXVelocity() {
        return curXVelocity;
    }

    public void setCurXVelocity(double xVelocity) {
        this.curXVelocity = xVelocity;
    }

    public static String getCarImageLocation() {
        return CAR_IMAGE_LOCATION;
    }

    public void addRedLightLocation(int redLightLocPixel) {
        stoppingLocs.add(redLightLocPixel);
    }
}