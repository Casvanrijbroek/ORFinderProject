package databaseConnector;

import ORFinderApp.Query;

import java.sql.*;

public class Connector {
    private Connection connection;

    private void makeConnection() throws SQLException {
        String url = "jdbc:mysql://hannl-hlo-bioinformatica-mysqlsrv.mysql.database.azure.com:3306/owe7_pg2" +
                "?useUnicode=true&serverTimezone=UTC";
        String user = "owe7_pg2@hannl-hlo-bioinformatica-mysqlsrv.mysql.database.azure.com";
        String password = "blaat1234";

        connection = DriverManager.getConnection(url, user, password);
    }

    private void closeConnection() throws SQLException {
        connection.close();
    }

    public Query getQuery(SearchOption searchOption, String value) throws SQLException {
        ResultSet resultSet;
        Statement statement;
        Query query;

        makeConnection();

        statement = connection.createStatement();
        resultSet = statement.executeQuery("SELECT * FROM header WHERE "+searchOption+" = '"+value+"'");

        resultSet.next();
        query = new Query(resultSet.getString("name"), resultSet.getString("sequence"));


        closeConnection();

        return query;
    }

    public Query getQuery(SearchOption searchOption, int value) throws SQLException {
        return getQuery(searchOption, Integer.toString(value));
    }
}
