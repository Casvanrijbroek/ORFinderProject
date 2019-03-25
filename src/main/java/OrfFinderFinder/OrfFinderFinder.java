package OrfFinderFinder;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class OrfFinderFinder {

    static final String regex = "(ATG)([ATCGN]{3})*?(TGA|TAG|TAA)";
    static final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);

    //public static String Sequence = "ATGGTGTTGATGTGTGTGNTGTGGATGGTGTGTGTGTGTGTGTGTGTTGTGTTGTGTGTACGTGCATAGCTAGCTCGATCGTCGATCGATCGATGATTGCGTATATAATGCGCGTGACTGACTACGTACGGGGGGGGGGGGGGGGGGTGTGTATGGTGTTGTGTGTGTTGTTAAATGTGATGTACATGATGCACACCCTACCTTCCTAAACTCCTGGGAGCAAGATGACGGTTGTGGACCGTGGAGCGACACGCGACGCTCTGCGTCTCGG";
    //public static void main(String[] args) {
     //   HashMap<Integer,String> FoundedOrf = FindOrf(Sequence);
    //}


    private static HashMap<Integer, String> FindOrf(String Sequence) {
        HashMap<Integer,String> FoundedOrf = new HashMap<>();
        final Matcher matcher = pattern.matcher(Sequence);

        while (matcher.find()) {
            if (matcher.group(0).length()>100){FoundedOrf.put(Sequence.indexOf(matcher.group(0)),matcher.group(0));}
            //System.out.println(Sequence.indexOf(matcher.group(0)));
            //System.out.println("Full match: " + matcher.group(0));
            //for (int i = 1; i <= matcher.groupCount(); i++) {
              //  System.out.println("Group " + i + ": " + matcher.group(i));
            //}
        }
        return FoundedOrf;
    }
}
