import java.io.File;
import java.io.IOException;

/**
 * Cone.java 
 * Date: 09.12.2023
 * @author Charles Kresho
 * Purpose: Represent and calculate the volume of a Cone. Assumed to be a right triangluar cone.
 *          Is also a JPanel that paints a png of a Cone to itself.
 */
public class Cone extends ThreeDimensionalShape {

    /**
     * Create a new Cone object.
     * @param radius radius of the cone
     * @param height height of the cone
     * @throws IOException
     */
    public Cone(double radius, double height) throws IOException {
        super("Cone", calculateVolume(radius, height), "imageFiles" + File.separator + "Cone.png");
    }

    private static double calculateVolume(double radius, double height) {
        double volume = Math.PI * Math.pow(radius, 2.0) * (height / 3.0);
        return volume;
    }
}