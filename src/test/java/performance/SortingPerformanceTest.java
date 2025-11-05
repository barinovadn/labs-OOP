package performance;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class SortingPerformanceTest {

    private static final Logger logger = Logger.getLogger(SortingPerformanceTest.class.getName());
    private Connection connection;

    @BeforeEach
    void setup() throws SQLException {
        connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/lab5_oop",
                "postgres",
                "admin"
        );
        generateTestData();
    }

    void generateTestData() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("SET session_replication_role = 'replica'");

            stmt.execute("DELETE FROM computed_points");
            stmt.execute("DELETE FROM composite_functions");
            stmt.execute("DELETE FROM functions");
            stmt.execute("DELETE FROM users");

            stmt.execute("INSERT INTO users (user_id, username, password, email) VALUES (1, 'perfuser', 'pass', 'perf@test.com')");

            StringBuilder sb = new StringBuilder();
            sb.append("INSERT INTO functions (function_id, user_id, function_name, function_type, function_expression, x_from, x_to) VALUES ");

            String[] types = {"SQR", "LINEAR", "CONST", "SIN", "COS"};

            for (int i = 0; i < 10000; i++) {
                String type = types[i % types.length];
                sb.append("(").append(i + 1).append(", 1, 'func_").append(i).append("', '")
                        .append(type).append("', 'expression_").append(i).append("', ")
                        .append(i * 0.1).append(", ").append(i * 0.1 + 5.0).append(")");

                if (i < 9999) {
                    sb.append(", ");
                }
            }

            stmt.execute(sb.toString());

            stmt.execute("INSERT INTO computed_points (function_id, x_value, y_value) VALUES " +
                    "(1, 1.0, 1.0), (1, 2.0, 4.0), (1, 3.0, 9.0), " +
                    "(2, 1.0, 2.0), (2, 2.0, 4.0), (2, 3.0, 6.0)");

            stmt.execute("SET session_replication_role = 'origin'");
        }
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
            long totalTime = 0;
            int count = 0;

            for (int i = 0; i < 50; i++) {
                long startTime = System.nanoTime();
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                int rowCount = 0;
                while (rs.next()) {
                    rowCount++;
                }

                long time = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);
                totalTime += time;
                count = rowCount;

                rs.close();
                stmt.close();
            }

            long avgTime = totalTime / 50;
            System.out.println(query + "," + avgTime + "," + count);
        }
    }
}