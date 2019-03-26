package OrfFinderFinder;

import orFinderApp.ORF;
import orFinderApp.Query;

import java.util.ArrayList;

public class OrfFinderFinder {

    final static String startCodon = "ATG";
    final static String stop1Codon = "TAA";
    final static String stop2Codon = "TGA";
    final static String stop3Codon = "TAG";

    ArrayList<ORF> ORFresults = new ArrayList<>();


    public void HandleQuery(Query query) {

        ArrayList<String> frames = GenReadingFrame(query.getSequence());
        for (int frame = 0; frame < frames.size(); frame++) {
            findGenes(frames.get(frame));


        }

    }


    public ArrayList<String> findGenes(String frame) {

        ArrayList<String> allGenes = new ArrayList<String>();

        String DNA = frame.toUpperCase();
        int start = -1;
        while (true) {

            start = DNA.indexOf(startCodon, start + 1);
            if (start == -1) {
                break;
            }

            int stop = findStopCodon(DNA, start);

            if (stop > start) {
                String gene = frame.substring(start, stop + 3);

                if (!allGenes.contains(gene)) {
                    allGenes.add(gene);
                }

                System.out.println("From: " + start + " to " + stop + " Gene: " + gene);
            }

        }

        return allGenes;


    }


    private int findStopCodon(String DNA, int start) {

        for (int i = start + 3; i < DNA.length() - 3; i += 3) {

            String frameString = DNA.substring(i, i + 3);

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


    private ArrayList<String> GenReadingFrame(String Sequence) {
        ArrayList<String> frames = new ArrayList();

        String ForSeq = Sequence;
        String RevSeq = new StringBuilder(Sequence).reverse().toString();
        frames.add(ForSeq);
        frames.add(RevSeq);
        return frames;
    }
}
