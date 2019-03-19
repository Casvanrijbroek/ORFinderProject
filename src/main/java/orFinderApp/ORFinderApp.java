package orFinderApp;

import databaseConnector.ConnectionException;
import databaseConnector.Connector;
import databaseConnector.SearchOption;

import java.sql.SQLException;

public class ORFinderApp {
    /**
     * The query that is currently being handled. The user can replace this query by using the various options to
     * load queries either manually or from the remote database.
     */
    private Query query;
    /**
     * This connector is used to make connections to the database and executes SQL commands.
     */
    private Connector databaseConnector;

    public static void main(String[] args) {
    }

    ORFinderApp() {
        databaseConnector = new Connector();
    }

    public void getQuery(SearchOption searchOption, String value) {
        try {
            databaseConnector.makeConnection();
            query = databaseConnector.getQuery(searchOption, value);
            databaseConnector.closeConnection();

            //TODO show query in the user interface
        } catch (SQLException err) {
            //TODO do something with the user interface
        } catch (ConnectionException err) {
            //TODO do something with the user interface
        }
    }

    public void getQuery(SearchOption searchOption, int value) {
        getQuery(searchOption, String.valueOf(value));
    }
}
