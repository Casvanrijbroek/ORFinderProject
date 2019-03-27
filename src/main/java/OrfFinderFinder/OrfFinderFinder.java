package OrfFinderFinder;


import orFinderApp.ORF;
import orFinderApp.Query;

import java.util.ArrayList;

/**
 * ORF finder finds Genes on the basis of start and stop codons
 * Class excepts Query finds start and stops
 * The Query adds an ORF object with the start and stop position of the founded gene
 */
public class OrfFinderFinder {

    final static String startCodon = "ATG";
    final static String stop1Codon = "TAA";
    final static String stop2Codon = "TGA";
    final static String stop3Codon = "TAG";

    Boolean Forward = true;
    Boolean Continue = true;

    /**
     * HandleQuery calls function GenreadingFrame and findGenes
     *
     * @param query
     */
    public void HandleQuery(Query query) {

        ArrayList<String> frames = GenReadingFrame(query.getSequence());

        String RevSeq = new StringBuilder(query.getSequence()).reverse().toString();
        String ForSeq = query.getSequence();

        FindGenes(ForSeq, query);
        Continue = true;
        Forward = false;
        FindGenes(RevSeq, query);


    }

    /**
     * FindGenes finds start codons and if a gene is found start and stop position are added
     * to the Query
     *
     * @param frame Is being searched for start codons
     * @param query Where if a Gene is found start and stop position will be added to that query
     * @return Allgenes where all founded genes are stored in as Strings (not active!!)
     */
    public ArrayList<String> FindGenes(String frame, Query query) {

        ArrayList<String> allGenes = new ArrayList<String>();

        int start = 0;
        int stop = 0;

        while (Continue) {
            start = frame.indexOf(startCodon, stop);

            if (start < 0) {
                Continue = false;
            }

            if (start > 0) {
                stop = FindStopCodon(frame, start);


                if ((stop - start > 100)) {
                    //String gene = frame.substring(start, stop + 3);
                    //if (!allGenes.contains(gene)) {
                    //  allGenes.add(gene);
                    //}
                    if (Forward) {
                        query.addOrfList(new ORF(start, (stop + 3)));
                    }

                    if (!Forward) {
                        int startfor = (frame.length() - start);
                        int stopfor = (frame.length() - stop);
                        query.addOrfList(new ORF(startfor, (stopfor - 3)));
                    }
                    //System.out.println("From: " + start + " to " + stop + " Gene: ");
                }
            }
        }

        return allGenes;


    }


    /**
     * FindStopCodon accepts the frame with its start position and if a stop codon is found
     * returns the stop position
     *
     * @param frame the String frame from FindGenes
     * @param start Integer start position from FindGenes
     * @return stop position of founded gene
     */
    public int FindStopCodon(String frame, int start) {

        for (int i = start; i < frame.length(); i += 3) {

            if ((i + 3) < frame.length()) {

                String frameString = frame.substring(i, i + 3);

                if (frameString.equals(stop1Codon)) {
                    return i;
                } else if (frameString.equals(stop2Codon)) {
                    return i;

                } else if (frameString.equals(stop3Codon)) {
                    return i;
                }
            }
            if ((i + 3) > frame.length()) {
                Continue = false;
            }

        }
        return -1;

    }

    /**
     * GenReadingFrame accepts the sequence and returns the forward and reverse strand of the sequence
     *
     * @param Sequence from HandleQuery
     * @return Arraylist with the forward and reverse strand
     */
    public ArrayList<String> GenReadingFrame(String Sequence) {

        ArrayList<String> frames = new ArrayList();

        String ForSeq = Sequence;
        String RevSeq = new StringBuilder(Sequence).reverse().toString();

        frames.add(ForSeq);
        frames.add(RevSeq);

        return frames;
    }


}
