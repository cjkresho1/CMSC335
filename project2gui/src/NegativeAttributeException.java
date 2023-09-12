/**
 * NegativeAttributesException.java 
 * Date: 08.29.2023
 * @author Charles Kresho
 * Purpose: An error to throw when an attribute is negative when it shouldn't be. 
 */
public class NegativeAttributeException extends Exception {
    public NegativeAttributeException(String errorMessage) {
        super(errorMessage);
    }
}
