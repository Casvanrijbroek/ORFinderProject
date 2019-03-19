package databaseConnector;

/**
 * This exception can be thrown to indicate the mishandling of a connection.
 * For example the calling of a method that requires a connection while no connection is present or inserting a value
 * that already exists.
 *
 * @author Cas van Rijbroek
 * @version 1.0
 */
class ConnectionException extends Exception {

    /**
     * The default constructor to call its super class
     */
    ConnectionException() {
        super();
    }

    /**
     * Constructor that adds a message to its super class
     *
     * @param message The message that is to be added
     */
    ConnectionException(String message) {
        super(message);
    }
}
