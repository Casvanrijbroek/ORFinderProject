package blastConnector;

import blastConnetor.NoBlastConnectionException;
import blastConnetor.proteinBlast;
import localDataManagement.LocalSave;
import orFinderApp.ORF;
import orFinderApp.Query;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ConnectionBlastTest {
    private Query query;
    private ORF newOrf;
    private ORF newOrf2;
    private proteinBlast testBlast;
    private final String SEQUENCE = "GCGGGGGGGAAGGAACCAACAGGGATTGCCTTAGTAACTGCGAGTGAAGCGGCAAAAGCTCAGATTTGAAACCTGTCGT" +
            "CTTTGGCGATCGGATTGTAATCTGGAGAGACATCTTCTGGCGGACAGCCGGTCCAAGTTCCTTGGAACAGGACGTCGGAGAGGGTGAGAATCCCGTATCTGGC" +
            "CGGTTTGTTCCGCCTTGTGAAGCGTCTTCGACGAGTCGAGTTGTTTGGGAATGCAGCTCAAAGTGGGTGGTAAATTTCATCTAAAGCTAAATATTGGCGAGAG" +
            "ACCGATAGCGCACAAGTAGAGTGATCGAAAGATGAAAAGCACTTTGAAAAGAGAGTCAAAAAGTACGTGAAATTGTTGAAAGGGAAGCGCTTGAGACCAGACT" +
            "CGCGTTGGGGTGATCAGCGGTGCTTCTGTGCCGTGCACTTGCCCCTTCTCGGGCCAGCATCGGTTGCGACGGCGGGATAAAGACCTGAGGAATGTGGCTCCCC" +
            "TCGGGGAGTGTTATAGCCTCGGGTGCAATGCCGCCAGCCGCGACCGAGGACCGCGCTTCGGCTAGGATGCTGGCATAATGGTCTTCAGCGACCCGTCTTGAAA" +
            "ACGGACCAAGGAGTCTAACATCTATGCGAGTGTTCGGGTGTAAAACCCCTGCGCGTAATGAAAGTGAACGGAGGTGGGAACCCTTCACGGGGTGCACCATCGA" +
            "CCGATCCTGATGTCTTCGGATGGATTTGAGTAGGAGCATAGCTGTTGGGACCCGAAAGATGGTGAACTATGCCTGAATAGGGTGAAGCCAGAGGAAACTCTGG" +
            "TGGAGGCTCGCAGCGGTTCTGACGTGCAAATCGATCGTCAAATTTGGGTATAGGGGCGAAAGACTAATCGAACCATCTAGTAGCTGGTTCCTGCCGAAGTTTC" +
            "CCCCTCCAGGAA";

    @Before
    public void setQuery(){
        this.query = new Query("test header", SEQUENCE);
        this.newOrf = new ORF(200, 400);
        this.newOrf2 = new ORF(400, 200);
        this.query.addOrfList(this.newOrf2);
        this.query.addOrfList(this.newOrf);
        this.testBlast = new proteinBlast();
    }


    @Test
    public void blastSequence() throws FileNotFoundException, NoBlastConnectionException, InterruptedException {
            //testBlast.blast(query));
        LocalSave newsave = new LocalSave();
        newsave.saveResults(query);
    }



}
