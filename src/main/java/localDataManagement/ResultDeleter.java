package localDataManagement;

import java.io.File;

/**
 * This class deletes the entry of the file with the selected header.
 *
 * @author Lex Bosch
 * @version 1.0
 * 29-03-2019
 */

public class ResultDeleter {

    /**
     * This method creates the filepath for the file to be deleted and calls the deleteFile method.
     *
     * @param header header of the result to be deleted
     * @return true if the file is deleted, else false
     */
    public boolean deleteLocalSave(String header) {
        String filepath = System.getProperty("user.dir") + File.separator + "SavedResults" + File.separator +
                header.replaceAll(" ", "_").replaceAll(">", "");

        return deleteFile(filepath);
    }

    /**
     * This method deletes the file.
     *
     * @param fileAvailable Path of the file to be deleted
     * @return true if the file is deleted, else false
     */
    private boolean deleteFile(String fileAvailable) {
        File newFile = new File(fileAvailable);

        return newFile.delete();
    }
}
