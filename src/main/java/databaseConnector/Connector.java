package databaseConnector;

import orFinderApp.ORF;
import orFinderApp.Query;
import orFinderApp.Result;

import java.sql.*;
import java.util.ArrayList;

/**
 * This class can be used to make a connection to the owe7_pg2 database hosted on azure.com.
 * Using this class you can retrieve data from the database by submitting queries or make specific changes.
 *
 * Known bug: The deletion of a non-existing result is considered a success.
 *
 * @author Cas van Rijbroek
 * @version 1.3
 * 27-03-2019
 */
public class Connector {
    /**
     * The connection made with the database will be stored here for access to the full class.
     */
    private Connection connection;

    /**
     * This method creates a connection to the database and stores it in the connection variable.
     *
     * @throws SQLException if a database access showPopupError occurs or this method is called on a closed connection
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
     * @throws SQLException if a database access showPopupError occurs or this method is called on a closed connection
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
     * Retrieves ORFs related to a header and adds a list of ORFs generated from this data to a Query object.
     * If no ORFs are found an empty initialised ArrayList will be added to the Query.
     *
     * @param query the Query to add the ORFs to
     * @param headerID the header id of the header to get ORFs from
     * @throws SQLException if a database access showPopupError occurs or this method is called on a closed connection
     */
    private void addOrfList(Query query, int headerID) throws SQLException {
        ArrayList<ORF> orfList;
        ResultSet resultSet;
        Statement statement;
        ORF orf;

        statement = connection.createStatement();
        resultSet = statement.executeQuery(String.format("SELECT * FROM orf WHERE Header_header_id = %d", headerID));

        orfList = new ArrayList<>();

        while(resultSet.next()) {
            orf = new ORF(resultSet.getInt("start_pos"), resultSet.getInt("stop_pos"));
            orfList.add(orf);
            addResultList(orf, resultSet.getInt("orf_id"));
        }

        query.setOrfList(orfList);
    }

    /**
     * Retrieves Results related to a header and adds a list of Results generated from this data to a ORF object.
     * If no Results are found an empty initialised ArrayList will be added to the ORF.
     *
     * @param orf the ORF to add the ORFs to
     * @param orfID the orf id of the header to get Results from
     * @throws SQLException if a database access showPopupError occurs or this method is called on a closed connection
     */
    private void addResultList(ORF orf, int orfID) throws SQLException {
        ArrayList<Result> resultList;
        ResultSet resultSet;
        Statement statement;
        Result result;

        statement = connection.createStatement();
        resultSet = statement.executeQuery(String.format("SELECT * FROM result WHERE ORF_orf_id = %d", orfID));

        resultList = new ArrayList<>();

        while (resultSet.next()) {
            result = new Result();

            result.setAccession(resultSet.getString("accession"));
            result.setDescription(resultSet.getString("description"));
            result.setScore(resultSet.getInt("score"));
            result.setQueryCover(resultSet.getInt("query_cover"));
            result.setpValue(resultSet.getFloat("e_value"));
            result.setIdentity(resultSet.getInt("ident"));
            result.setStartPosition(resultSet.getInt("start_pos"));
            result.setStopPosition(resultSet.getInt("stop_pos"));

            resultList.add(result);
        }

        orf.setResultList(resultList);
    }

    /**
     * Retrieves a single header entry from the database based on ID or header.
     *
     * @param searchOption a SearchOption enum indicating the attribute type that is to be searched on
     * @param value a String with the attribute that is to be searched on
     * @return a single Query object filled with the data retrieved from the database
     * @throws SQLException if a database access showPopupError occurs or this method is called on a closed connection
     * @throws ConnectionException if the method is called without establishing a connection first
     */
    public Query getQuery(SearchOption searchOption, String value) throws SQLException, ConnectionException {
        ResultSet resultSet;
        Statement statement;
        Query query;

        checkConnection();

        statement = connection.createStatement();
        resultSet = statement.executeQuery(String.format("SELECT * FROM header WHERE %s = '%s'", searchOption, value));

        resultSet.next();
        query = new Query(resultSet.getString("name"), resultSet.getString("sequence"));

        addOrfList(query, resultSet.getInt("header_id"));

        statement.close();

        return query;
    }

    /**
     * Retrieves a single header entry from the database based on ID
     *
     * @param searchOption a SearchOption enum indicating the attribute type that is to be searched on
     * @param value an Integer with the ID that is to be searched on
     * @return a single Query object filled with the data retrieved from the database
     * @throws SQLException if a database access showPopupError occurs or this method is called on a closed connection
     * @throws ConnectionException if the method is called without establishing a connection first
     */
    public Query getQuery(SearchOption searchOption, int value) throws SQLException, ConnectionException {
        return getQuery(searchOption, Integer.toString(value));
    }

    /**
     * Inserts the data in a Query object into the database
     *
     * @param query the Query that is to be inserted into the database
     * @throws SQLException if a database access showPopupError occurs or this method is called on a closed connection
     * @throws ConnectionException if the method is called without establishing a connection first or when the given
     * query already exists in the database.
     */
    public void insertQuery(Query query) throws SQLException, ConnectionException {
        Statement statement;
        int newIdentifier;

        checkConnection();

        statement = connection.createStatement();
        newIdentifier = getNewIdentifier("header_id", "header");

        if (queryExists(query)) {
            throw new ConnectionException("Dit resultaat bestaat al in de database, verwijder het aub of verander" +
                    "de naam van de header");
        }

        statement.executeUpdate(String.format(
                "INSERT INTO header VALUES (%d, '%s', '%s')",
                newIdentifier, query.getHeader(), query.getSequence()));

        if (query.getOrfList() != null) {
            for (ORF orf : query.getOrfList()) {
                insertORF(orf, newIdentifier);
            }
        }

        statement.close();
    }

    /** Inserts an ORF into the database.
     *
     * @param orf the orf that is to be inserted
     * @param headerIdentifier the foreign key for the header belonging to the orf
     * @throws SQLException if a database access showPopupError occurs or this method is called on a closed connection
     */
    private void insertORF(ORF orf, int headerIdentifier) throws SQLException {
        Statement statement;
        int newIdentifier;

        statement = connection.createStatement();
        newIdentifier = getNewIdentifier("orf_id", "orf");

        statement.executeUpdate(String.format("INSERT INTO orf VALUES (%d, '%s', '%s', %d)",
                newIdentifier, orf.getStartPosition(), orf.getStopPosition(), headerIdentifier));

        if (orf.getResultList() != null) {
            for (Result result : orf.getResultList()) {
                insertResult(result, newIdentifier);
            }
        }
    }

    /**
     * Inserts a Result into the database.
     *
     * @param result the result that is to be inserted
     * @param orfIdentifier the foreign key for the orf belonging to the result
     * @throws SQLException if a database access showPopupError occurs or this method is called on a closed connection
     */
    private void insertResult(Result result, int orfIdentifier) throws SQLException {
        Statement statement;
        int newIdentifier;

        statement = connection.createStatement();
        newIdentifier = getNewIdentifier("result_id", "result");

        statement.executeUpdate(String.format("INSERT INTO result VALUES (%d, %d, '%s', '%s', %d, %d, %s, %d, %d, %d)",
                newIdentifier, orfIdentifier, result.getAccession(), result.getDescription(), result.getScore(),
                result.getQueryCover(), String.valueOf(result.getpValue()), result.getIdentity(),
                result.getStartPosition(), result.getStopPosition()));
    }

    /**
     * Checks if a certain query exists in the database based on the header.
     *
     * @param header the header that you want to use
     * @return true if the query exists in the database, else false
     * @throws SQLException if a database access showPopupError occurs or this method is called on a closed connection
     */
    private boolean queryExists(String header) throws SQLException {
        return connection.createStatement().executeQuery(
                String.format("SELECT name FROM header WHERE name = '%s'", header)).next();
    }

    /**
     * Checks if a certain query exists in the database based on the header. This method overloads the method that
     * checks this and instead accepts a Query object. It extracts the header from the Query.
     *
     * @param query the query containing the header that you want to use
     * @return true if the query exists in the database, else false
     * @throws SQLException if a database access showPopupError occurs or this method is called on a closed connection
     */
    private boolean queryExists(Query query) throws SQLException {
        return queryExists(query.getHeader());
    }

    /**
     * Gets the maximum identifier of a table in the database and adds 1 to that number, creating a new unique
     * identifier to insert a new attribute with.
     *
     * @param id the id attribute that is to be searched on
     * @param table the table containing the id that is to be searched on
     * @return the new unique identifier
     * @throws SQLException if a database access showPopupError occurs or this method is called on a closed connection
     */
    private int getNewIdentifier(String id, String table) throws SQLException {
        ResultSet resultSet;
        Statement statement;

        statement = connection.createStatement();
        resultSet = statement.executeQuery(String.format("SELECT MAX(%s)+1 max_id FROM %s", id, table));
        resultSet.next();

        return resultSet.getInt("max_id");
    }

    /**
     * Deletes a query and all related orfs and results from the database.
     *
     * @param searchOption a SearchOption enum indicating the attribute type that is to be searched on
     * @param value a String with the attribute that is to be searched on
     * @throws SQLException if a database access showPopupError occurs or this method is called on a closed connection
     * @throws ConnectionException if the method is called without establishing a connection first
     */
    public void deleteQuery(SearchOption searchOption, String value) throws SQLException, ConnectionException {
        Statement statement;
        String deleteCommand;

        deleteCommand = "DELETE %s FROM header " +
                "LEFT JOIN orf on header_id = Header_header_id " +
                "LEFT JOIN result on orf_id = ORF_orf_id " +
                "WHERE %s = '%s'";

        checkConnection();

        statement = connection.createStatement();
        statement.executeUpdate(String.format(deleteCommand, "result", searchOption, value));
        statement.executeUpdate(String.format(deleteCommand, "orf", searchOption, value));
        statement.executeUpdate(String.format(deleteCommand, "header", searchOption, value));
        statement.close();
    }

    /**
     * Deletes a query and all related orfs and results from the database.
     *
     * @param searchOption a SearchOption enum indicating the attribute type that is to be searched on
     * @param value an Integer with the ID that is to be searched on
     * @throws SQLException if a database access showPopupError occurs or this method is called on a closed connection
     * @throws ConnectionException if the method is called without establishing a connection first
     */
    public void deleteQuery(SearchOption searchOption, int value) throws SQLException, ConnectionException {
        deleteQuery(searchOption, Integer.toString(value));
    }
}
