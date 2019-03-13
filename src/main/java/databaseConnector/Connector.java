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
     * Retrieves ORFs related to a header and adds a list of ORFs generated from this data to a Query object.
     * If no ORFs are found an empty initialised ArrayList will be added to the Query.
     *
     * @param query the Query to add the ORFs to
     * @param headerID the header id of the header to get ORFs from
     * @throws SQLException if a database access error occurs or this method is called on a closed connection
     */
    private void addOrfList(Query query, int headerID) throws SQLException {
        ArrayList<ORF> orfList;
        ResultSet resultSet;
        Statement statement;
        ORF orf;

        statement = connection.createStatement();
        resultSet = statement.executeQuery("SELECT * FROM orf WHERE Header_header_id = "+headerID);
        statement.close();

        orfList = new ArrayList<>();

        while(resultSet.next()) {
            orf = new ORF(resultSet.getInt("start_pos"), resultSet.getInt("stop_pos"));
            orfList.add(orf);
            addResultList(orf, resultSet.getInt("orf_id"));
        }

        //query.setOrfList(orfList);
        //TODO addORfList asks for an ORF, not an ArrayList
    }

    /**
     * Retrieves Results related to a header and adds a list of Results generated from this data to a ORF object.
     * If no Results are found an empty initialised ArrayList will be added to the ORF.
     *
     * @param orf the ORF to add the ORFs to
     * @param orfID the orf id of the header to get Results from
     * @throws SQLException if a database access error occurs or this method is called on a closed connection
     */
    private void addResultList(ORF orf, int orfID) throws SQLException {
        ArrayList<Result> resultList;
        ResultSet resultSet;
        Statement statement;
        Result result;

        statement = connection.createStatement();
        resultSet = statement.executeQuery("SELECT * FROM result WHERE ORF_orf_id = "+orfID);
        statement.close();

        resultList = new ArrayList<>();

        while (resultSet.next()) {
            result = new Result();

            result.setAccession(resultSet.getString("accession"));
            result.setDescription(resultSet.getString("description"));
            result.setScore(resultSet.getInt("score"));
            //result.setQueryCover(resultSet.getFloat("query_cover"));
            //TODO float in db int in Result
            result.setpValue(resultSet.getFloat("p_value"));
            result.setIdentity(resultSet.getFloat("ident"));
            result.setStartPosition(resultSet.getInt("start_pos"));
            result.setStopPosition(resultSet.getInt("stop_pos"));
        }

        //result.setResultList(resultList);
        //TODO no set method for resultList
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
        statement.close();

        resultSet.next();
        query = new Query(resultSet.getString("name"), resultSet.getString("sequence"));

        addOrfList(query, resultSet.getInt("header_id"));

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
