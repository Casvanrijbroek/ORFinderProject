package localDataManagement;

import orFinderApp.ORF;
import orFinderApp.Query;
import orFinderApp.Result;

import javax.swing.*;
import java.awt.*;
import java.io.*;

/**
 * This class saves everything within a query object to a CSV file to be opened later.
 *
 * @author Lex Bosch
 * @version 1.0
 */

public class LocalSave {

    /**
     * Writes the file containing the Query object.
     *
     * @param query Object containing the information to be stored.
     * @throws FileNotFoundException No access to create folder.
     * @throws NullPointerException  No ORFs within the query
     */
    public void saveResults(Query query) throws FileNotFoundException, NullPointerException {
        String filePath = createResultsFolder("SavedResults");
        chooseOverwriteFile(filePath + File.separator + query.getHeader());
        FileWriter writeFiles;
        try {
            if(query.getOrfList().size() == 0){
                throw new NullPointerException();
            }
            writeFiles = new FileWriter(filePath + File.separator + query.getHeader());
            writeFiles.append(query.getHeader()).append(";").append(query.getSequence()).append("\n");
            for (ORF occORF : query.getOrfList()) {
                writeFiles.append(writeORF(occORF));
            }
            writeFiles.close();
        } catch (IOException FNFE) {
            throw new FileNotFoundException("Geen bevoegdheid om folders aan te maken. Neem contact op met uw" +
                    " beheerder.");
        } catch (NullPointerException NPE) {
            throw new NullPointerException("Geen ORFs gevonden in het opgegeven query.");
        }
    }

    /**
     * Creates folder if so needed and doesn't exist yet.
     *
     * @param addDir
     * @return
     */
    private String createResultsFolder(String addDir) {
        String workingDir = System.getProperty("user.dir");
        File resultDir = new File(workingDir + File.separator + addDir);
        if (!resultDir.exists()) {
            resultDir.mkdirs();
        }
        return addDir;
    }


    /**
     * Creates String containing the information of the ORF given.
     * @param orf ORF object to have he information extracted from.
     * @return String containing the information of the given ORF.
     */
    private String writeORF(ORF orf) {
        try {
            StringBuilder orfBuilder = new StringBuilder();
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
     * @param result Result object to have to data extracted from.
     * @return Returns a String containing the information of the Result object.
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
     * @param filePath File path of file to to overwrite.
     * @exception NullPointerException Throws when the user decides not to overwrite the file.
     */
    private void chooseOverwriteFile(String filePath) throws NullPointerException {
        File existFile = new File(filePath);
        if (existFile.exists()) {
            Object[] options = {"Ja, Graag",
                    "Nee, bedankt"};
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