package manual.repository;

import manual.DatabaseConnection;
import manual.dto.FunctionDto;
import manual.dto.UserDto;
import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FunctionRepositoryTest {
    private Connection connection;
    private UserRepository userRepository;
    private FunctionRepository functionRepository;
    private Long testUserId;

    @BeforeAll
    void setup() throws SQLException {
        connection = DatabaseConnection.getConnection();
        userRepository = new UserRepository(connection);
        functionRepository = new FunctionRepository(connection);
    }

    @BeforeEach
    void cleanupBeforeTest() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM computed_points");
            stmt.execute("DELETE FROM composite_functions");
            stmt.execute("DELETE FROM functions");
            stmt.execute("DELETE FROM users");
        }

        UserDto user = new UserDto(null, "functestuser", "pass", "functest@email.com", null);
        testUserId = userRepository.create(user);
    }

    @AfterAll
    void cleanup() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    // тесты без изменений...
}