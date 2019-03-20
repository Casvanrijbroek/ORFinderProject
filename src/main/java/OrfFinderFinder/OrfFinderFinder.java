package OrfFinderFinder;


import orFinderApp.Query;
import org.biojava.bio.seq.FramedFeature;
import org.biojava.bio.seq.Sequence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OrfFinderFinder {
    final List<String> stop_codons = Arrays.asList("tga","tag","taa");
    final String start_codon = "atg";

    List<Integer> start_positions = new ArrayList<>();
    List<Integer> stop_positions = new ArrayList<>();
    Integer count_start=0;
    Integer count_stop=0;

    public static String Sequence="ATGGTGTTGTGTGTGTGTGGGGGGGGGGGGGGGGGGGTGTGTGTGTTGTGTGTGTTGTTAAATGTGATGTACCACACCCTACCTTCCTAAACTCCTGGGAGCAAGATGACGGTTGTGGACCGTGGAGCGACACGCGACGCTCTGCGTCTCGG";

    public static void main(String[] args){
        FindOrf(Sequence);
    }

    private static void FindOrf(String Sequence){
        ArrayList<String> Frames = ReadingFrames();


    }
    private static ArrayList<String> ReadingFrames()    {
        ArrayList<String> Frames = new ArrayList<String>();



        return Frames;

    }
}
