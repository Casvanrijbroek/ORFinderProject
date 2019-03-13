package ORFinderApp;

import java.util.ArrayList;

public class Query {
    /**
     * ArrayList containing ORFinder.ORFinderApp.ORF resutls
     */
    private ArrayList<ORF> orfList;
    /**
     * Header of the result
     */
    private String header;
    /**
     * sequence of the result
     */
    private String sequence;

    /**
     * constructor of Querry
     *
     * @param header
     * @param sequence
     */
    public Query(String header, String sequence) {
        this.header = header;
        this.sequence = sequence;
    }

    /**
     * clear Arraylist orfList
     */
    protected void clearOrfList() {
        orfList = null;
    }

    /**
     * Gets sequence
     *
     * @return value of sequence
     */
    public String getSequence() {
        return sequence;
    }


    /**
     * Creates a list of sequences of the ORFs
     * @return Arraylist of protein sequences
     */
    public ArrayList<String> getSequenceList(){
        ArrayList<String> sequenceList = new ArrayList<String>();
        for (ORF singleORF: orfList){
            sequenceList.add(this.sequence.substring(singleORF.getStartPosition(), singleORF.getStopPosition()));
        }
        return sequenceList;
    }
}
