package localDataManagement;

import orFinderApp.ORF;
import orFinderApp.Query;
import orFinderApp.Result;

import javax.swing.*;
import java.io.*;

/**
 * This class saves everything within a query object to a CSV file to be opened later.
 *
 * @author Lex Bosch
 * @version 1.0
 * 29-03-2019
 */

public class ResultSaver {

    /**
     * Writes the file containing the Query object.
     *
     * @param query an Object containing the information to be stored
     * @throws FileNotFoundException no access to create folder
     * @throws NullPointerException  no ORFs within the query
     */
    public void saveResults(Query query) throws IOException, NullPointerException {
        FileWriter filewriter;
        String filePath;

        filePath = "SavedResults";
        createResultsFolder(filePath);
        chooseOverwriteFile(filePath + File.separator + query.getHeader()
                .replaceAll(" ", "_").replaceAll(">", ""));

        try {
            if(query.getOrfList().size() == 0){
                throw new NullPointerException();
            }

            filewriter = new FileWriter(filePath + File.separator + query.getHeader()
                    .replaceAll(" ", "_").replaceAll(">", ""));
            filewriter.append(query.getHeader()).append(";").append(query.getSequence()).append("\n");

            for (ORF occORF : query.getOrfList()) {
                filewriter.append(writeORF(occORF));
            }

            filewriter.close();
        } catch (NullPointerException err) {
            throw new NullPointerException("Geen ORFs gevonden in het opgegeven query.");
        }
    }

    /**
     * Creates a folder if needed and doesn't exist yet.
     *
     * @param addDir the folder that needs to be located
     */
    private void createResultsFolder(String addDir) {
        String workingDir = System.getProperty("user.dir");
        File resultDir = new File(workingDir + File.separator + addDir);
        if (!resultDir.exists()) {
            resultDir.mkdirs();
        }
    }

    /**
     * Creates a String containing the information of the ORF given.
     *
     * @param orf the ORF object to have information extracted from
     * @return a String containing the information of the given ORF
     */
    private String writeORF(ORF orf) {
        StringBuilder orfBuilder;

        try {
            orfBuilder = new StringBuilder();
            orfBuilder.append("\n").append(orf.getStartPosition())
                    .append("\t").append(orf.getStopPosition()).append("\n");

            for (Result occResult : orf.getResultList()) {
                orfBuilder.append(writeResults(occResult)).append("\n");
            }

            return orfBuilder.toString();
        } catch (NullPointerException NPE) {
            return "\n";
        }
    }

    /**
     * Returns a String containing the information stored in the result object in a tdf.
     *
     * @param result a Result object to have to data extracted from
     * @return a String containing the information of the Result object
     */
    private String writeResults(Result result) {
        return result.getAccession() + "\t" +
                result.getDescription() + "\t" +
                result.getIdentity() + "\t" +
                result.getpValue() + "\t" +
                result.getQueryCover() + "\t" +
                result.getScore() + "\t" +
                result.getStartPosition() + "\t" +
                result.getStopPosition();
    }

    /**
     * Calls optionPane to overwrite the existing file with the same header.
     *
     * @param filePath the File path of file to to overwrite
     * @exception NullPointerException when the user decides not to overwrite the file
     */
    private void chooseOverwriteFile(String filePath) throws NullPointerException {
        File existFile = new File(filePath);
        Object[] options = {"Ja, Graag",
                "Nee, bedankt"};

        if (existFile.exists()) {
            int n = JOptionPane.showOptionDialog(null,
                    "Resultaten met deze header bestaan al.\n Wilt u deze overschrijven?\n" +
                            " Oude resultaten zullen permanent verloren raken",
                    "Waarschuwing",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[1]);

            if (n == 1) {
                throw new NullPointerException("Bestand is niet opgeslagen");
            }
        }
    }
}
