package databaseConnector;

import ORFinderApp.Query;

import java.sql.*;

public class Connector {
    private Connection connection;

    public Query getQuery(String name) throws SQLException {
        ResultSet resultSet;
        Statement statement;
        Query query;

        makeConnection();

        statement = connection.createStatement();
        resultSet = statement.executeQuery(String.format("SELECT * FROM header WHERE name = '%s'", name));

        resultSet.next();
        query = new Query(resultSet.getString("name"), resultSet.getString("sequence"));

        return query;
    }

    private void makeConnection() throws SQLException {
        String url = "jdbc:mysql://hannl-hlo-bioinformatica-mysqlsrv.mysql.database.azure.com:3306/owe7_pg2" +
                "?useUnicode=true&serverTimezone=UTC";
        String user = "owe7_pg2@hannl-hlo-bioinformatica-mysqlsrv.mysql.database.azure.com";
        String password = "blaat1234";

        connection = DriverManager.getConnection(url, user, password);
    }
}
