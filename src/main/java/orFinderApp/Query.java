package orFinderApp;

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
     * @param header   Header of the sequence
     * @param sequence Protein sequence of the entry
     */
    public Query(String header, String sequence) {
        this.header = header;
        this.sequence = sequence;
    }

    /**
     * clear Arraylist orfList
     */
    public void clearOrfList() {
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
     *
     * @return Arraylist of protein sequences
     */
    public ArrayList<String> getSequenceList() {
        ArrayList<String> sequenceList = new ArrayList<String>();
        for (ORF singleORF : orfList) {
            sequenceList.add(this.sequence.substring(singleORF.getStartPosition(), singleORF.getStopPosition()));
        }
        return sequenceList;
    }

    /**
     * Gets orfList
     *
     * @return value of orfList
     */
    public ArrayList<ORF> getOrfList() {
        return orfList;
    }

    /**
     * Add another Orf object to the query
     *
     * @param newOrf ORF object to add
     */
    public void addOrfList(ORF newOrf) {
        if (orfList == null) {
            orfList = new ArrayList<ORF>();
        }
        orfList.add(newOrf);
    }
}
