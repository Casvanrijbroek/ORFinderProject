package databaseConnector;

import org.junit.jupiter.api.*;

import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ConnectorTest {
    private final Connector connector = new Connector();

    @BeforeAll
    void makeConnection() throws SQLException{
        connector.makeConnection();
    }

    @Test
    void getQuery() {
        Assertions.assertAll(
                () -> assertEquals("ATCG", connector.getQuery(SearchOption.NAME, "organisme 2")
                        .getSequence()),
                () -> assertEquals("ATCG", connector.getQuery(SearchOption.ID, 2).getSequence())
        );
    }

    @AfterAll
    void closeConnection() throws SQLException {
        connector.closeConnection();
    }
}