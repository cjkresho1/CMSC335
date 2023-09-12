import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * ThreeDimensionalShape.java 
 * Date: 09.12.2023
 * @author Charles Kresho
 * Purpose: Represent a three dimensional shape, and store all information of one. 
 */
class ThreeDimensionalShape extends Shape {
    private double volume;
    private Image img;

    /**
     * Create a new ThreeDimensionalShape object
     * @param shapeName_ name of the shape
     * @param volume_ volume of the shape
     * @param imageLocation Absolute path to the image to be displayed
     * @throws IOException If the image file cannot be opened.
     */
    public ThreeDimensionalShape(String shapeName_, double volume_, String imageLocation) throws IOException {
        super(3, shapeName_, "volume", volume_);
        File imageFile = new File(imageLocation);
        img = ImageIO.read(imageFile);
        volume = volume_;
    }

    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(img, 0, 0, null);
    }

    /**
     * Get the volume of the shape
     * @return volume of the shape
     */
    public double getVolume() {
        return volume;
    }
}