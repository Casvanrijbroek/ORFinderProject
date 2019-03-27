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

    /**
     * HandleQuery calls function GenreadingFrame and findGenes
     *
     * @param query
     */
    public void HandleQuery(Query query) {

        ArrayList<String> frames = GenReadingFrame(query.getSequence());
        for (int frame = 0; frame < frames.size(); frame++) {
            FindGenes(frames.get(frame), query);
            Forward = false;


        }

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

        int start = -1;
        while (true) {

            start = frame.indexOf(startCodon, start + 1);
            if (start == -1) {
                break;
            }

            int stop = FindStopCodon(frame, start);

            if (stop > start) {
                // gene = frame.substring(start, stop + 3);

                //if (!allGenes.contains(gene)) {
                //    allGenes.add(gene);
                //}

                if ((stop - start > 102)) {
                    if (Forward) {
                        query.addOrfList(new ORF(start, (stop + 3)));
                    }
                    if (!Forward) {
                        start = (frame.length() - start);
                        stop = (frame.length() - stop);
                        query.addOrfList(new ORF(start, (stop - 3)));
                    }
                    //System.out.println("From: " + start + " to " + stop + " Gene: " + gene);
                }

            }
        }

        return allGenes;


    }


    /**
     * FindStopCodon accepts the frame with its start position and if a stop codon is found
     * returns the stop position
     * @param frame the String frame from FindGenes
     * @param start Integer start position from FindGenes
     * @return stop position of founded gene
     */
    public int FindStopCodon(String frame, int start) {

        for (int i = start + 3; i < frame.length() - 3; i += 3) {

            String frameString = frame.substring(i, i + 3);

            if (frameString.equals(stop1Codon)) {
                return i;
            } else if (frameString.equals(stop2Codon)) {
                return i;

            } else if (frameString.equals(stop3Codon)) {
                return i;
            }

        }
        return -1;

    }

    /**
     * GenReadingFrame accepts the sequence and returns the forward and reverse strand of the sequence
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
