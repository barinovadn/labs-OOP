package performance;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.sql.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class SortingPerformanceTest {

    private static final Logger logger = Logger.getLogger(SortingPerformanceTest.class.getName());
    private static Connection connection;

    @BeforeAll
    static void setup() throws SQLException {
        connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/lab5_oop",
                "postgres",
                "admin"
        );
    }

    @Test
    void testOrderByPerformance() throws SQLException {
        logger.info("Testing ORDER BY performance in framework branch");

        String[] queries = {
                "SELECT * FROM users ORDER BY username ASC",
                "SELECT * FROM users ORDER BY username DESC",
                "SELECT * FROM functions ORDER BY function_name ASC",
                "SELECT * FROM functions ORDER BY function_type DESC",
                "SELECT * FROM computed_points ORDER BY x_value ASC",
                "SELECT * FROM computed_points ORDER BY y_value DESC"
        };

        System.out.println("Query,TimeMs,RowCount");

        for (String query : queries) {
            long startTime = System.nanoTime();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            int count = 0;
            while (rs.next()) {
                count++;
            }

            long time = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);
            System.out.println(query + "," + time + "," + count);

            rs.close();
            stmt.close();
        }
    }
}