package localDataManagement;

import orFinderApp.ORF;
import orFinderApp.Query;
import orFinderApp.Result;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class openSave {
    private String filepath;
    private BufferedReader readFile;
    private Query newQuery;

    public Query openHead(String header) throws FileNotFoundException {
        this.filepath = System.getProperty("user.dir") + File.separator + "SavedResults" + File.separator + header;
        checkFile(this.filepath);
        readFile = new BufferedReader(new FileReader(this.filepath));
        String Line;
        StringBuilder SB = new StringBuilder();
        try {
        while ((Line = readFile.readLine()) != null) {
SB.append(Line);
        }
        return fillQuery(SB.toString());
    }catch (IOException ioe){
            System.out.println(ioe);
        }
    }

    public void checkFile(String fileAvalible) throws FileNotFoundException{
        File newFile = new File(fileAvalible);
        if (!(newFile.exists())){
            //todo betere shit schrijven
            throw new FileNotFoundException("Header niet gevonden in de lokale bestanden.");
        }
    }

    public Query fillQuery(String fileString){
        String[] resArray = fileString.split("\n\n");
        String[] queryInfo = resArray[0].split(";");
        newQuery = new Query(queryInfo[0], queryInfo[1]);
        newQuery.setOrfList(createORFList(resArray));
    }

    private ArrayList<ORF> createORFList(String[] resultList){
        ArrayList<String> newResults =  new ArrayList<>(Arrays.asList(resultList));
        for (String orfString : newResults.subList(1, newResults.size())){
            String[] tempwrite = orfString.split("\n");
            String[] werkplz = tempwrite[0].split(";");
            ORF newOrf = new ORF(Integer.parseInt(werkplz[0]), Integer.parseInt(werkplz[1]));
            newOrf.setResultList(createResultList(werkplz));
        }
    }

    private ArrayList<Result> createResultList(String[] resultList){
        ArrayList<String> listWithResults = new ArrayList<>(Arrays.asList(resultList));
        for (String ResultLine : listWithResults.subList(1, listWithResults.size())){
            String[] resultListArray = ResultLine.split(";");


        }
    }


}





