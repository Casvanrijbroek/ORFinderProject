package ORFinderGUI;


import orFinderApp.Query;

import javax.swing.*;
import java.awt.*;
import java.io.*;
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


    public static void main(String[] args) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
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

    public ORFinderGui() throws IllegalAccessException,
            UnsupportedLookAndFeelException, InstantiationException, ClassNotFoundException {
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");

        browseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browse();
            }
        });
        berekenButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    openFile();
                } catch (EmptyException e) {
                    error(e.getMessage());
                } catch (WrongfileException e) {
                    error(e.getMessage());
                } catch (IOException e) {
                    error(e.getMessage());
                }
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


        if (!path.endsWith(".fasta")) {
            throw new WrongfileException("Bestand: " + filename + " is geen fasta bestand.");

        } else {

            BufferedReader buf = new BufferedReader(new FileReader(path));
            String Header = buf.readLine();
            String line = null;
            String Seq = "";
            while ((line = buf.readLine()) != null) {
                Seq += line;
            }
            buf.close();
            Query test = new Query(Header, Seq);

        }
    }

    private void error(String message) {
        JOptionPane.showMessageDialog(null, message);
    }
}








