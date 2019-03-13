package databaseConnector;

import ORFinderApp.Query;

import java.sql.SQLException;

public class ConnectorTest {
    public static void main(String[] args) {
        try {
            Connector connector;
            Query query;

            connector = new Connector();
            query = connector.getQuery(SearchOption.ID, 1);
            query = connector.getQuery(SearchOption.NAME, "organisme 2");
        } catch (SQLException err) {
            System.out.println("SQLException: " + err.getMessage());
            System.out.println("SQLState: " + err.getSQLState());
            System.out.println("VendorError: " + err.getErrorCode());
        }
    }
}
