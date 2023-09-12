/**
 * TwoDimensionalShape.java 
 * Date: 08.29.2023
 * @author Charles Kresho
 * Purpose: Represent a two dimensional shape, and store all information of one. 
 */
class TwoDimensionalShape extends Shape {
    private double area;

    /**
     * Create a new TwoDimensionalShape object
     * @param shapeName_ name of the shape
     * @param area_ area of the shape
     */
    public TwoDimensionalShape(String shapeName_, double area_) {
        super(2, shapeName_, "area", area_);
        area = area_;
    }

    /**
     * Get the area of the shape
     * @return area of the shape
     */
    public double getArea() {
        return area;
    }
}