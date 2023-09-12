/**
 * Cube.java 
 * Date: 08.29.2023
 * @author Charles Kresho
 * Purpose: Represent and calculate the volume of a Cube. Assumed to not have equal length sides.
 */
public class Cube extends ThreeDimensionalShape {

    /**
     * Create a new Cube object.
     * @param length lenght of the cube
     * @param width width of the cube
     * @param height height of the cube
     */
    public Cube(double length, double width, double height) {
        super("Cube", calculateVolume(height, length, width));
    }

    private static double calculateVolume(double length, double width, double height) {
        double volume = length * width * height;
        return volume;
    }
}