import java.io.File;
import java.io.IOException;

/**
 * Torus.java 
 * Date: 09.12.2023
 * @author Charles Kresho
 * Purpose: Represent and calculate the volume of a Torus.
 *          Is also a JPanel that paints a png of a Torus to itself.
 */
public class Torus extends ThreeDimensionalShape {

    /**
     * Create a new Torus object.
     * @param majorRadius distance from the center of the torus to the center of the torus's ring
     * @param minorRadius radius of the torus's ring
     * @throws InvalidTorusAttributesException if minor radius is greater than major radiusF
     * @throws IOException
     */
    public Torus(double majorRadius, double minorRadius) throws InvalidTorusAttributesException, IOException {
        super("Torus", calculateVolume(majorRadius, minorRadius), "imageFiles" + File.separator + "Torus.png");
    }

    private static double calculateVolume(double majorRadius, double minorRadius)
            throws InvalidTorusAttributesException {
        if (minorRadius > majorRadius) {
            throw new InvalidTorusAttributesException("The minor radius cannot be greater than the major radius.");
        }
        double volume = (Math.PI * Math.pow(minorRadius, 2.0)) * (2.0 * Math.PI * majorRadius);
        return volume;
    }
}