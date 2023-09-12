import java.awt.Graphics;

/**
 * Square.java 
 * Date: 08.29.2023
 * @author Charles Kresho
 * Purpose: Represent and calculate the area of a Square. Assumed to have equal length sides.
 */
public class Square extends TwoDimensionalShape {

    private double length;

    /**
     * Create a new Square object
     * @param length length (and height) of the Square
     */
    public Square(double length) {
        super("Square", calculateArea(length));
        this.length = length;
    }

    public void paint(Graphics g) {
        super.paint(g);
        g.fillRect(0, 0, (int) length * 2, (int) length * 2);
    }

    public static double calculateArea(double length) {
        double area = Math.pow(length, 2.0);
        return area;
    }
}