package orFinderGUI;

/**
 * Can be thrown when handling a file with improper layout.
 * For example a FASTA file with too many sequences for the application to handle.
 *
 * @author Elco van Rijwsijk
 * @version 1.0
 * 29-02-2019
 */
public class WrongFileException extends Exception {

    /**
     * The default constructor to call its super class
     */
    public WrongFileException() {
        super();
    }

    /**
     * Constructor that adds a message to its super class
     *
     * @param message The message that is to be added
     */
    public WrongFileException(String message) {
        super(message);
    }
}
