import java.io.File;
import java.io.IOException;

/**
 * Cylinder.java 
 * Date: 09.12.2023
 * @author Charles Kresho
 * Purpose: Represent and calculate the volume of a Cylinder.
 *          Is also a JPanel that paints a png of a Cylinder to itself. 
 */
public class Cylinder extends ThreeDimensionalShape {

    /**
     * Create a new Cylinder object.
     * @param radius radius of the cylinder
     * @param height height of the cylinder
     * @throws IOException
     */
    public Cylinder(double radius, double height) throws IOException {
        super("Cylinder", calculateVolume(radius, height), "imageFiles" + File.separator + "Cylinder.png");
    }

    private static double calculateVolume(double radius, double height) {
        double volume = Math.PI * Math.pow(radius, 2) * height;
        return volume;
    }
}