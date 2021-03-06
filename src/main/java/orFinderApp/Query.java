package orFinderApp;

import java.util.ArrayList;

/**
 * This class represents a DNA sequence with the possibility to add ORFs located on the sequence.
 *
 * @author Lex Bosch
 * @version 1.0
 * 29-03-2019
 */
public class Query {
    /**
     * ArrayList containing ORFinder.orFinderApp.ORF resutls
     */
    private ArrayList<ORF> orfList;

    /**
     * Gets header
     *
     * @return value of header
     */
    public String getHeader() {
        return header;
    }

    /**
     * Sets header
     *
     * @param header sets this value into the class variable
     */
    public void setHeader(String header) {
        this.header = header;
    }

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
        setSequence(sequence);
    }

    /**
     * Sets sequence
     *
     * @param sequence sets this value into the class variable
     */
    void setSequence(String sequence) {
        this.sequence = sequence.replaceAll("\n", "");
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
     * Creates a list of sequences of the ORFs.
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
     * Sets orfList
     *
     * @param orfList orfList to be set
     */
    public void setOrfList(ArrayList<ORF> orfList) {
        this.orfList = orfList;
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


    /**
     * Returns the subsequence of the ORF.
     *
     * @param subStringOrf the orf
     * @return String subsequence of the ORF
     */
    public String getSubSequence(ORF subStringOrf){
        int start = subStringOrf.getStartPosition();
        int stop = subStringOrf.getStopPosition();
        if(start < stop){
            return this.sequence.substring(start, stop);
        } else {
            return this.sequence.substring(stop,start);
        }
    }
}


