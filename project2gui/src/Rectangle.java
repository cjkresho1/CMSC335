import java.awt.Graphics;

/**
 * Rectangle.java 
 * Date: 08.29.2023
 * @author Charles Kresho
 * Purpose: Represent and calculate the area of a Rectangle.
 */
public class Rectangle extends TwoDimensionalShape {

    private double length;
    private double height;

    /**
     * Create a new Rectangle object.
     * @param length length of the rectangle
     * @param height height of the rectangle
     */
    public Rectangle(double length, double height) {
        super("Rectangle", calculateArea(length, height));
    }

    public void paint(Graphics g) {
        super.paint(g);
        g.fillRect(0, 0, (int) length, (int) height);
    }

    public static double calculateArea(double length, double height) {
        double area = length * height;
        return area;
    }
}