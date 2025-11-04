package manual.repository;

import manual.DatabaseConnection;
import manual.dto.UserDto;
import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserRepositoryCoverageTest {
    private Connection connection;
    private UserRepository userRepository;

    @BeforeAll
    void setup() throws SQLException {
        connection = DatabaseConnection.getConnection();
        userRepository = new UserRepository(connection);
    }

    @BeforeEach
    void cleanup() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM computed_points");
            stmt.execute("DELETE FROM composite_functions");
            stmt.execute("DELETE FROM functions");
            stmt.execute("DELETE FROM users");
        }
    }

    @AfterAll
    void cleanupAll() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    @Test
    void testEmptyConstructor() {
        UserDto user = new UserDto();
        assertNull(user.getUserId());
        assertNull(user.getUsername());
        assertNull(user.getPassword());
        assertNull(user.getEmail());
        assertNull(user.getCreatedAt());
    }

    @Test
    void testFindAllEmpty() throws SQLException {
        List<UserDto> users = userRepository.findAll();
        assertTrue(users.isEmpty());
    }

    @Test
    void testFindAllWithData() throws SQLException {
        UserDto user1 = new UserDto(null, "user1", "pass1", "email1@test.com", null);
        UserDto user2 = new UserDto(null, "user2", "pass2", "email2@test.com", null);

        userRepository.create(user1);
        userRepository.create(user2);

        List<UserDto> users = userRepository.findAll();
        assertEquals(2, users.size());
    }

    @Test
    void testUpdateNonExistentUser() throws SQLException {
        UserDto user = new UserDto(99999L, "nonexistent", "pass", "none@test.com", null);
        boolean result = userRepository.update(user);
        assertFalse(result);
    }

    @Test
    void testDeleteNonExistentUser() throws SQLException {
        boolean result = userRepository.delete(99999L);
        assertFalse(result);
    }

    @Test
    void testFindByIdNonExistent() throws SQLException {
        UserDto user = userRepository.findById(99999L);
        assertNull(user);
    }

    @Test
    void testUserDtoSetters() {
        UserDto user = new UserDto();
        user.setUserId(1L);
        user.setUsername("testuser");
        user.setPassword("testpass");
        user.setEmail("test@email.com");

        assertEquals(1L, user.getUserId());
        assertEquals("testuser", user.getUsername());
        assertEquals("testpass", user.getPassword());
        assertEquals("test@email.com", user.getEmail());
    }
}