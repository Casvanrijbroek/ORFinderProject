package ORFinderGUI;


import OrfFinderFinder.OrfFinderFinder;
import orFinderApp.ORF;
import orFinderApp.ORFinderApp;
import orFinderApp.Query;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.util.regex.Pattern;

public class ORFinderGui extends Component {
    public JPanel getGui() {
        return gui;
    }

    public JPanel gui;
    private JTextField filepathTextField;
    private JButton browseButton;
    private JButton berekenButton;
    private JComboBox databaseComboBox;
    private JLabel bestandLabel;
    private JTable restultTable;
    private JButton exportButton;

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


    public ORFinderGui() throws IllegalAccessException,
            UnsupportedLookAndFeelException, InstantiationException, ClassNotFoundException {
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");

        LoadDataBase();

        browseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browse();
            }
        });
        berekenButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    openFile();
                } catch (EmptyException | WrongfileException | IOException e) {
                    error(e.getMessage());
                }
            }
        });

    }

    private void LoadDataBase() {

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
        bestandLabel.setText("");
        String[] paths = path.split(Pattern.quote(File.separator));
        String filename = paths[paths.length - 1];
        bestandLabel.setText(filename);


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

            Query Sequence = new Query(Header, Seq);
            OrfFinderFinder FindORF = new OrfFinderFinder();
            FindORF.HanldeQueary(Sequence);


        } else
            throw new WrongfileException("Bestand: " + filename + " is een bestand dat niet bestaat uit DNA, gebruik alstublieft een ander bestand");

    }

    private void error(String message) {
        JOptionPane.showMessageDialog(null, message);
    }
}








