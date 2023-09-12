/**
 * ThreeDimensionalShape.java 
 * Date: 08.29.2023
 * @author Charles Kresho
 * Purpose: Represent a three dimensional shape, and store all information of one. 
 */
class ThreeDimensionalShape extends Shape {
    private double volume;

    /**
     * Create a new ThreeDimensionalShape object
     * @param shapeName_ name of the shape
     * @param volume_ volume of the shape
     */
    public ThreeDimensionalShape(String shapeName_, double volume_) {
        super(3, shapeName_, "volume", volume_);
        volume = volume_;
    }

    /**
     * Get the volume of the shape
     * @return volume of the shape
     */
    public double getVolume() {
        return volume;
    }
}