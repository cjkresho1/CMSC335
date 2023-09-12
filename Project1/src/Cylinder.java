/**
 * Cylinder.java 
 * Date: 08.29.2023
 * @author Charles Kresho
 * Purpose: Represent and calculate the volume of a Cylinder.
 */
public class Cylinder extends ThreeDimensionalShape {

    /**
     * Create a new Cylinder object.
     * @param radius radius of the cylinder
     * @param height height of the cylinder
     */
    public Cylinder(double radius, double height) {
        super("Cylinder", calculateVolume(radius, height));
    }

    private static double calculateVolume(double radius, double height) {
        double volume = Math.PI * Math.pow(radius, 2) * height;
        return volume;
    }
}