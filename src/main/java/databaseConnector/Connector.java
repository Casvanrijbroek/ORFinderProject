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
 * @version 1.1
 */
public class Connector {
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
     * @throws ConnectionException if the method is called without establishing a connection first
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
     * @throws ConnectionException if the method is called without establishing a connection first
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
     * @param searchOption a SearchOption enum indicating the attribute type that is to be searched on
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
        checkConnection();
        //TODO Query must have a getHeader method
    }

    /**
     * Deletes a query and all related orfs and results from the database.
     *
     * @param searchOption a SearchOption enum indicating the attribute type that is to be searched on
     * @param value a String with the attribute that is to be searched on
     * @throws SQLException if a database access error occurs or this method is called on a closed connection
     * @throws ConnectionException if the method is called without establishing a connection first
     */
    public void deleteQuery(SearchOption searchOption, String value) throws SQLException, ConnectionException {
        Statement statement;
        String deleteCommand;

        deleteCommand = "DELETE %s FROM header " +
                "LEFT JOIN orf on header_id = Header_header_id " +
                "LEFT JOIN result on orf_id = ORF_orf_id " +
                "WHERE %s = '%s'";

        //tempMustUse();
        checkConnection();

        statement = connection.createStatement();
        statement.executeUpdate(String.format(deleteCommand, "result", searchOption, value));
        statement.executeUpdate(String.format(deleteCommand, "orf", searchOption, value));
        statement.executeUpdate(String.format(deleteCommand, "header", searchOption, value));
        statement.close();
    }

    void tempMustUse() throws SQLException {
        Statement statement;
        statement = connection.createStatement();
        statement.executeUpdate("INSERT INTO header VALUES (4, 'delete this', 'ATCG')");
        statement.executeUpdate("INSERT INTO orf VALUES(4, 1, 10, 4)");
        statement.executeUpdate("INSERT INTO result VALUES(4, 4, 'ACC', 'desc', 10, 30, 1, 10, 1, 10)");
        statement.close();
    }

    /**
     * Deletes a query and all related orfs and results from the database.
     *
     * @param searchOption a SearchOption enum indicating the attribute type that is to be searched on
     * @param value an Integer with the ID that is to be searched on
     * @throws SQLException if a database access error occurs or this method is called on a closed connection
     * @throws ConnectionException if the method is called without establishing a connection first
     */
    public void deleteQuery(SearchOption searchOption, int value) throws SQLException, ConnectionException {
        deleteQuery(searchOption, Integer.toString(value));
    }
}
