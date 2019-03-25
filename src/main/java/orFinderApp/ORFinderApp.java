package orFinderApp;

import ORFinderGUI.ORFinderGui;
import databaseConnector.ConnectionException;
import databaseConnector.Connector;
import databaseConnector.SearchOption;

import javax.swing.*;
import java.awt.*;
import java.io.File;
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
 * @version 0.1
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
     * The static main method that sets up the application. This is where the GUI is visualised.
     *
     * @param args no args are expected to be given since this application is not designed for command line usage
     */
    public static void main(String[] args) {
        try {
            ORFinderGui gui = new ORFinderGui();
            JFrame frame = new JFrame();
            frame.setContentPane(gui.getGui());
            frame.setIconImage(Toolkit.getDefaultToolkit().getImage(
                    Paths.get("src", "main", "resources", "icon.jpg").toString()));
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    /**
     * In this constructor instance variables will be set as needed from the different packages. This includes a
     * Connector object to communicate with the database, ...
     */
    ORFinderApp() {
        databaseConnector = new Connector();
    }

    /**
     * This method is used to obtain a query from the database based on it's header. It utilises the getQuery method
     * of the Connector to retrieve the query and handles exceptions that can be thrown by these methods.
     *
     * @param searchOption a SearchOption enum indicating the attribute type that is to be searched on
     * @param value a String with the attribute that is to be searched on
     */
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

    /**
     * This methods is used to obtain a query from the database based on it's identifier. It utilises the getQuery
     * method of the Connector to retrieve the query and handles exceptions that can be thrown by these methods.
     * method
     * @param searchOption a SearchOption enum indicating the attribute type that is to be searched on
     * @param value a String with the attribute that is to be searched on
     */
    public void getQuery(SearchOption searchOption, int value) {
        getQuery(searchOption, String.valueOf(value));
    }
}
