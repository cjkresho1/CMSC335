import java.awt.Graphics;

/**
 * Rectangle.java 
 * Date: 09.12.2023
 * @author Charles Kresho
 * Purpose: Represent and calculate the area of a Rectangle.
 *          Is also a JPanel that paints a rectangle to itself.
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
        this.length = length;
        this.height = height;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.fillRect(0, 0, (int) length * 2, (int) height * 2);
    }

    public static double calculateArea(double length, double height) {
        double area = length * height;
        return area;
    }
}