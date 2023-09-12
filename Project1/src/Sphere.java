/**
 * Sphere.java 
 * Date: 08.29.2023
 * @author Charles Kresho
 * Purpose: Represent and calculate the volume of a Sphere.
 */
public class Sphere extends ThreeDimensionalShape {

    /**
     * Create a new Sphere object
     * @param radius radius of the sphere
     */
    public Sphere(double radius) {
        super("Sphere", calculateVolume(radius));
    }

    private static double calculateVolume(double radius) {
        double volume = (4.0 / 3.0) * Math.PI * Math.pow(radius, 3.0);
        return volume;
    }
}