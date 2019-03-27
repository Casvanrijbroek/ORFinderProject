package OrfFinderFinder;
import orFinderApp.Query;
import org.junit.Test;



import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrfFinderFinderTest {
    public static Query testQuery = new Query("test", "ATGGATGTTTTTTTTTTAATGTTGATGTGTGTGNTGTGGATGGTGTGTGTGTGTGTGTGTGTTGTGTTGTGTGTACGTGCATAGCTAGCTCGATCGTCGATCGATCGATGATTGCGTATATAATGCGCGTGACTGACTACGTACGGGGGGGGGGGGGGGGGGTGTGTATGGTGTTGTGTGTGTTGTTAAATGTGATGTACATGATGCACACCCTACCTTCCTAAACTCCTGGGAGCAAGATGACGGTTGTGGACCGTGGAGCGACACGCGACGCTCTGCGTCTCGG");

    @Test
    public void FindOrfTest(){
        OrfFinderFinder test = new OrfFinderFinder();
        assertEquals(0, test.FindGenes(testQuery.getSequence(), testQuery).size());
    }

    @Test
    public void GenReadingFrameTest(){
        OrfFinderFinder test = new OrfFinderFinder();
        assertEquals(2, test.GenReadingFrame(testQuery.getSequence()).size());
    }

    @Test
    public void HandleQueryTest() {
        OrfFinderFinder test = new OrfFinderFinder();
        test.HandleQuery(testQuery);
    }


}
