import java.awt.Graphics;

/**
 * Triangle.java 
 * Date: 08.29.2023
 * @author Charles Kresho
 * Purpose: Represent and calculate the area of a Triangle. Assumed to be a right triangle.
 */
public class Triangle extends TwoDimensionalShape {

    private double length;
    private double height;

    /**
     * Create a new Triangle object
     * @param length lenght of the triangle
     * @param height height of the triangle
     */
    public Triangle(double length, double height) {
        super("Triangle", calculateArea(length, height));
    }

    public void paint(Graphics g) {
        super.paint(g);
        int[] x = { 0, (int) (length / 2), (int) length };
        int[] y = { (int) height, 0, (int) height };
        g.fillPolygon(x, y, 3);
    }

    private static double calculateArea(double length, double height) {
        double area = length * height / 2.0;
        return area;
    }
}
