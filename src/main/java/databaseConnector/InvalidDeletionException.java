package databaseConnector;

/**
 * This exception can be thrown to indicate the execution of a query that attempts to delete a non-existent item from
 * the database.
 *
 * @author Cas van Rijbroek
 * @version 1.0
 * 27-03-2019
 */
public class InvalidDeletionException extends Exception {

    /**
     * The default constructor to call its super class
     */
    InvalidDeletionException() {
        super();
    }

    /**
     * Constructor that adds a message to its super class
     *
     * @param message The message that is to be added
     */
    InvalidDeletionException(String message) {
        super(message);
    }
}
