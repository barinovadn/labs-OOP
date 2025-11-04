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
class FunctionRepositoryCoverageTest {
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
    void cleanup() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM computed_points");
            stmt.execute("DELETE FROM composite_functions");
            stmt.execute("DELETE FROM functions");
            stmt.execute("DELETE FROM users");
        }

        UserDto user = new UserDto(null, "coverageuser", "pass", "coverage@email.com", null);
        testUserId = userRepository.create(user);
    }

    @AfterAll
    void cleanupAll() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    @Test
    void testFunctionDtoEmptyConstructor() {
        FunctionDto function = new FunctionDto();
        assertNull(function.getFunctionId());
        assertNull(function.getUserId());
        assertNull(function.getFunctionName());
        assertNull(function.getFunctionType());
        assertNull(function.getFunctionExpression());
        assertNull(function.getXFrom());
        assertNull(function.getXTo());
        assertNull(function.getCreatedAt());
    }

    @Test
    void testFunctionDtoSetters() {
        FunctionDto function = new FunctionDto();
        function.setFunctionId(1L);
        function.setUserId(2L);
        function.setFunctionName("test");
        function.setFunctionType("TYPE");
        function.setFunctionExpression("x");
        function.setXFrom(0.0);
        function.setXTo(10.0);

        assertEquals(1L, function.getFunctionId());
        assertEquals(2L, function.getUserId());
        assertEquals("test", function.getFunctionName());
        assertEquals("TYPE", function.getFunctionType());
        assertEquals("x", function.getFunctionExpression());
        assertEquals(0.0, function.getXFrom());
        assertEquals(10.0, function.getXTo());
    }

    @Test
    void testFindByUserIdEmpty() throws SQLException {
        List<FunctionDto> functions = functionRepository.findByUserId(testUserId);
        assertTrue(functions.isEmpty());
    }

    @Test
    void testFindByIdNonExistent() throws SQLException {
        FunctionDto function = functionRepository.findById(99999L);
        assertNull(function);
    }

    @Test
    void testUpdateNonExistentFunction() throws SQLException {
        FunctionDto function = new FunctionDto(99999L, testUserId, "nonexistent", "TYPE", "x", 0.0, 1.0, null);
        boolean result = functionRepository.update(function);
        assertFalse(result);
    }

    @Test
    void testDeleteNonExistentFunction() throws SQLException {
        boolean result = functionRepository.delete(99999L);
        assertFalse(result);
    }

    @Test
    void testMapToDtoFullCoverage() throws SQLException {
        FunctionDto original = new FunctionDto(null, testUserId, "fullcoverage", "TYPE", "2*x", -5.0, 5.0, null);
        Long functionId = functionRepository.create(original);

        FunctionDto found = functionRepository.findById(functionId);
        assertNotNull(found);
        assertEquals("fullcoverage", found.getFunctionName());
        assertEquals("TYPE", found.getFunctionType());
        assertEquals("2*x", found.getFunctionExpression());
        assertEquals(-5.0, found.getXFrom());
        assertEquals(5.0, found.getXTo());
        assertNotNull(found.getCreatedAt());
    }
}