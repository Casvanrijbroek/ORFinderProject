package OrfFinderFinder;




import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class OrfFinderFinder {
    final List<String> stop_codons = Arrays.asList("TGA", "TAG", "TAA");


    List<Integer> start_positions = new ArrayList<>();
    List<Integer> stop_positions = new ArrayList<>();


    public static String Sequence = "ATGGTGTTGATGTGTGTGTGTGGGGGGGGGGGGGGGGGGGTGTGTATGGTGTTGTGTGTGTTGTTAAATGTGATGTACATGATGCACACCCTACCTTCCTAAACTCCTGGGAGCAAGATGACGGTTGTGGACCGTGGAGCGACACGCGACGCTCTGCGTCTCGG";


    public static void main(String[] args)  {
        FindOrf(Sequence);
    }

    private static void FindOrf(String Sequence)  {
        ArrayList<String> Frames = ReadingFrames(Sequence);
        for (int i = 0; i < Frames.size(); i++) {
            SearchORF(Frames.get(i));
        }


    }

    static ArrayList<String> ReadingFrames(String seq) {

        ArrayList<String> Frames = new ArrayList<String>();

        String seqFor = seq;

        String seqRev = new StringBuilder(seq).reverse().toString();

        String for1, for2, for3, rev1, rev2, rev3;

        for1 = seqFor;
        Frames.add(for1);
        for2 = seqFor.substring(1);
        Frames.add(for2);
        for3 = seqFor.substring(2);
        Frames.add(for3);
        rev1 = seqRev;
        Frames.add(rev1);
        rev2 = seqRev.substring(1);
        Frames.add(rev2);
        rev3 = seqRev.substring(2);
        Frames.add(rev3);


        return Frames;

    }

    static void SearchORF(String Frame)  {

        String[] codon_list = makeCodons(Frame);
        String start = "ATG";
        String stop = "TAA";

        if (Arrays.asList(codon_list).contains(start) && Arrays.asList(codon_list).contains(start)) {


            System.out.println(Arrays.asList(codon_list).contains(start));
            System.out.println(Arrays.asList(codon_list).contains(stop));

            System.out.println(Arrays.asList(codon_list).indexOf(start));
            System.out.println(Arrays.asList(codon_list).indexOf(stop));

        }
    }


    static String[] makeCodons(String Frame) {
        String codon_list[] = new String[(Frame.length() / 3)];
        int b = 0;
        for (int a = 0; a < Frame.length() - (Frame.length() % 3); a += 3) {

            codon_list[b] = Frame.substring(a, a + 3);
            b++;
        }

        return codon_list;
    }
}
