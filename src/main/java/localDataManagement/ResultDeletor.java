package localDataManagement;


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

public class ResultDeletor {

    /**
     * This method creates the filepath for the file to be deleted and calls the checkfile method.
     * @param header Header of the result to be deleted.
     * @throws FileNotFoundException Throws if the file is not found within the saved results.
     */
    public void DeleteLocalSave(String header) throws IOException {
        String filepath = System.getProperty("user.dir") + File.separator + "SavedResults" + File.separator +
                header.replaceAll(" ", "_").replaceAll(">", "");
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
            newFile.delete();
    }
}
