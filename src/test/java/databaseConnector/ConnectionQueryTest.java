package databaseConnector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;

import java.sql.SQLException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
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
    @Disabled
    public void insertQuery() {

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
    public void closeConnection() throws SQLException {
        connector.closeConnection();
    }
}