import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.imageio.ImageIO;

// TODO Document

public class AnimatedCar implements Runnable {
    private BufferedImage car;
    private double xPos;
    private double yPos;
    private double xVelocity;
    private boolean isPaused, terminate;
    private static final String CAR_IMAGE_LOCATION = "imageFiles" + File.separator + "car.png";
    private final Lock lock = new ReentrantLock();
    private final Condition notPaused = lock.newCondition();

    /**
     * Create a new AnimatedCar
     * @param x Starting y position of the car
     * @param y Starting y position of the car
     * @param velocity Rate at which the car will left and right (positive = left; negative = right)
     * @param isPaused
     * @throws IOException
     */
    public AnimatedCar(int x, int y, double velocity, boolean isPaused) throws IOException {
        xPos = x;
        yPos = y;
        xVelocity = velocity;
        this.isPaused = isPaused;
        car = ImageIO.read(new File(CAR_IMAGE_LOCATION));
        terminate = false;
    }

    @Override
    public void run() {
        while (true) {
            if (terminate) {
                break;
            }
            if (isPaused) {
                try {
                    while (isPaused) {
                        notPaused.await();
                    }
                } catch (InterruptedException e) {
                }
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }

            xPos += xVelocity;
        }
    }

    public void pause() {
        while (!isPaused) {    
            lock.tryLock();
            isPaused = true;
        }
    }

    public void unpause() {
        while (isPaused) {
            isPaused = false;
            notPaused.notifyAll();
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

    public double getxVelocity() {
        return xVelocity;
    }

    public void setxVelocity(double xVelocity) {
        this.xVelocity = xVelocity;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setMoving(boolean isPaused) {
        this.isPaused = isPaused;
    }

    public static String getCarImageLocation() {
        return CAR_IMAGE_LOCATION;
    }
}