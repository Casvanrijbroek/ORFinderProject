package blastConnetor;

/**
 * This exception can be thrown when no connection with the BLAST database is possible
 *
 * @author Lex Bosch
 * @version V1.0
 */

public class NoBlastConnectionException extends Exception {

    /**
     * The default constructor to call its super class
     */
    NoBlastConnectionException() {
        super();
    }

    /**
     * Constructor that adds a message to its super class
     *
     * @param message The message that is to be added
     */
    NoBlastConnectionException(String message) {
        super(message);
    }
}