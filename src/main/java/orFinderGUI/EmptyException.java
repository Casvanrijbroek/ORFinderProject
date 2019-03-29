package orFinderGUI;

/**
 * This exception can be thrown when trying to handle a non-existent file.
 *
 * @author Elco van Rijswijk
 * @version 1.0
 * 29-03-2019
 */
public class EmptyException extends Exception {

    /**
     * The default constructor to call its super class
     */
    public EmptyException() {
        super();
    }

    /**
     * Constructor that adds a message to its super class
     *
     * @param message The message that is to be added
     */
    public EmptyException(String message) {
        super(message);
    }
}