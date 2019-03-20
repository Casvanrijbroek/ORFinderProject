package databaseConnector;

import orFinderApp.ORF;
import orFinderApp.Query;
import orFinderApp.Result;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ConnectionQueryTest {
    private final Connector connector = new Connector();

    @Before
    public void makeConnection() throws SQLException{
        connector.makeConnection();
    }

    @Test
    public void getQueryString() throws SQLException, ConnectionException {
        assertEquals("ATCG", connector.getQuery(SearchOption.NAME, "organisme 1")
                        .getSequence());
    }

    @Test
    public void getQueryInt() throws SQLException, ConnectionException {
        assertEquals("ATCG", connector.getQuery(SearchOption.ID, 1).getSequence());
    }

    @Test
    public void getQueryEmpty() {
        assertThrows(SQLException.class, () -> connector.getQuery(SearchOption.NAME, ""));
    }

    @Test
    public void hasNOResult() throws ClassNotFoundException {
    }

    @Test
    public void insertQuery() throws SQLException, ConnectionException {
        Query query = new Query("delete this", "ATCG");
        ORF orf = new ORF(1, 10);
        Result result = new Result();
        ArrayList<ORF> orfList = new ArrayList<>();
        ArrayList<Result> resultList = new ArrayList<>();

        result.setAccession("ACCESSION10");
        result.setDescription("delete this");
        result.setScore(10);
        result.setQueryCover(20);
        result.setpValue(0.001f);
        result.setIdentity(30);
        result.setStartPosition(1);
        result.setStopPosition(10);

        query.setOrfList(orfList);
        orfList.add(orf);
        orf.setResultList(resultList);
        resultList.add(result);

        connector.insertQuery(query);
    }

    @Test
    public void deleteQuery() throws SQLException, ConnectionException {
        connector.deleteQuery(SearchOption.NAME, "delete this");
    }

    @Test
    public void deleteQueryInt() throws SQLException, ConnectionException {
        connector.deleteQuery(SearchOption.ID, 4);
    }

    @Disabled
    public void deleteQueryFalseIndex() throws SQLException, ConnectionException {
        connector.deleteQuery(SearchOption.ID, -1);
    }

    @After
    public void closeConnection() throws SQLException, ConnectionException {
        connector.closeConnection();
    }
}