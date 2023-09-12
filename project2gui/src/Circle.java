/**
 * Circle.java 
 * Date: 08.29.2023
 * @author Charles Kresho
 * Purpose: Represent and calculate the area of a Circle.
 */
public class Circle extends TwoDimensionalShape {

    /**
     * Construct a new Circle object.
     * @param radius radius of the circle
     */
    public Circle(double radius) {
        super("Circle", calculateArea(radius));
    }

    private static double calculateArea(double radius) {
        double area = Math.PI * Math.pow(radius, 2.0);
        return area;
    }
}