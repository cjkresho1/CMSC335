import javax.swing.JPanel;

/**
 * Shape.java 
 * Date: 09.12.2023
 * @author Charles Kresho
 * Purpose: Represent and store the information of a shape.
 */
public class Shape extends JPanel {
    private int numberOfDimensions;
    private String shapeName;
    private String attributeName;
    private double attributeValue;

    /**
     * Create a new Shape object
     * @param numDimensions dimensions the shape exists in
     * @param shapeName_ name of the shape
     * @param attributeName_ name of the defining attribute of the shape (area, volume, etc.)
     * @param attributeValue_ value of the defining attribute of the shape
     */
    public Shape(int numDimensions, String shapeName_, String attributeName_, double attributeValue_) {
        numberOfDimensions = numDimensions;
        shapeName = shapeName_;
        attributeName = attributeName_;
        attributeValue = attributeValue_;
    }

    public int getNumDimensions() {
        return numberOfDimensions;
    }

    public String getShapeName() {
        return shapeName;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public double getAttributeValue() {
        return attributeValue;
    }

    public String toString() {
        return String.format("The %s of the %s is %.2f.", attributeName, shapeName, attributeValue);
    }
}