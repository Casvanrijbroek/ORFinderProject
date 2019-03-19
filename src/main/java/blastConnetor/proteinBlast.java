package blastConnetor;

import orFinderApp.ORF;
import orFinderApp.Query;

import java.util.ArrayList;

public class proteinBlast{

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
     * @param query input query to be BLASTed
     */
    public void proteinBlast(Query query){
        this.occQuery = query;
        this.queryString = this.occQuery.getSequence();
    }


    /**
     * Creates BLAST thread. The ORF of the query given at the contructor are blasted.
     */
    public void blast(){
        blastList = new ArrayList<blastThread>();
        for (ORF occOrf : occQuery.getOrfList()){
            blastList.add(new blastThread(queryString.substring(occOrf.getStartPosition(), occOrf.getStopPosition()), occOrf));
            blastList.get(blastList.size() - 1).start();
        }
    }

}


