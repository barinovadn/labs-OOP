package manual.repository;

import manual.DatabaseConnection;
import manual.dto.CreateUserRequest;
import manual.dto.UserResponse;
import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserRepositoryTest {
    private Connection connection;
    private UserRepository userRepository;

    @BeforeAll
    void setup() throws SQLException {
        connection = DatabaseConnection.getConnection();
        userRepository = new UserRepository(connection);
    }

    @BeforeEach
    void cleanupBeforeTest() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM computed_points");
            stmt.execute("DELETE FROM composite_functions");
            stmt.execute("DELETE FROM functions");
            stmt.execute("DELETE FROM users");
        }
    }

    @AfterAll
    void cleanup() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    @Test
    void testCreateAndFindUser() throws SQLException {
        CreateUserRequest request = new CreateUserRequest("testuser1", "password123", "test1@email.com");

        UserResponse response = userRepository.create(request);
        assertNotNull(response);
        assertNotNull(response.getUserId());
        assertEquals("testuser1", response.getUsername());
        assertEquals("test1@email.com", response.getEmail());
    }

    @Test
    void testFindAllUsers() throws SQLException {
        CreateUserRequest user1 = new CreateUserRequest("userA", "passA", "a@test.com");
        CreateUserRequest user2 = new CreateUserRequest("userB", "passB", "b@test.com");

        userRepository.create(user1);
        userRepository.create(user2);

        List<UserResponse> users = userRepository.findAll();
        assertEquals(2, users.size());
    }

    @Test
    void testUpdateUser() throws SQLException {
        CreateUserRequest request = new CreateUserRequest("original", "pass", "original@test.com");
        UserResponse created = userRepository.create(request);

        CreateUserRequest updateRequest = new CreateUserRequest("updated", "newpass", "updated@test.com");
        UserResponse updated = userRepository.update(created.getUserId(), updateRequest);

        assertNotNull(updated);
        assertEquals("updated", updated.getUsername());
        assertEquals("updated@test.com", updated.getEmail());
    }

    @Test
    void testDeleteUser() throws SQLException {
        CreateUserRequest request = new CreateUserRequest("todelete", "pass", "delete@test.com");
        UserResponse created = userRepository.create(request);

        boolean deleted = userRepository.delete(created.getUserId());
        assertTrue(deleted);

        UserResponse found = userRepository.findById(created.getUserId());
        assertNull(found);
    }
}