package databaseConnector;

import orFinderApp.Query;
import java.sql.*;

/**
 * This class can be used to make a connection to the owe7_pg2 database hosted on azure.com.
 * Using this class you can retrieve data from the database by submitting queries or make specific changes.
 *
 * @author Cas van Rijbroek
 * @version 1.0
 */
public class Connector {
    private static String SELECT_FROM_HEADER = "SELECT * FROM header WHERE %s = '%s'";
    /**
     * The connection made with the database will be stored here for access to the full class.
     */
    private Connection connection;

    /**
     * This method creates a connection to the database and stores it in the connection variable.
     *
     * @throws SQLException if a database access error occurs or this method is called on a closed connection
     */
    public void makeConnection() throws SQLException {
        String url = "jdbc:mysql://hannl-hlo-bioinformatica-mysqlsrv.mysql.database.azure.com:3306/owe7_pg2" +
                "?useUnicode=true&serverTimezone=UTC";
        String user = "owe7_pg2@hannl-hlo-bioinformatica-mysqlsrv.mysql.database.azure.com";
        String password = "blaat1234";

        connection = DriverManager.getConnection(url, user, password);
    }

    /**
     * This method closes the connection to the database.
     *
     * @throws SQLException if a database access error occurs or this method is called on a closed connection
     */
    public void closeConnection() throws SQLException {
        connection.close();
        connection = null;
    }

    /**
     * Checks whether there currently is a connection established.
     *
     * @throws ConnectionException if there is currently no connection established
     */
    private void checkConnection() throws ConnectionException {
        if (connection == null) {
            throw new ConnectionException("Please create a connection first");
        }
    }

    /**
     * Retrieves a single header entry from the database based on ID or header.
     *
     * @param searchOption an SearchOption enum indicating the attribute type that is to be searched on
     * @param value a String with the attribute that is to be searched on
     * @return a single Query object filled with the data retrieved from the database
     * @throws SQLException if a database access error occurs or this method is called on a closed connection
     * @throws ConnectionException if the method is called without establishing a connection first
     */
    public Query getQuery(SearchOption searchOption, String value) throws SQLException, ConnectionException {
        ResultSet resultSet;
        Statement statement;
        Query query;

        checkConnection();

        statement = connection.createStatement();
        resultSet = statement.executeQuery("SELECT * FROM header WHERE "+searchOption+" = '"+value+"'");

        resultSet.next();
        query = new Query(resultSet.getString("name"), resultSet.getString("sequence"));

        return query;
    }

    /**
     * Retrieves a single header entry from the database based on ID
     *
     * @param searchOption an SearchOption enum indicating the attribute type that is to be searched on
     * @param value an Integer with the ID that is to be searched on
     * @return a single Query object filled with the data retrieved from the database
     * @throws SQLException if a database access error occurs or this method is called on a closed connection
     * @throws ConnectionException if the method is called without establishing a connection first
     */
    public Query getQuery(SearchOption searchOption, int value) throws SQLException, ConnectionException {
        return getQuery(searchOption, Integer.toString(value));
    }

    /**
     * Inserts the data in a Query object into the database
     *
     * @param query the Query that is to be inserted into the database
     * @throws SQLException if a database access error occurs or this method is called on a closed connection
     * @throws ConnectionException if the method is called without establishing a connection first
     */
    public void insertQuery(Query query) throws SQLException, ConnectionException {
        Statement statement;

        checkConnection();

        statement = connection.createStatement();
        //TODO Query must have a getHeader method
    }
}
