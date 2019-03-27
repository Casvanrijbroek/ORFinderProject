package ORFinderGUI;


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
 * @version 1.1
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

    /**
     * This constructor sets up the class is several ways:
     * 1. It sets the look of the JFileChooser in the windows fashion.
     * 2. It adds actionListeners to the buttons.
     * 3. It sets the ORFinderApp belonging to the GUI.
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
        browseButton.addActionListener(evt -> browse());
        databaseButton.addActionListener(evt -> executeDatabase());
        localButton.addActionListener(evt -> executeLocal());
        loadFileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    openFile();
                } catch (EmptyException | WrongfileException | IOException e) {
                    showPopupError(e.getMessage());
                }
            }
        });

        setResultTableModel();

        setORFinderApp(orFinderApp);
    }

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
     * This method references local data handling methods when the user pressed the handle local button.
     * The method called is determined by the radiobutton selected by the user. Fetch result is the default option.
     */
    private void executeLocal() {
        if (fetchResultButton.isSelected()) {

        } else if (saveResultButton.isSelected()) {

        } else if (deleteResultButton.isSelected()) {

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
            //headerField.setText(query.getHeader());
            //sequenceArea.setText(query.getSequence());
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

    private void browse() {
        File selectedFile;

        int value =fileChooser.showOpenDialog(null);

        if (value == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            filePathField.setText(selectedFile.getAbsolutePath());
        }
    }

    private void openFile() throws EmptyException, WrongfileException, IOException {
        String Filepath = filePathField.getText();
        if (Filepath.isEmpty()) {
            throw new EmptyException("Bestandspad bestaat niet, kies alstublieft een bestand via de 'bladeren' knop");
        } else {
            readFile(Filepath);

        }

    }

    private void readFile(String path) throws WrongfileException, IOException {
        BufferedReader reader;
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
                throw new WrongfileException("Bestand: " + fileName + " is een file met meerdere sequenties, gebruik een fasta file met één sequentie");
            }
            sequenceBuilder.append(line);
        }
        reader.close();

        sequence = sequenceBuilder.toString().toUpperCase();
        if (sequence.matches("[ATGCN]+")) {

            orFinderApp.setQuery(new Query(header, sequence));
            headerField.setText(header);
            setSequenceArea(sequence);

        } else
            throw new WrongfileException("Bestand: " + fileName + " is een bestand dat niet bestaat uit DNA, gebruik alstublieft een ander bestand");

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

    private void visualiseQuery(Query query) {
        headerField.setText(query.getHeader());
        setSequenceArea(query.getSequence());
        orfComboBox.removeAllItems();

        for (ORF orf : query.getOrfList()) {
            orfComboBox.addItem(orf);
        }
    }

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
