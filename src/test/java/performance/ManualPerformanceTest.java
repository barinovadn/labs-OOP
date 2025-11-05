package performance;

import manual.DatabaseConnection;
import manual.dto.CreateUserRequest;
import manual.dto.UserResponse;
import manual.repository.UserRepository;
import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ManualPerformanceTest {
    private static final Logger logger = Logger.getLogger(ManualPerformanceTest.class.getName());
    private static final int OPERATION_COUNT = 1000;

    private Connection connection;
    private UserRepository userRepository;
    private List<Long> userIds;

    @BeforeAll
    void setup() throws SQLException {
        connection = DatabaseConnection.getConnection();
        userRepository = new UserRepository(connection);
    }

    @BeforeEach
    void init() {
        userIds = new ArrayList<>();
    }

    @AfterEach
    void cleanup() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM computed_points");
            stmt.execute("DELETE FROM composite_functions");
            stmt.execute("DELETE FROM functions");
            stmt.execute("DELETE FROM users");
        }
    }

    @AfterAll
    void close() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    @Test
    void testCreateOperations() throws SQLException {
        logger.info("Starting CREATE operations test");
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < OPERATION_COUNT; i++) {
            CreateUserRequest user = new CreateUserRequest(
                    "user_" + i,
                    "password_" + i,
                    "user_" + i + "@test.com"
            );
            UserResponse response = userRepository.create(user);
            userIds.add(response.getUserId());
        }

        long totalTime = System.currentTimeMillis() - startTime;
        logger.info("CREATE operations completed in " + totalTime + "ms");
        assertTrue(totalTime < 10000);
    }

    @Test
    void testReadOperations() throws SQLException {
        logger.info("Starting READ operations test");

        for (int i = 0; i < OPERATION_COUNT; i++) {
            CreateUserRequest user = new CreateUserRequest("user_" + i, "pass", "user_" + i + "@test.com");
            UserResponse response = userRepository.create(user);
            userIds.add(response.getUserId());
        }

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < OPERATION_COUNT; i++) {
            UserResponse user = userRepository.findById(userIds.get(i));
            assertNotNull(user);
        }

        long totalTime = System.currentTimeMillis() - startTime;
        logger.info("READ operations completed in " + totalTime + "ms");
        assertTrue(totalTime < 5000);
    }

    @Test
    void testUpdateOperations() throws SQLException {
        logger.info("Starting UPDATE operations test");

        for (int i = 0; i < OPERATION_COUNT; i++) {
            CreateUserRequest user = new CreateUserRequest("user_" + i, "pass", "user_" + i + "@test.com");
            UserResponse response = userRepository.create(user);
            userIds.add(response.getUserId());
        }

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < OPERATION_COUNT; i++) {
            CreateUserRequest user = new CreateUserRequest(
                    "updated_user_" + i,
                    "updated_password_" + i,
                    "updated_user_" + i + "@test.com"
            );
            UserResponse updated = userRepository.update(userIds.get(i), user);
            assertNotNull(updated);
        }

        long totalTime = System.currentTimeMillis() - startTime;
        logger.info("UPDATE operations completed in " + totalTime + "ms");
        assertTrue(totalTime < 5000);
    }

    @Test
    void testDeleteOperations() throws SQLException {
        logger.info("Starting DELETE operations test");

        for (int i = 0; i < OPERATION_COUNT; i++) {
            CreateUserRequest user = new CreateUserRequest("user_" + i, "pass", "user_" + i + "@test.com");
            UserResponse response = userRepository.create(user);
            userIds.add(response.getUserId());
        }

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < OPERATION_COUNT; i++) {
            boolean deleted = userRepository.delete(userIds.get(i));
            assertTrue(deleted);
        }

        long totalTime = System.currentTimeMillis() - startTime;
        logger.info("DELETE operations completed in " + totalTime + "ms");
        assertTrue(totalTime < 5000);
    }

    @Test
    void testSearchOperations() throws SQLException {
        logger.info("Starting SEARCH operations test");

        for (int i = 0; i < 100; i++) {
            CreateUserRequest user = new CreateUserRequest("user_" + i, "pass", "user_" + i + "@test.com");
            userRepository.create(user);
        }

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            List<UserResponse> users = userRepository.findAll();
            assertFalse(users.isEmpty());
        }

        long totalTime = System.currentTimeMillis() - startTime;
        logger.info("SEARCH operations completed in " + totalTime + "ms");
        assertTrue(totalTime < 3000);
    }
}