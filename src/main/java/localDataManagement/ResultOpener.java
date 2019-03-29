package localDataManagement;

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
 * 29-03-2019
 */

public class ResultOpener {
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
     * Method defining the filepath of the chosen header and calling the methods to build the query and check if the
     * given file exists.
     *
     * @param header given header with the name of the file to be opened
     * @return a Query object with the values as in the file
     * @throws IOException throws FileNOtFoundException if the file is not found within the local data
     */
    public Query openHead(String header) throws IOException {
        StringBuilder queryBuilder;
        String Line;

        filepath = System.getProperty("user.dir") + File.separator + "SavedResults" + File.separator + header
                .replaceAll(" ", "_").replaceAll(">", "");
        readFile = new BufferedReader(new FileReader(filepath));
        queryBuilder = new StringBuilder();

        while ((Line = readFile.readLine()) != null) {
            queryBuilder.append(Line).append("\n");
        }

        return fillQuery(queryBuilder.toString());
    }

    /**
     * Creates a Query object to be filled with ORFs.
     *
     * @param fileString a String containing all the information of the file
     * @return the Query object to be returned
     */
    private Query fillQuery(String fileString) {
        String[] resultArray;
        String[] queryInfo;

        resultArray = fileString.split("\n\n");
        queryInfo = resultArray[0].split(";");
        newQuery = new Query(queryInfo[0], queryInfo[1]);
        newQuery.setOrfList(createORFList(resultArray));

        return newQuery;
    }

    /**
     * Creates an ArrayList filled with ORF objects to be returned.
     *
     * @param resultList list containing Strings which contain all the information of the ORFs and Results
     * @return an ArrayList with filled ORF objects
     */
    private ArrayList<ORF> createORFList(String[] resultList) {
        ArrayList<String> newResults;
        ArrayList<ORF> newORFList;

        newResults = new ArrayList<>(Arrays.asList(resultList));
        newORFList = new ArrayList<>();

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
     * Creates an ArrayList containing filled Result objects.
     *
     * @param resultList a List containing all the Strings containing data of the Results
     * @return an ArrayList of filled Result objects
     */
    private ArrayList<Result> createResultList(String[] resultList) {
        ArrayList<String> listWithResults = new ArrayList<>(Arrays.asList(resultList));
        ArrayList<Result> newResultList = new ArrayList<>();
        String[] resultListArray;

        for (String ResultLine : listWithResults.subList(1, listWithResults.size())) {
            resultListArray = ResultLine.split("\t");
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
