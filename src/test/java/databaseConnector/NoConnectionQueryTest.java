package databaseConnector;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class NoConnectionQueryTest {
    private final Connector connector = new Connector();

    @Test
    public void getQueryNoConnection() {
        assertThrows(ConnectionException.class, () -> connector.getQuery(SearchOption.ID, 2));
    }
}
