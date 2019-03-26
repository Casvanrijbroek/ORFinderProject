package blastConnetor;

import orFinderApp.ORF;
import orFinderApp.Query;
import orFinderApp.Result;

import java.util.ArrayList;

public class proteinBlast {

    /**
     * Query impot of the Blast
     */
    private Query occQuery;

    /**
     * Protein sequence of the Blast
     */
    private String queryString;

    /**
     * List of blast results
     * May not be used in further versions
     */
    private ArrayList<blastThread> blastList;

    /**
     * Constructor of the Blast module
     *
     * @param query input query to be BLASTed
     */
    public int blast(Query query) throws NoBlastConnectionException, InterruptedException  {
        this.occQuery = query;
        this.queryString = this.occQuery.getSequence();
        this.proteinBlast();
        return checkResults(query);
        //todo Error als geen ORFS
    }


    private int checkResults(Query query){
        ArrayList<ORF> ORFList = query.getOrfList();
        int amResults = 0;
        for(ORF orfs : ORFList){
            amResults += orfs.getResultList().size();
        }
        return amResults;
    }


    /**
     * Creates BLAST thread. The ORF of the query given at the contructor are blasted.
     * According  to the Blastdoc developer info, only one Blast request may be send once every 10 seccconds.
     * To make sure the user of the application will not be banned from the Blast services, this delay is set on
     * 10.5 seconds. See
     * https://blast.ncbi.nlm.nih.gov/Blast.cgi?CMD=Web&PAGE_TYPE=BlastDocs&DOC_TYPE=DeveloperInfo
     * for more info.
     * DO NOT REMOVE DELAY
     */
    private void proteinBlast() throws NoBlastConnectionException, InterruptedException {
        blastList = new ArrayList<blastThread>();
        for (ORF occOrf : occQuery.getOrfList()) {
            blastList.add(new blastThread(queryString.substring(occOrf.getStartPosition(), occOrf.getStopPosition()), occOrf, occQuery));
            blastList.get(blastList.size() - 1).start();
            Thread.sleep(10500);
        }
        System.out.println("Alle Threads zijn bezig");
        while ( !(blastThread.getRunningThreads() == 0)){
            Thread.sleep(2000);
        }
    }

}


