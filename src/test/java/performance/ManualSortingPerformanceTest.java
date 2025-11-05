package performance;

import manual.DatabaseConnection;
import manual.dto.FunctionResponse;
import manual.repository.FunctionRepository;
import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ManualSortingPerformanceTest {
    private Connection connection;
    private FunctionRepository functionRepository;

    @BeforeAll
    void setup() throws SQLException {
        connection = DatabaseConnection.getConnection();
        functionRepository = new FunctionRepository(connection);
        generateTestData();
    }

    private void generateTestData() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM computed_points");
            stmt.execute("DELETE FROM composite_functions");
            stmt.execute("DELETE FROM functions");
            stmt.execute("DELETE FROM users");

            stmt.execute("INSERT INTO users (user_id, username, password, email) VALUES (1, 'perfuser', 'pass', 'perf@test.com')");

            StringBuilder sb = new StringBuilder();
            sb.append("INSERT INTO functions (user_id, function_name, function_type, function_expression, x_from, x_to) VALUES ");

            String[] types = {"SQR", "LINEAR", "CONST", "SIN", "COS"};

            for (int i = 0; i < 10000; i++) {
                String type = types[i % types.length];
                sb.append("(1, 'func_").append(i).append("', '")
                        .append(type).append("', 'expression_").append(i).append("', ")
                        .append(i * 0.1).append(", ").append(i * 0.1 + 5.0).append(")");

                if (i < 9999) {
                    sb.append(", ");
                }
            }

            stmt.execute(sb.toString());
        }
    }

    @AfterAll
    void cleanup() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    @Test
    void testDatabaseSortingPerformance() throws SQLException {
        List<FunctionResponse> functions = functionRepository.findAll();
        assertEquals(10000, functions.size());

        long dbNameSortTime = measureDatabaseSortTime(() -> {
            try {
                functionRepository.findAllOrderByNameAsc();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        long dbTypeSortTime = measureDatabaseSortTime(() -> {
            try {
                functionRepository.findAllOrderByTypeAndName();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        long memoryNameSortTime = measureMemorySortTime(functions,
                Comparator.comparing(FunctionResponse::getFunctionName));

        long memoryTypeSortTime = measureMemorySortTime(functions,
                Comparator.comparing(FunctionResponse::getFunctionType)
                        .thenComparing(FunctionResponse::getFunctionName));

        saveResults(dbNameSortTime, dbTypeSortTime, memoryNameSortTime, memoryTypeSortTime);

        assertTrue(dbNameSortTime > 0);
        assertTrue(dbTypeSortTime > 0);
        assertTrue(memoryNameSortTime > 0);
        assertTrue(memoryTypeSortTime > 0);
    }

    private long measureDatabaseSortTime(Runnable query) {
        long totalTime = 0;

        for (int i = 0; i < 50; i++) {
            long startTime = System.nanoTime();
            query.run();
            long endTime = System.nanoTime();

            totalTime += (endTime - startTime);
        }

        return totalTime / 50;
    }

    private long measureMemorySortTime(List<FunctionResponse> functions, Comparator<FunctionResponse> comparator) {
        long totalTime = 0;

        for (int i = 0; i < 50; i++) {
            List<FunctionResponse> copy = new ArrayList<>(functions);

            long startTime = System.nanoTime();
            copy.sort(comparator);
            long endTime = System.nanoTime();

            totalTime += (endTime - startTime);
        }

        return totalTime / 50;
    }

    private void saveResults(long dbNameSort, long dbTypeSort, long memoryNameSort, long memoryTypeSort) {
        java.util.logging.Logger logger = java.util.logging.Logger.getLogger(ManualSortingPerformanceTest.class.getName());
        logger.info("Manual JDBC Performance Results (ns):");
        logger.info("Database sort by name: " + dbNameSort);
        logger.info("Database sort by type and name: " + dbTypeSort);
        logger.info("Memory sort by name: " + memoryNameSort);
        logger.info("Memory sort by type and name: " + memoryTypeSort);
        logger.info("Data size: 10000 records");
        logger.info("Test iterations: 50");
    }
}