package performance;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.sql.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class FrameworkPerformanceTest {

    private static final Logger logger = Logger.getLogger(FrameworkPerformanceTest.class.getName());
    private static Connection connection;

    @BeforeAll
    static void setup() throws SQLException {
        logger.info("Инициализация тестов производительности Framework");
        connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/lab5_oop",
                "postgres",
                "admin"
        );
    }

    @Test
    void testSelectPerformance() throws SQLException {
        logger.info("Начало теста SELECT производительности");

        long startTime = System.nanoTime();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM users WHERE username LIKE ?");

        for (int i = 0; i < 1000; i++) {
            stmt.setString(1, "%user%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) { }
            rs.close();
        }
        stmt.close();

        long time = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);
        logger.info("Framework SELECT: " + time + "ms");
    }

    @Test
    void testInsertPerformance() throws SQLException {
        logger.info("Начало теста INSERT производительности");

        long startTime = System.nanoTime();
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO users (username, password, email) VALUES (?, ?, ?)");

        for (int i = 0; i < 1000; i++) {
            stmt.setString(1, "testuser_fw_" + System.currentTimeMillis() + "_" + i);
            stmt.setString(2, "password");
            stmt.setString(3, "email_fw_" + System.currentTimeMillis() + "_" + i + "@test.com");
            stmt.executeUpdate();
        }
        stmt.close();

        long time = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);
        logger.info("Framework INSERT: " + time + "ms");
    }

    @Test
    void testJoinPerformance() throws SQLException {
        logger.info("Начало теста JOIN производительности");

        long startTime = System.nanoTime();
        String sql = "SELECT u.username, f.function_name, COUNT(p.point_id) as point_count " +
                "FROM users u JOIN functions f ON u.user_id = f.user_id " +
                "LEFT JOIN computed_points p ON f.function_id = p.function_id " +
                "WHERE u.username LIKE ? GROUP BY u.username, f.function_name";

        PreparedStatement stmt = connection.prepareStatement(sql);
        for (int i = 0; i < 100; i++) {
            stmt.setString(1, "%user%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) { }
            rs.close();
        }
        stmt.close();

        long time = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);
        logger.info("Framework JOIN: " + time + "ms");
    }

    @Test
    void testUpdatePerformance() throws SQLException {
        logger.info("Начало теста UPDATE производительности");

        long startTime = System.nanoTime();
        PreparedStatement stmt = connection.prepareStatement("UPDATE users SET password = ? WHERE user_id = ?");
        PreparedStatement selectStmt = connection.prepareStatement("SELECT user_id FROM users LIMIT 500");
        ResultSet rs = selectStmt.executeQuery();

        int count = 0;
        while (rs.next() && count < 500) {
            int userId = rs.getInt("user_id");
            stmt.setString(1, "updated_pass_" + System.currentTimeMillis() + "_" + count);
            stmt.setInt(2, userId);
            stmt.addBatch();
            count++;
        }

        stmt.executeBatch();
        stmt.close();
        selectStmt.close();
        rs.close();

        long time = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);
        logger.info("Framework UPDATE: " + time + "ms");
    }
}