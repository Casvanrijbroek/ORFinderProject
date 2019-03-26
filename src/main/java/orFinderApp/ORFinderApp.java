package orFinderApp;

import ORFinderGUI.ORFinderGui;
import blastConnetor.NoBlastConnectionException;
import blastConnetor.proteinBlast;
import databaseConnector.ConnectionException;
import databaseConnector.Connector;
import databaseConnector.SearchOption;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Paths;
import java.sql.SQLException;

/**
 * This is the main class that implements the different packages of the ORFinder. This class can be converted to a JAR
 * file to create a working application. All information and calls trough the application run from the GUI to this
 * class. This class then calls the appropriate methods and gives Objects back where needed.
 *
 * This is also where the Query object is be stored. This Object can be easily converted to an ArrayList in the future
 * to scale the application to handle more queries at once.
 *
 * @author Cas van Rijbroek
 * @version 0.3
 */
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
    /**
     * The ProteinBlast object used to BLAST protein sequences via the NCBI BLAST server.
     */
    private proteinBlast proteinBlast;
    private ORFinderGui orFinderGui;

    /**
     * The static main method that sets up the application. This is where the GUI is visualised.
     *
     * @param args no args are expected to be given since this application is not designed for command line usage
     */
    public static void main(String[] args) {
        new ORFinderApp();
    }

    /**
     * In this constructor instance variables will be set as needed from the different packages. This includes a
     * Connector object to communicate with the database, ...
     */
    private ORFinderApp() {
        JFrame frame;

        databaseConnector = new Connector();
        proteinBlast = new proteinBlast();
        orFinderGui = new ORFinderGui();

        orFinderGui.setORFinderApp(this);
        frame = new JFrame();
        frame.setContentPane(orFinderGui.getGui());
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage(
                Paths.get("src", "main", "resources", "icon.jpg").toString()));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Sets query
     *
     * @param query sets this value into the class variable
     */
    public void setQuery(Query query) {
        this.query = query;
    }

    /**
     * Gets query
     *
     * @return value of query
     */
    public Query getQuery() {
        return query;
    }

    /**
     * This method is used to obtain a query from the database based on it's header or identifier. It utilises the
     * getQuery method of the Connector to retrieve the query and handles exceptions that can be thrown by these
     * methods.
     *
     * @param searchOption a SearchOption enum indicating the attribute type that is to be searched on
     * @param value a String with the attribute that is to be searched on
     * @return true if the fetch was successful, else false
     */
    public boolean getQueryDatabase(SearchOption searchOption, String value) {
        try {
            databaseConnector.makeConnection();
            query = databaseConnector.getQuery(searchOption, value);
            databaseConnector.closeConnection();
        } catch (SQLException err) {
            handleSQLException(err);

            return false;
        } catch (ConnectionException err) {
            //TODO do something with the user interface
        }

        return true;
    }

    /**
     * This method is used to obtain a query from the database based on it's identifier. It utilises the getQuery
     * method of the Connector to retrieve the query and handles exceptions that can be thrown by this method.
     *
     * @param searchOption a SearchOption enum indicating the attribute type that is to be searched on
     * @param value an integer with the identifier that is to be searched on
     * @return true if the fetch was successful, else false
     */
    public boolean getQueryDatabase(SearchOption searchOption, int value) {
        return getQueryDatabase(searchOption, String.valueOf(value));
    }

    /**
     * This method deletes a query from the database based on it's header or identifier. It utilises the deleteQuery
     * method of the Connector to delete the query and handles exceptions that can be thrown by this method.
     *
     * @param searchOption a SearchOption enum indicating the attribute type that is to be searched on
     * @param value a String with the attribute that is to be searched on
     */
    public void deleteQueryDatabase(SearchOption searchOption, String value) {
        try {
            databaseConnector.makeConnection();
            databaseConnector.deleteQuery(searchOption, value);
            databaseConnector.closeConnection();
        } catch(SQLException err) {

        } catch (ConnectionException err) {

        }
    }

    /**
     * This method deletes a query from the database based on it's header or identifier. It utilises the deleteQuery
     * method of the Connector to delete the query and handles exceptions that can be thrown by this method.
     *
     * @param searchOption a SearchOption enum indicating the attribute type that is to be searched on
     * @param value an integer with the attribute that is to be searched on
     */
    public void deleteQueryDatabase(SearchOption searchOption, int value) {
        deleteQueryDatabase(searchOption, String.valueOf(value));
    }

    /**
     * This method inserts a query into the database based on a Query object. The doesn't need to have ORFs or results
     * for this method to work. Do note that you can't update a Query in the database at the current version of the
     * application. So queries will need to be deleted before they can be replaced.
     *
     * It utilises the insertQuery method of the Connector to insert the query and handles exceptions that can be
     * thrown by this method.
     *
     * @param query the Query object that is to be inserted
     */
    public void insertQueryDatabase(Query query) {
        try {
            databaseConnector.makeConnection();
            databaseConnector.insertQuery(query);
            databaseConnector.closeConnection();
        } catch (SQLException err) {

        } catch (ConnectionException err) {

        }
    }

    /**
     * This query uses the proteinBlast to perform a proteinBlast on the found ORFs of a Query object. Note that ORFs
     * have to be present in the Query for this method to work. It handles exceptions that can be thrown by the blast
     * method.
     */
    public void proteinBlastQuery() {
        if (!hasQuery()) {
            //TODO show in GUI
        } else {
            try {
                proteinBlast.blast(query);
            } catch (NoBlastConnectionException err) {
                //TODO show in GUI
            } catch (InterruptedException err) {
                //TODO show in GUI
            }
        }
    }

    private void handleSQLException(SQLException err) {
        if (err.getMessage().equals("Illegal operation on empty result set.")) {
            orFinderGui.setStatusLabel("De opgegeven header heeft niks opgeleverd in de database");

            return;
        } else if (err.getMessage().contains("Communications link failure")) {
            orFinderGui.setStatusLabel("Er kon geen connectie gemaakt worden met de database, controleer uw " +
                    "internetverbinding");

            return;
        }

        orFinderGui.showPopupError(String.format("Een probleem heeft zich voorgedaan tijdens het ophalen van een " +
                "resultaat uit de database.\nNeem contact op met het systeembeheer en laat aub de volgende error " +
                "zien:\nSQLState: %s\nError code: %s\nMessage: %s", err.getSQLState(), err.getErrorCode(), err.getMessage()));
    }

    /**
     * Checks if the query has been initialised.
     *
     * @return true if the query is initialised, else false.
     */
    private boolean hasQuery() {
        return (query != null);
    }
}
