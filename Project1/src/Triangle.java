/**
 * Triangle.java 
 * Date: 08.29.2023
 * @author Charles Kresho
 * Purpose: Represent and calculate the area of a Triangle. Assumed to be a right triangle.
 */
public class Triangle extends TwoDimensionalShape {

    /**
     * Create a new Triangle object
     * @param length lenght of the triangle
     * @param height height of the triangle
     */
    public Triangle(double length, double height) {
        super("Circle", calculateArea(length, height));
    }

    private static double calculateArea(double length, double height) {
        double area = length * height / 2.0;
        return area;
    }
}
