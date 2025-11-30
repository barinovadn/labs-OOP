package manual.search;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

import manual.DatabaseConnection;

public class FunctionGraphSearch {
    private static final Logger logger = Logger.getLogger(FunctionGraphSearch.class.getName());

    private final Connection connection;

    public FunctionGraphSearch() throws SQLException {
        this.connection = DatabaseConnection.getConnection();
        logger.info("FunctionGraphSearch initialized");
    }

    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            logger.info("FunctionGraphSearch closed");
        }
    }
}
