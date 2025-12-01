package performance;

import org.junit.jupiter.api.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.logging.Logger;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FrameworkPerformanceTest {
    private static final Logger logger = Logger.getLogger(FrameworkPerformanceTest.class.getName());
    private static final int OPERATION_COUNT = 10000;

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/labs_oop",
                "postgres",
                "admin"
        );
    }

    @BeforeEach
    void cleanupBeforeEachTest() throws SQLException {
        cleanupDatabase();
    }

    private void cleanupDatabase() throws SQLException {
        try (Connection cleanupConn = getConnection();
             Statement stmt = cleanupConn.createStatement()) {
            cleanupConn.setAutoCommit(true);
            stmt.execute("DELETE FROM computed_points");
            stmt.execute("DELETE FROM composite_functions");
            stmt.execute("DELETE FROM functions");
            stmt.execute("DELETE FROM users");
        }
    }

    private void createTestUsers(Connection connection) throws SQLException {
        String userSql = "INSERT INTO users (username, password, email, created_at) VALUES (?, ?, ?, ?)";
        PreparedStatement userStmt = connection.prepareStatement(userSql);
        for (int i = 0; i < OPERATION_COUNT; i++) {
            userStmt.setString(1, "fw_user_" + i);
            userStmt.setString(2, "password_" + i);
            userStmt.setString(3, "fw_user_" + i + "@test.com");
            userStmt.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            userStmt.addBatch();
            if (i % 1000 == 0) userStmt.executeBatch();
        }
        userStmt.executeBatch();
        userStmt.close();
    }

    @Test
    @Order(1)
    void testCreatePerformance() throws SQLException {
        Connection connection = getConnection();
        long startTime = System.currentTimeMillis();

        createTestUsers(connection);

        long time = System.currentTimeMillis() - startTime;
        System.out.println("Framework CREATE: " + time + "ms (10000 operations)");

        connection.close();
    }

    @Test
    @Order(2)
    void testReadPerformance() throws SQLException {
        Connection connection = getConnection();
        createTestUsers(connection);
        long startTime = System.currentTimeMillis();

        String userSql = "SELECT * FROM users WHERE user_id = ?";
        PreparedStatement userStmt = connection.prepareStatement(userSql);
        for (int i = 0; i < OPERATION_COUNT; i++) {
            userStmt.setInt(1, i + 1);
            ResultSet rs = userStmt.executeQuery();
            if (rs.next()) {}
            rs.close();
        }
        userStmt.close();

        long time = System.currentTimeMillis() - startTime;
        System.out.println("Framework READ: " + time + "ms (10000 operations)");

        connection.close();
    }

    @Test
    @Order(3)
    void testUpdatePerformance() throws SQLException {
        Connection connection = getConnection();
        createTestUsers(connection);
        long startTime = System.currentTimeMillis();

        String userSql = "UPDATE users SET password = ? WHERE user_id = ?";
        PreparedStatement userStmt = connection.prepareStatement(userSql);
        for (int i = 0; i < OPERATION_COUNT; i++) {
            userStmt.setString(1, "updated_user_" + i);
            userStmt.setInt(2, i + 1);
            userStmt.addBatch();
            if (i % 1000 == 0) userStmt.executeBatch();
        }
        userStmt.executeBatch();
        userStmt.close();

        long time = System.currentTimeMillis() - startTime;
        System.out.println("Framework UPDATE: " + time + "ms (10000 operations)");

        connection.close();
    }

    @Test
    @Order(4)
    void testSearchPerformance() throws SQLException {
        Connection connection = getConnection();
        createTestUsers(connection);
        long startTime = System.currentTimeMillis();

        String searchSql = "SELECT * FROM users WHERE username LIKE ?";
        PreparedStatement searchStmt = connection.prepareStatement(searchSql);

        for (int i = 0; i < 1000; i++) {
            searchStmt.setString(1, "%fw_user%");
            ResultSet rs = searchStmt.executeQuery();
            while (rs.next()) {}
            rs.close();
        }
        searchStmt.close();

        long time = System.currentTimeMillis() - startTime;
        System.out.println("Framework SEARCH: " + time + "ms (1000 operations)");

        connection.close();
    }

    @Test
    @Order(5)
    void testDeletePerformance() throws SQLException {
        Connection connection = getConnection();
        createTestUsers(connection);
        long startTime = System.currentTimeMillis();

        String userSql = "DELETE FROM users WHERE user_id = ?";
        PreparedStatement userStmt = connection.prepareStatement(userSql);
        for (int i = 0; i < OPERATION_COUNT; i++) {
            userStmt.setInt(1, i + 1);
            userStmt.addBatch();
            if (i % 1000 == 0) userStmt.executeBatch();
        }
        userStmt.executeBatch();
        userStmt.close();

        long time = System.currentTimeMillis() - startTime;
        System.out.println("Framework DELETE: " + time + "ms (10000 operations)");

        connection.close();
    }
}