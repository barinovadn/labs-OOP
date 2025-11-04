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
        UserDto user = new UserDto(null, "testuser1", "password123", "test1@email.com", null);

        Long userId = userRepository.create(user);
        assertNotNull(userId);

        UserDto found = userRepository.findById(userId);
        assertNotNull(found);
        assertEquals("testuser1", found.getUsername());
        assertEquals("test1@email.com", found.getEmail());
    }

    // остальные тесты без изменений...
}