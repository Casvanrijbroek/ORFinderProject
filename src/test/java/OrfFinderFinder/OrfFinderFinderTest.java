package OrfFinderFinder;
import orFinderApp.Query;
import org.junit.Test;



import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrfFinderFinderTest {
    public static Query testQuery = new Query("test", "ATGGATGTTTTTTTTTTAATGTTGATGTGTGTGNTGTGGATGGTGTGTGTGTGTGTGTGTGTTGTGTTGTGTGTACGTGCATAGCTAGCTCGATCGTCGATCGATCGATGATTGCGTATATAATGCGCGTGACTGACTACGTACGGGGGGGGGGGGGGGGGGTGTGTATGGTGTTGTGTGTGTTGTTAAATGTGATGTACATGATGCACACCCTACCTTCCTAAACTCCTGGGAGCAAGATGACGGTTGTGGACCGTGGAGCGACACGCGACGCTCTGCGTCTCGG");

    @Test
    public void FindOrfTest(){
        OrfFinderFinder test = new OrfFinderFinder();
        assertEquals(12, test.findGenes(testQuery.getSequence()).size());
    }
}
