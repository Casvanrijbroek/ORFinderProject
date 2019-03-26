package blastConnector;

import blastConnetor.NoBlastConnectionException;
import blastConnetor.proteinBlast;
import orFinderApp.ORF;
import orFinderApp.Query;
import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class NoConnectionBlastTest {

    private Query query;
    private ORF newOrf;
    private proteinBlast testBlast;
    private final String SEQUENCE = "RNQSVLSLYKTSDVFRLPEKLQSSNSSKNQRSPSMLLFPDNTSNVYSKHRIAKEPSVDNESEDMS" +
            "DSKQKISHLSKVTLVSTLMKGVDYPSSYAVDKIMPPTPAKKVEFILNSLYIPEDLNEQSGTLQGTSTTSS" +
            "LDNNSNSNSRSNTSSMSVLHRSAIGLLAKWMKNHNRHDSSNDKKFMSAIKPANQKPEMDAFVKYVVSISS" +
            "LNRKSSKEEEEEFLNSDSSKFDILSARTIDEVESLLHLQNQLIEKVQTHSNNNRGPTVNVDCERREHIHD" +
            "IKILQQNSFKPSNDNFSAMDNLDLYQTVSSIAQSVISLTNTLNKQLQNNESNMQPSPSYDALQRRKVKSL" +
            "TTAYYNKMHGSYSAESMRLFDKDNSSSRTDENGPQRLLFHETDKTNSEAITNMTPRRKNHSQSQKSMTSS" +
            "PLKNVLPDLKESSPLNDSREDTESITYSYDSELSSSSPPRDTVTKKSRKVRNIVNNTDSPTLKTKTGFLN" +
            "LREFTFEDTKSLDEKKSTIDGLEKNYDNKENQESEYESTKKLDNSLDASSEANNYDITTRKKHSSCNHKI";

    @Before
    public void setQuery(){
        this.query = new Query("test header", SEQUENCE);
        this.newOrf = new ORF(200, 400);
        this.query.addOrfList(this.newOrf);
        this.testBlast = new proteinBlast();
    }


    @Test
    public void blastSequence(){
        assertThrows(NoBlastConnectionException.class, () -> testBlast.blast(query));
    }

}