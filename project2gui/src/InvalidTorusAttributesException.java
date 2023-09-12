/**
 * InvalidTorusAttributesException.java 
 * Date: 08.29.2023
 * @author Charles Kresho
 * Purpose: An error to throw when the minor radius of a torus is greater than the major radius.
 */
public class InvalidTorusAttributesException extends Exception {
    public InvalidTorusAttributesException(String errorMessage) {
        super(errorMessage);
    }
}
