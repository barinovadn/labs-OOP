package manual.repository;

import manual.DatabaseConnection;
import manual.dto.CreateUserRequest;
import manual.dto.CreateFunctionRequest;
import manual.dto.CreatePointRequest;
import manual.dto.CreateCompositeFunctionRequest;
import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RepositoryEdgeCasesTest {
    private Connection connection;
    private UserRepository userRepository;
    private FunctionRepository functionRepository;
    private PointRepository pointRepository;
    private CompositeFunctionRepository compositeRepository;

    @BeforeAll
    void setup() throws SQLException {
        connection = DatabaseConnection.getConnection();
        userRepository = new UserRepository(connection);
        functionRepository = new FunctionRepository(connection);
        pointRepository = new PointRepository(connection);
        compositeRepository = new CompositeFunctionRepository(connection);
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
    void testUserRepositoryFindNonExistent() throws SQLException {
        assertNull(userRepository.findById(999999L));
    }

    @Test
    void testFunctionRepositoryEmptyResult() throws SQLException {
        assertTrue(functionRepository.findByUserId(999999L).isEmpty());
    }

    @Test
    void testPointRepositoryEmptyFind() throws SQLException {
        assertTrue(pointRepository.findByFunctionId(999999L).isEmpty());
    }

    @Test
    void testCompositeRepositoryNoResults() throws SQLException {
        assertTrue(compositeRepository.findByUserId(999999L).isEmpty());
    }

    @Test
    void testUpdateNonExistentUser() throws SQLException {
        CreateUserRequest request = new CreateUserRequest("test", "pass", "test@test.com");
        assertNull(userRepository.update(999999L, request));
    }

    @Test
    void testDeleteNonExistent() throws SQLException {
        assertFalse(userRepository.delete(999999L));
        assertFalse(functionRepository.delete(999999L));
        assertFalse(pointRepository.delete(999999L));
        assertFalse(compositeRepository.delete(999999L));
    }

    @Test
    void testCreateUserWithNullFields() throws SQLException {
        CreateUserRequest request = new CreateUserRequest(null, null, null);
        assertThrows(SQLException.class, () -> userRepository.create(request));
    }
}