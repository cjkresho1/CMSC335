/**
 * Cone.java 
 * Date: 08.29.2023
 * @author Charles Kresho
 * Purpose: Represent and calculate the volume of a Cone. Assumed to be a right triangluar cone.
 */
public class Cone extends ThreeDimensionalShape {

    /**
     * Create a new Cone object.
     * @param radius radius of the cone
     * @param height height of the cone
     */
    public Cone(double radius, double height) {
        super("Cone", calculateVolume(radius, height));
    }

    private static double calculateVolume(double radius, double height) {
        double volume = Math.PI * Math.pow(radius, 2.0) * (height / 3.0);
        return volume;
    }
}