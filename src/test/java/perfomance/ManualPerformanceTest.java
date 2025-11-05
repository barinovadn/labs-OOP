package manual.performance;

import manual.DatabaseConnection;
import manual.dto.*;
import manual.repository.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ManualPerformanceTest {
    private static final Logger logger = Logger.getLogger(ManualPerformanceTest.class.getName());
    private static final int OPERATION_COUNT = 10000;

    private Connection connection;
    private UserRepository userRepository;
    private FunctionRepository functionRepository;
    private PointRepository pointRepository;

    private List<Long> userIds = new ArrayList<>();
    private List<Long> functionIds = new ArrayList<>();
    private List<Long> pointIds = new ArrayList<>();

    public ManualPerformanceTest() throws SQLException {
        this.connection = DatabaseConnection.getConnection();
        this.userRepository = new UserRepository(connection);
        this.functionRepository = new FunctionRepository(connection);
        this.pointRepository = new PointRepository(connection);
    }

    public void cleanup() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM computed_points");
            stmt.execute("DELETE FROM composite_functions");
            stmt.execute("DELETE FROM functions");
            stmt.execute("DELETE FROM users");
        }
        userIds.clear();
        functionIds.clear();
        pointIds.clear();
    }

    public void close() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    public PerformanceResult testCreateOperations() throws SQLException {
        logger.info("Starting CREATE operations test");

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < OPERATION_COUNT; i++) {
            UserDto user = new UserDto(null,
                    "user_" + i,
                    "password_" + i,
                    "user_" + i + "@test.com",
                    LocalDateTime.now()
            );
            Long userId = userRepository.create(user);
            userIds.add(userId);
        }

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;

        logger.info("CREATE operations completed");
        return new PerformanceResult("CREATE", OPERATION_COUNT, totalTime);
    }

    public PerformanceResult testReadOperations() throws SQLException {
        logger.info("Starting READ operations test");

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < OPERATION_COUNT; i++) {
            UserDto user = userRepository.findById(userIds.get(i));
        }

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;

        logger.info("READ operations completed");
        return new PerformanceResult("READ", OPERATION_COUNT, totalTime);
    }

    public PerformanceResult testUpdateOperations() throws SQLException {
        logger.info("Starting UPDATE operations test");

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < OPERATION_COUNT; i++) {
            UserDto user = new UserDto(userIds.get(i),
                    "updated_user_" + i,
                    "updated_password_" + i,
                    "updated_user_" + i + "@test.com",
                    LocalDateTime.now()
            );
            userRepository.update(user);
        }

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;

        logger.info("UPDATE operations completed");
        return new PerformanceResult("UPDATE", OPERATION_COUNT, totalTime);
    }

    public PerformanceResult testDeleteOperations() throws SQLException {
        logger.info("Starting DELETE operations test");

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < OPERATION_COUNT; i++) {
            userRepository.delete(userIds.get(i));
        }

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;

        logger.info("DELETE operations completed");
        return new PerformanceResult("DELETE", OPERATION_COUNT, totalTime);
    }

    public PerformanceResult testSearchOperations() throws SQLException {
        logger.info("Starting SEARCH operations test");

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < 1000; i++) {
            List<UserDto> users = userRepository.findAll();
        }

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;

        logger.info("SEARCH operations completed");
        return new PerformanceResult("SEARCH", 1000, totalTime);
    }

    public static void main(String[] args) {
        try {
            ManualPerformanceTest test = new ManualPerformanceTest();

            test.cleanup();

            List<PerformanceResult> results = new ArrayList<>();

            results.add(test.testCreateOperations());
            results.add(test.testReadOperations());
            results.add(test.testUpdateOperations());
            results.add(test.testSearchOperations());
            results.add(test.testDeleteOperations());

            System.out.println("\n===== MANUAL JDBC PERFORMANCE RESULTS =====");
            System.out.println("===========================================");
            System.out.printf("%-10s %-12s %-12s %-15s%n",
                    "Operation", "Count", "Time(ms)", "Ops/ms");
            System.out.println("--------------------------------------------");

            for (PerformanceResult result : results) {
                System.out.printf("%-10s %-12d %-12d %-15.2f%n",
                        result.getOperation(),
                        result.getRecordCount(),
                        result.getTimeMs(),
                        result.getOperationsPerMs());
            }

            test.cleanup();
            test.close();

        } catch (SQLException e) {
            logger.severe("Performance test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static class PerformanceResult {
        private String operation;
        private int recordCount;
        private long timeMs;

        public PerformanceResult(String operation, int recordCount, long timeMs) {
            this.operation = operation;
            this.recordCount = recordCount;
            this.timeMs = timeMs;
        }

        public String getOperation() { return operation; }
        public int getRecordCount() { return recordCount; }
        public long getTimeMs() { return timeMs; }
        public double getOperationsPerMs() {
            return timeMs > 0 ? (double) recordCount / timeMs : 0;
        }
    }
}