package ORFinderGUI;

import java.io.*; import java.util.*;



import javax.swing.*;
import java.awt.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.regex.Pattern;

public class ORFinderGui extends Component {
    private JPanel Gui;
    private JTextField filepathTextField;
    private JButton browseButton;
    private JButton berekenButton;
    private JComboBox databaseComboBox;
    private JLabel bestandLabel;

    private JFileChooser fileChooser;
    private static String imagePad;
    private static String seperator = "/";

    HashMap<String, String> FastaSequences = new HashMap<>();


    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setContentPane(new ORFinderGui().Gui);
        getImagePath();
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage(imagePad + "icon.jpg"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public static void getImagePath() {
        imagePad = System.getProperty("java.class.path") + seperator;
        imagePad = imagePad.substring(imagePad.indexOf(':') + 1);

    }

    public ORFinderGui() {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (UnsupportedLookAndFeelException e) {
            error(e.getMessage());
        } catch (ClassNotFoundException e) {
            error(e.getMessage());
        } catch (InstantiationException e) {
            error(e.getMessage());
        } catch (IllegalAccessException e) {
            error(e.getMessage());
        }

        browseButton.addActionListener(evt -> browse());
        berekenButton.addActionListener(evt -> {
            try {
                openFile();
            } catch (emptyException e) {
                error(e.getMessage());
            }
        });


    }


    private void browse() {
        File selectedFile;
        int reply;


        fileChooser = new JFileChooser();
        reply = fileChooser.showOpenDialog(this);
        if (reply == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            filepathTextField.setText(selectedFile.getAbsolutePath());
        }
    }

    private void openFile() throws emptyException {
        String Filepath = filepathTextField.getText();
        if (Filepath.isEmpty()) {
            throw new emptyException("Bestandspad bestaat niet, kies alstublieft een bestand via de 'bladeren' knop");
        } else {
            try {
                readFile(Filepath);
            } catch (wrongfileException e) {
                error(e.getMessage());
            }
        }

    }

    private void readFile(String path) throws wrongfileException {
        bestandLabel.setText("");
        String[] paths = path.split(Pattern.quote(File.separator));
        String filename=paths[paths.length-1];
        bestandLabel.setText(filename);
        boolean first = true;


        if (!path.endsWith(".fasta")) {
            throw new wrongfileException("Bestand:"+filename+"is geen fasta bestand.");

        } else {
            try {
                //setup file input

                BufferedInputStream is =
                        new BufferedInputStream(new FileInputStream(path));

                //get the appropriate Alphabet
                Alphabet alpha = AlphabetManager.alphabetForName(args[1]);

                //get a SequenceDB of all sequences in the file
                SequenceDB db = SeqIOTools.readFasta(is, alpha);
            }
            catch (BioException ex) {
                //not in fasta format or wrong alphabet
                ex.printStackTrace();
            }catch (NoSuchElementException ex) {
                //no fasta sequences in the file
                ex.printStackTrace();
            }catch (FileNotFoundException ex) {
                //problem reading file
                ex.printStackTrace();
            }


        }
    }



    private void error(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

}

class emptyException extends Exception {
    public emptyException(String msg) {
        super(msg);
    }
}

class wrongfileException extends Exception {
    public wrongfileException(String msg) {
        super(msg);
    }
}



