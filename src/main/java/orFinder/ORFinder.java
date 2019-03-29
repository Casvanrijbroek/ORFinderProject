package orFinder;

import orFinderApp.ORF;
import orFinderApp.Query;

import java.util.HashSet;

/**
 * ORF finder finds Genes on the basis of start and stop codons.
 * Class excepts Query finds start and stops.
 * The Query adds an ORF object with the start and stop position of the founded ORF.
 *
 * @author Elco van Rijswijk
 * @version 1.0
 * 29-03-2019
 */
public class ORFinder {
    /**
     * A start codon.
     */
    private final static String startCodon = "ATG";
    /**
     * A stop codon.
     */
    private final static String stop1Codon = "TAA";
    /**
     * A stop codon.
     */
    private final static String stop2Codon = "TGA";
    /**
     * A stop codon.
     */
    private final static String stop3Codon = "TAG";
    /**
     * Boolean indicating orientation.
     */
    private Boolean Forward = true;
    /**
     * Boolean indicating whether or not ot continue.
     */
    private Boolean Continue = true;

    /**
     * HandleQuery handles sequence from query makes and calls FindGenes with forward and reverse strand.
     *
     * @param query with header and sequence
     */
    public void HandleQuery(Query query) {

        FindGenes(query.getSequence(), query);
        Continue = true;
        Forward = false;
        FindGenes(new StringBuilder(query.getSequence()).reverse().toString(), query);

    }

    /**
     * FindGenes finds start codons and calls FindStopCodon to find a stop codon.
     * FindGenes also checks if founded ORF >100 nucleotides.
     * FindGenes also checks if StopCodon isn't already present so nested ORF are not counted as new genes.
     *
     * @param frame Is being searched for start codons
     * @param query Where if a Gene is found start and stop position will be added to that query
     */
    private void FindGenes(String frame, Query query) {
        HashSet<Integer> StopCodon= new HashSet<>();
        int start = 0;
        int stop;

        while (Continue) {
            start = frame.indexOf(startCodon, start + 1);

            if (start < 0) {
                Continue = false;
            } else {
                stop = FindStopCodon(frame, start);

                if ((stop - start > 100) && !StopCodon.contains(stop)) {
                    StopCodon.add(stop);

                    if (Forward) {
                        query.addOrfList(new ORF((start + 1), (stop + 3)));
                    }

                    if (!Forward) {
                        int startFor = (frame.length() - start);
                        int stopFor = (frame.length() - stop);
                        query.addOrfList(new ORF((startFor - 1), (stopFor - 3)));
                    }
                }
            }
        }

    }
    /**
     * FindStopCodon accepts the frame with its start position and if a stop codon is found and
     * returns the stop position.
     *
     * @param frame the String frame of the DNA sequence from FindGenes
     * @param start Integer start position of startcodon on the frame from FindGenes
     * @return stop position of founded gene
     */
    private int FindStopCodon(String frame, int start) {
        for (int i = start; i < frame.length(); i += 3) {
            if ((i + 90) > frame.length()) {
                Continue = false;
            } else {
                String frameString = frame.substring(i, i + 3);

                switch (frameString) {
                    case stop1Codon:
                        return i;
                    case stop2Codon:
                        return i;
                    case stop3Codon:
                        return i;
                }
            }
        }

        return -1;
    }
}
