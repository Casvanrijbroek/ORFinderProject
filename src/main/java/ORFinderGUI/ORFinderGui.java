package ORFinderGUI;


import databaseConnector.SearchOption;
import orFinderApp.ORFinderApp;
import orFinderApp.Query;
import org.apache.commons.lang.WordUtils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.util.regex.Pattern;

/**
 * @author Elco van Rijswijk, Cas van Rijbroek
 * @version 1.1
 */
public class ORFinderGui extends Component {
    public JPanel getGui() {
        return gui;
    }

    public JPanel gui;
    private JTextField filepathTextField;
    private JButton browseButton;
    private JButton berekenButton;
    private JComboBox databaseComboBox;
    private JLabel statusValueLabel;
    private JTable restultTable;
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
    private ORFinderApp orFinderApp;

    final private static FileNameExtensionFilter docfilter = new FileNameExtensionFilter("Fasta files", "fasta");
    private JFileChooser fileChooser;
    private static String imagePad;
    private static String seperator = "/";


    public static void main(String[] args) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        JFrame frame = new JFrame();
        frame.setContentPane(new ORFinderGui().gui);
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage(imagePad + "icon.jpg"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Sets orFinderApp
     *
     * @param orFinderApp sets this value into the class variable
     */
    public void setORFinderApp(ORFinderApp orFinderApp) {
        this.orFinderApp = orFinderApp;
    }

    public ORFinderGui() {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (IllegalAccessException | UnsupportedLookAndFeelException | InstantiationException |
                ClassNotFoundException err) {
            showPopupError(String.format("Een fatale showPopupError is voorgekomen, neem contact op met systeembeheer " +
                    "en laat het volgende zien:\n%s", err.getMessage()));
        }

        browseButton.addActionListener(evt -> browse());
        databaseButton.addActionListener(evt -> executeDatabase());
        localButton.addActionListener(evt -> executeLocal());

        berekenButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    openFile();
                } catch (EmptyException | WrongfileException | IOException e) {
                    showPopupError(e.getMessage());
                }
            }
        });
    }

    private void executeLocal() {
        if (fetchResultButton.isSelected()) {

        } else if (saveResultButton.isSelected()) {

        } else if (deleteResultButton.isSelected()) {

        }
    }

    private void executeDatabase() {
        if (fetchResultButton.isSelected()) {
            loadFromDatabase();
        } else if (saveResultButton.isSelected()) {
            insertIntoDatabase();
        } else if (deleteResultButton.isSelected()) {
            deleteFromDatabase();
        }
    }

    private void loadFromDatabase() {
        Query query;

        if (!hasHeader()) {
            return;
        }

        if (orFinderApp.getQueryDatabase(SearchOption.NAME, headerField.getText())) {
            query = orFinderApp.getQuery();

            headerField.setText(query.getHeader());
            sequenceArea.setText(query.getSequence().replaceAll("(.{50})", "$1\n"));
            statusValueLabel.setText("Ophalen resultaat uit database succesvol");
        }
    }

    private void insertIntoDatabase() {
        Query query;

        if (!hasHeader()) {
            return;
        }

        query = orFinderApp.getQuery();
        query.setHeader(headerField.getText());

        if (orFinderApp.insertQueryDatabase(query)) {
            statusValueLabel.setText("Toevoegen resultaat in database succesvol");
        }
    }

    private void deleteFromDatabase() {
        if (!hasHeader()) {
            return;
        }

        if (orFinderApp.deleteQueryDatabase(SearchOption.NAME, headerField.getText())) {
            statusValueLabel.setText("Verwijderen resultaat uit database succesvol");
        }
    }

    private boolean hasHeader() {
        if (headerField.getText().isEmpty()) {
            setStatusLabel("Geef aub eerst een header op");
            return false;
        } else {
            return true;
        }
    }

    private void browse() {
        File selectedFile;

        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(docfilter);
        int value = chooser.showOpenDialog(null);


        if (value == JFileChooser.APPROVE_OPTION) {
            selectedFile = chooser.getSelectedFile();
            filepathTextField.setText(selectedFile.getAbsolutePath());
        }
    }

    private void openFile() throws EmptyException, WrongfileException, IOException {
        String Filepath = filepathTextField.getText();
        if (Filepath.isEmpty()) {
            throw new EmptyException("Bestandspad bestaat niet, kies alstublieft een bestand via de 'bladeren' knop");
        } else {
            readFile(Filepath);

        }

    }

    private void readFile(String path) throws WrongfileException, IOException {
        statusValueLabel.setText("");
        String[] paths = path.split(Pattern.quote(File.separator));
        String filename = paths[paths.length - 1];
        statusValueLabel.setText(filename);


        BufferedReader buf = new BufferedReader(new FileReader(path));
        String Header = buf.readLine();
        String line = null;
        String Seq = "";
        while ((line = buf.readLine()) != null) {
            if (line.startsWith(">")) {
                throw new WrongfileException("Bestand: " + filename + " is een file met meerdere sequenties, gebruik een fasta file met één sequentie");
            }
            Seq += line;
        }
        buf.close();

        Seq = Seq.toUpperCase();
        if (Seq.matches("[ATGCN]+")) {

            new Query(Header, Seq);

        } else
            throw new WrongfileException("Bestand: " + filename + " is een bestand dat niet bestaat uit DNA, gebruik alstublieft een ander bestand");

    }

    public void setStatusLabel(String message) {
        statusValueLabel.setText(message);
    }

    public void showPopupError(String message) {
        JOptionPane.showMessageDialog(null, message);
    }
}








