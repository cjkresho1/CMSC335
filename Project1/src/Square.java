/**
 * Square.java 
 * Date: 08.29.2023
 * @author Charles Kresho
 * Purpose: Represent and calculate the area of a Square. Assumed to have equal length sides.
 */
public class Square extends TwoDimensionalShape {

    /**
     * Create a new Square object
     * @param length length (and height) of the Square
     */
    public Square(double length) {
        super("Circle", calculateArea(length));
    }

    public static double calculateArea(double length) {
        double area = Math.pow(length, 2.0);
        return area;
    }
}