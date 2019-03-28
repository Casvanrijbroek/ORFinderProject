package localDataManagement;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import orFinderApp.ORF;
import orFinderApp.Query;
import orFinderApp.Result;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class opens the file with the same name as the given header. It will return the data in a Query object.
 * @author Lex Bosch
 * @version 1.0
 */

public class openSave {

    /**
     * String with the files path.
     */
    private String filepath;

    /**
     * Bufferreader to read the file .
     */
    private BufferedReader readFile;

    /**
     * New Query object to be returned.
     */
    private Query newQuery;


    /**
     * Method devining the filepath of the chosen header and calling the methods to build the query and check if the
     * given file exists.
     * @param header given header with the name of the file to be opened.
     * @return Returns Query object with the values as in the file.
     * @throws IOException Throws FileNOtFoundException if the file is not found within the local data.
     */
    public Query openHead(String header) throws IOException {
        this.filepath = System.getProperty("user.dir") + File.separator + "SavedResults" + File.separator + header;
        checkFile(this.filepath);
        readFile = new BufferedReader(new FileReader(this.filepath));
        String Line;
        StringBuilder SB = new StringBuilder();
        try {
            while ((Line = readFile.readLine()) != null) {
                SB.append(Line + "\n");
            }
        } catch (IOException ioe) {
            throw new IOException("No acces to the given file. Please contact your network administrator.");
        }
        return fillQuery(SB.toString());
    }

    /**
     * Checks to see if the given file is found within the local data.
     * @param fileAvalible File path of the given file.
     * @throws FileNotFoundException Throws when the given file is not found within the local data.
     */
    public void checkFile(String fileAvalible) throws FileNotFoundException {
        File newFile = new File(fileAvalible);
        if (!(newFile.exists())) {
            //todo betere shit schrijven
            throw new FileNotFoundException("Header niet gevonden in de lokale bestanden.");
        }
    }

    /**
     * Creates Query to be filled with ORFs.
     * @param fileString String containing all the information of the file.
     * @return Filled Query object to be returned.
     */
    public Query fillQuery(String fileString) {
        String[] resArray = fileString.split("\n\n");
        String[] queryInfo = resArray[0].split(";");
        newQuery = new Query(queryInfo[0], queryInfo[1]);
        newQuery.setOrfList(createORFList(resArray));
        return newQuery;
    }

    /**
     * Creates ArrayList filled with ORF objects to be returned.
     * @param resultList List containing Strings which contain all the information of the ORFs and Results.
     * @return Arraylist with filled ORF objects.
     */
    private ArrayList<ORF> createORFList(String[] resultList) {
        ArrayList<String> newResults = new ArrayList<>(Arrays.asList(resultList));
        ArrayList<ORF> newORFList = new ArrayList<>();
        for (String orfString : newResults.subList(1, newResults.size())) {
            String[] tempwrite = orfString.split("\n");
            String[] werkplz = tempwrite[0].split("\t");
            ORF newOrf = new ORF(Integer.parseInt(werkplz[0]), Integer.parseInt(werkplz[1]));
            newOrf.setResultList(createResultList(tempwrite));
            newORFList.add(newOrf);
        }
        return newORFList;
    }

    /**
     * Creates Arraylist containing filled Result objects.
     * @param resultList List containing all the Strings containing data of the Results.
     * @return Arraylist of filled Result objects.
     */
    private ArrayList<Result> createResultList(String[] resultList) {
        ArrayList<String> listWithResults = new ArrayList<>(Arrays.asList(resultList));
        ArrayList<Result> newResultList = new ArrayList<>();
        for (String ResultLine : listWithResults.subList(1, listWithResults.size())) {
            String[] resultListArray = ResultLine.split("\t");
            Result newResult = new Result();
            newResult.setAccession(resultListArray[0]);
            newResult.setDescription(resultListArray[1]);
            newResult.setIdentity(Integer.parseInt(resultListArray[2]));
            newResult.setpValue(Float.parseFloat(resultListArray[3]));
            newResult.setQueryCover(Integer.parseInt(resultListArray[4]));
            newResult.setScore(Integer.parseInt(resultListArray[5]));
            newResult.setStartPosition(Integer.parseInt(resultListArray[6]));
            newResult.setStopPosition(Integer.parseInt(resultListArray[7]));
            newResultList.add(newResult);
        }
        return newResultList;
    }


}





