import java.io.File;
import java.io.IOException;

/**
 * Sphere.java 
 * Date: 09.12.2023
 * @author Charles Kresho
 * Purpose: Represent and calculate the volume of a Sphere.
 *          Is also a JPanel that paints a png of a Sphere to itself. 
 */
public class Sphere extends ThreeDimensionalShape {

    /**
     * Create a new Sphere object
     * @param radius radius of the sphere
     * @throws IOException
     */
    public Sphere(double radius) throws IOException {
        super("Sphere", calculateVolume(radius), "imageFiles" + File.separator + "Sphere.png");
    }

    private static double calculateVolume(double radius) {
        double volume = (4.0 / 3.0) * Math.PI * Math.pow(radius, 3.0);
        return volume;
    }
}