import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

// TODO Document

public class AnimatedCar implements Runnable {
    private BufferedImage car;
    private double xPos;
    private double yPos;
    private double xVelocity;
    private boolean isMoving;
    private static final String CAR_IMAGE_LOCATION = "imageFiles" + File.separator + "car.png";

    /**
     * Create a new AnimatedCar
     * @param x Starting y position of the car
     * @param y Starting y position of the car
     * @param velocity Rate at which the car will left and right (positive = left; negative = right)
     * @param isMoving True if the car should start moving, false if the car should be stationary
     * @throws IOException
     */
    public AnimatedCar(int x, int y, double velocity, boolean isMoving) throws IOException {
        xPos = x;
        yPos = y;
        xVelocity = velocity;
        this.isMoving = isMoving;
        car = ImageIO.read(new File(CAR_IMAGE_LOCATION));
    }

    @Override
    public void run() {
        // TODO Add internal timer to move the car right every, like, 100ms
        while (true) {
            if (isMoving) {
                xPos += xVelocity;
            }
        }
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

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean isMoving) {
        this.isMoving = isMoving;
    }

    public static String getCarImageLocation() {
        return CAR_IMAGE_LOCATION;
    }

    
}