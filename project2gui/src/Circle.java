import java.awt.Graphics;

/**
 * Circle.java 
 * Date: 09.12.2023
 * @author Charles Kresho
 * Purpose: Represent and calculate the area of a Circle. A
 *          Is also a JPanel that paints a Circle to itself. 
 */
public class Circle extends TwoDimensionalShape {

    double radius;

    /**
     * Construct a new Circle object.
     * @param radius radius of the circle
     */
    public Circle(double radius) {
        super("Circle", calculateArea(radius));

        this.radius = radius;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.fillOval(0, 0, (int) radius * 2, (int) radius * 2);
    }

    private static double calculateArea(double radius) {
        double area = Math.PI * Math.pow(radius, 2.0);
        return area;
    }
}