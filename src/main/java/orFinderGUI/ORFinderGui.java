package orFinderGUI;

import databaseConnector.SearchOption;
import orFinderApp.ORF;
import orFinderApp.ORFinderApp;
import orFinderApp.Query;
import orFinderApp.Result;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * This class creates a GUI for the ORFinder. This is where the user can input Fasta files, find ORF's an blast the
 * results. The user can also save these results either locally or in the remote database.
 *
 * @author Elco van Rijswijk, Cas van Rijbroek
 * @version 1.5
 * 27-03-2019
 */
public class ORFinderGui extends Component {
    /**
     * A filter for the JFileChooser that only accepts Fasta files.
     */
    final private static FileNameExtensionFilter FASTA_FILTER = new FileNameExtensionFilter(
            "Fasta files", "fasta", "fa");
    /**
     * The orFinderApp belonging to the GUI.
     */
    private ORFinderApp orFinderApp;
    /**
     * The JFileChooser that the user can use to load in a Fasta file.
     */
    private JFileChooser fileChooser;
    /*
    Below the variables that represent the swing components of the GUI are defined. Some are not used in this class,
    because the form file already assigns values and to certain components.
     */
    public JPanel gui;
    private JTextField filePathField;
    private JButton browseButton;
    private JButton loadFileButton;
    private JComboBox<ORF> orfComboBox;
    private JLabel statusValueLabel;
    private JTable resultTable;
    private JLabel statusLabel;
    private JRadioButton fetchResultButton;
    private JRadioButton saveResultButton;
    private JRadioButton deleteResultButton;
    private JPanel databasePanel;
    private JButton databaseButton;
    private JButton localButton;
    private JTextField headerField;
    private JLabel headerLabel;
    private JTextArea sequenceArea;
    private JLabel sequenceLabel;
    private JScrollPane sequenceScroll;
    private JLabel filePathLabel;
    private JLabel loadFileLabel;
    private JScrollPane resultScroll;
    private JLabel resultLabel;
    private JButton blastButton;
    private JButton findORFButton;

    /**
     * This constructor sets up the class is several ways:
     * 1. It sets the look of the JFileChooser in the windows fashion.
     * 2. It adds actionListeners to the buttons.
     * 3. It sets up the table model for the resultTable.
     * 4. It sets the ORFinderApp belonging to the GUI.
     *
     * @param orFinderApp the ORFinderApp belonging to the GUI
     */
    public ORFinderGui(ORFinderApp orFinderApp) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");

            fileChooser = new JFileChooser();
            fileChooser.setFileFilter(FASTA_FILTER);
            fileChooser.setAcceptAllFileFilterUsed(false);
        } catch (IllegalAccessException | UnsupportedLookAndFeelException | InstantiationException |
                ClassNotFoundException err) {
            showPopupError(String.format("Een fatale showPopupError is voorgekomen, neem contact op met systeembeheer " +
                    "en laat het volgende zien:\n%s", err.getMessage()));
        }

        orfComboBox.addActionListener(evt -> showResults());
        findORFButton.addActionListener(evt -> findORFS());
        blastButton.addActionListener(evt -> blast());
        browseButton.addActionListener(evt -> browse());
        databaseButton.addActionListener(evt -> executeDatabase());
        localButton.addActionListener(evt -> executeLocal());
        loadFileButton.addActionListener(evt -> {
            try {
                openFile();
            } catch (EmptyException | WrongFileException | IOException e) {
                showPopupError(e.getMessage());
            }
        });

        setResultTableModel();

        setORFinderApp(orFinderApp);
    }

    /**
     * Calls the ORFinderApp to search for ORFs in the given sequence.
     */
    private void findORFS() {
        if (hasNoHeader()) {
            return;
        } else if (sequenceArea.getText().isEmpty()) {
            setStatusLabel("Voer een een sequentie in");

            return;
        }

        orFinderApp.setQuery(new Query(headerField.getText(), sequenceArea.getText()));
        orFinderApp.findORFS();
        visualiseQuery(orFinderApp.getQuery());
    }

    /**
     * Calls the ORFinderApp to blast the found ORFs.
     */
    private void blast() {
        if (orFinderApp.getQuery() == null || orFinderApp.getQuery().getOrfList() == null ||
                orFinderApp.getQuery().getOrfList().size() <= 0) {
            setStatusLabel("Er zijn (nog) geen ORFs om te blasten");
        } else if (orFinderApp.proteinBlastQuery()) {
            visualiseQuery(orFinderApp.getQuery());
        }
    }

    /**
     * This method references local data handling methods when the user presses the handle local button.
     * The method called is determined by the radiobutton selected by the user. Fetch result is the default option.
     */
    private void executeLocal() {
        if (fetchResultButton.isSelected()) {
            openLocal();
        } else if (saveResultButton.isSelected()) {
            saveLocal();
        } else if (deleteResultButton.isSelected()) {
            deleteLocal();
        }
    }

    /**
     * Calls the ORFinderApp to save the results locally.
     */
    private void saveLocal() {
        if (orFinderApp.getQuery() == null) {
            setStatusLabel("Geef aub eerst een resultaat op");

            return;
        }
        orFinderApp.getQuery().setHeader(headerField.getText());

        if (orFinderApp.saveQueryLocal(orFinderApp.getQuery())) {
            setStatusLabel("Het resultaat is opgeslagen");
        } else {
            setStatusLabel("Het resultaat is niet opgeslagen");
        }
    }

    /**
     * Calls the ORFinderApp to delete results locally.
     */
    private void deleteLocal() {
        if (hasNoHeader()) {
            showPopupError("Voer eerst een header in");
            setStatusLabel("Resultaat niet verwijderd");

            return;
        }
        if (orFinderApp.deleteQueryLocal(headerField.getText())) {
            setStatusLabel("Resultaat verwijderd");
        } else {
            setStatusLabel("Resultaat niet verwijderd");
        }
    }

    /**
     * Calls the ORFinderApp to open local results.
     */
    private void openLocal() {
        if (hasNoHeader()) {
            showPopupError("Voer eerst een header in");
            setStatusLabel("Resultaat niet ingeladen");

            return;
        }
        if (orFinderApp.openQueryLocal(headerField.getText())) {
            visualiseQuery(orFinderApp.getQuery());
            setStatusLabel("Resultaat ingeladen");
        } else {
            setStatusLabel("Resultaat niet ingeladen");
        }
    }

    /**
     * This method references database handling methods when the user pressed the handle local button.
     * The method called is determined by the radiobutton selected by the user. Fetch result is the default option.
     */
    private void executeDatabase() {
        if (fetchResultButton.isSelected()) {
            loadFromDatabase();
        } else if (saveResultButton.isSelected()) {
            insertIntoDatabase();
        } else if (deleteResultButton.isSelected()) {
            deleteFromDatabase();
        }
    }

    /**
     * This method calls the ORFinderApp to fetch a query from the database based on header.
     */
    private void loadFromDatabase() {
        Query query;

        if (hasNoHeader()) {
            return;
        }

        if (orFinderApp.getQueryDatabase(SearchOption.NAME, headerField.getText())) {
            query = orFinderApp.getQuery();

            visualiseQuery(query);
            statusValueLabel.setText("Ophalen resultaat uit database succesvol");
        }
    }

    /**
     * This method calls the ORFinderApp to insert the current query displayed in the GUI into the database.
     */
    private void insertIntoDatabase() {
        Query query;

        if (hasNoHeader()) {
            return;
        }

        query = orFinderApp.getQuery();

        if (query == null) {
            setStatusLabel("Voer op zijn minst een header en een sequentie in om een resultaat op te slaan");

            return;
        }

        query.setHeader(headerField.getText());

        if (orFinderApp.insertQueryDatabase(query)) {
            statusValueLabel.setText("Toevoegen resultaat in database succesvol");
        }
    }

    /**
     * This method calls the ORFinderApp to delete the query related to the current header displayed in the GUI.
     */
    private void deleteFromDatabase() {
        if (hasNoHeader()) {
            return;
        }

        if (orFinderApp.deleteQueryDatabase(SearchOption.NAME, headerField.getText())) {
            statusValueLabel.setText("Verwijderen resultaat uit database succesvol");
        }
    }

    /**
     * Allows the user to select a file using a JFileChooser.
     */
    private void browse() {
        File selectedFile;

        int value =fileChooser.showOpenDialog(null);

        if (value == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            filePathField.setText(selectedFile.getAbsolutePath());
        }
    }

    /**
     * Opens the file related to the given file path.
     *
     * @throws EmptyException if the file doesn't exist
     * @throws WrongFileException if the file contains more than 1 sequence
     * @throws IOException if something goes wrong with the handling of the file
     */
    private void openFile() throws EmptyException, WrongFileException, IOException {
        String Filepath = filePathField.getText();

        if (Filepath.isEmpty()) {
            throw new EmptyException("Bestandspad bestaat niet, kies alstublieft een bestand via de 'bladeren' knop");
        } else {
            readFile(Filepath);
        }
    }

    /**
     * Reads a FASTA file and extracts information from it.
     *
     * @param path the path of the file that is to be read
     * @throws WrongFileException if the file contains more than 1 sequence
     * @throws IOException if something goes wrong with the handling of the file
     */
    private void readFile(String path) throws WrongFileException, IOException {
        BufferedReader reader;
        Query query;
        String line;
        String header;
        String sequence;
        String fileName;
        String[] paths;
        StringBuilder sequenceBuilder;

        paths = path.split(Pattern.quote(File.separator));
        fileName = paths[paths.length - 1];
        statusValueLabel.setText(String.format("%s ingeladen", fileName));

        reader = new BufferedReader(new FileReader(path));
        header = reader.readLine();
        sequenceBuilder = new StringBuilder();

        while ((line = reader.readLine()) != null) {
            if (line.startsWith(">")) {
                throw new WrongFileException("Bestand: " + fileName + " is een file met meerdere sequenties, gebruik een fasta file met één sequentie");
            }
            sequenceBuilder.append(line);
        }
        reader.close();

        sequence = sequenceBuilder.toString().toUpperCase();
        if (sequence.matches("[ATGCN]+")) {

            query = new Query(header, sequence);
            orFinderApp.setQuery(query);
            visualiseQuery(query);

        } else
            throw new WrongFileException("Bestand: " + fileName + " is een bestand dat niet bestaat uit DNA, gebruik alstublieft een ander bestand");

    }

    /**
     * Visualises the BLAST results in the resultTable.
     */
    private void showResults() {
        DefaultTableModel tableModel;
        ORF orf;

        tableModel = (DefaultTableModel) resultTable.getModel();
        orf = (ORF) orfComboBox.getSelectedItem();

        tableModel.setRowCount(0);

        if (orf != null) {
            for (Result result : orf.getResultList()) {
                tableModel.addRow(new Object[]{result.getAccession(), result.getDescription(), result.getScore(),
                        result.getQueryCover(), result.getpValue(), result.getIdentity(), result.getStartPosition(),
                        result.getStopPosition()});
            }
        }
    }

    /**
     * Checks if there is currently no header displayed in the GUI.
     *
     * @return true if there is no header displayed, else false
     */
    private boolean hasNoHeader() {
        if (headerField.getText().isEmpty()) {
            setStatusLabel("Geef aub eerst een header op");
            return true;
        } else {
            return false;
        }
    }

    /**
     * Gets gui.
     *
     * @return value of gui
     */
    public JPanel getGui() {
        return gui;
    }

    /**
     * Sets orFinderApp.
     *
     * @param orFinderApp sets this value into the class variable
     */
    private void setORFinderApp(ORFinderApp orFinderApp) {
        this.orFinderApp = orFinderApp;
    }

    /**
     * Sets sequenceArea.
     *
     * @param sequence sets this value into the class variable
     */
    private void setSequenceArea(String sequence) {
        sequenceArea.setText(sequence.replaceAll("(.{100})", "$1\n"));
    }

    /**
     * Sets a message into the statusValueLabel.
     *
     * @param message sets this value into the class variable
     */
    public void setStatusLabel(String message) {
        statusValueLabel.setText(message);
    }

    /**
     * Shows a popup to the user displaying an (error) message.
     *
     * @param message the message that is to be displayed
     */
    public void showPopupError(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    /**
     * Visualises the Query object in the GUI.
     *
     * @param query the Query that is to be visualised
     */
    private void visualiseQuery(Query query) {
        DefaultTableModel tableModel;

        tableModel = (DefaultTableModel) resultTable.getModel();
        tableModel.setRowCount(0);
        headerField.setText(query.getHeader());
        setSequenceArea(query.getSequence());
        orfComboBox.removeAllItems();

        if (query.getOrfList() != null) {
            for (ORF orf : query.getOrfList()) {
                orfComboBox.addItem(orf);
            }
        }
    }

    /**
     * Sets the DefaultTableModel of the resultTable.
     */
    private void setResultTableModel() {
        DefaultTableModel tableModel;
        String[] columnNames;

        tableModel = new DefaultTableModel();
        columnNames = new String[]{"Accessie code", "Beschrijving", "Score", "Query cover", "E waarde", "Identity %",
        "Start positie", "Stop positie"};

        for (String columnName : columnNames) {
            tableModel.addColumn(columnName);
        }

        resultTable.setModel(tableModel);
    }
}
