package localDataManagement;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


/**
 * This class deletes the entry of the file with the selected header. It will return a filenotfound exception if
 * the given header is not in the saved results.
 *
 * @author Lex Bosch
 * @version 1.0
 */

public class DeleteLocalSave {

    /**
     * This method creates the filepath for the file to be deleted and calls the checkfile method.
     * @param header Header of the result to be deleted.
     * @throws FileNotFoundException Throws if the file is not found within the saved results.
     */
    public void DeleteLocalSave(String header) throws IOException {
        String filepath = System.getProperty("user.dir") + File.separator + "SavedResults" + File.separator + header;
        checkFile(filepath);
    }

    /**
     * This method checks for the chosen file to be present. If the file is missing, it will return a filenotfound-
     * exception. If the file is found, it will be deleted.
     * @param fileAvalible Path of the file to be deleted.
     * @throws FileNotFoundException hrows if the file is not found within the saved results.
     */
    private void checkFile(String fileAvalible) throws IOException {
        File newFile = new File(fileAvalible);
        if (!(newFile.exists())) {
            //todo betere shit schrijven
            throw new FileNotFoundException("Header niet gevonden in de lokale bestanden.");
        } else {
            if(newFile.delete()){
                throw new IOException("Er is geen toegang van de aplicatie naar dit bestand. Neem contact op " +
                        "met de beheerder van dit apperaat");
            }
        }
    }
}
