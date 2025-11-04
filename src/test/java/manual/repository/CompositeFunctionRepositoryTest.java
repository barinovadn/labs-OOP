package manual.repository;

import manual.DatabaseConnection;
import manual.dto.CompositeFunctionDto;
import manual.dto.FunctionDto;
import manual.dto.UserDto;
import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CompositeFunctionRepositoryTest {
    private Connection connection;
    private UserRepository userRepository;
    private FunctionRepository functionRepository;
    private CompositeFunctionRepository compositeRepository;
    private Long testUserId;
    private Long firstFunctionId;
    private Long secondFunctionId;

    @BeforeAll
    void setup() throws SQLException {
        connection = DatabaseConnection.getConnection();
        userRepository = new UserRepository(connection);
        functionRepository = new FunctionRepository(connection);
        compositeRepository = new CompositeFunctionRepository(connection);
    }

    @BeforeEach
    void cleanupBeforeTest() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM computed_points");
            stmt.execute("DELETE FROM composite_functions");
            stmt.execute("DELETE FROM functions");
            stmt.execute("DELETE FROM users");
        }

        UserDto user = new UserDto(null, "comptestuser", "pass", "comptest@email.com", null);
        testUserId = userRepository.create(user);

        FunctionDto func1 = new FunctionDto(null, testUserId, "f", "SQR", "x*x", 0.0, 10.0, null);
        FunctionDto func2 = new FunctionDto(null, testUserId, "g", "LINEAR", "x+1", 0.0, 10.0, null);
        firstFunctionId = functionRepository.create(func1);
        secondFunctionId = functionRepository.create(func2);
    }

    @AfterAll
    void cleanup() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    @Test
    void testCreateAndFindCompositeFunction() throws SQLException {
        CompositeFunctionDto composite = new CompositeFunctionDto(null, testUserId, "f(g(x))", firstFunctionId, secondFunctionId, null);

        Long compositeId = compositeRepository.create(composite);
        assertNotNull(compositeId);

        CompositeFunctionDto found = compositeRepository.findById(compositeId);
        assertNotNull(found);
        assertEquals("f(g(x))", found.getCompositeName());
        assertEquals(firstFunctionId, found.getFirstFunctionId());
        assertEquals(secondFunctionId, found.getSecondFunctionId());
    }

    @Test
    void testFindCompositesByUserId() throws SQLException {
        CompositeFunctionDto comp1 = new CompositeFunctionDto(null, testUserId, "comp1", firstFunctionId, secondFunctionId, null);
        CompositeFunctionDto comp2 = new CompositeFunctionDto(null, testUserId, "comp2", secondFunctionId, firstFunctionId, null);

        compositeRepository.create(comp1);
        compositeRepository.create(comp2);

        List<CompositeFunctionDto> composites = compositeRepository.findByUserId(testUserId);
        assertEquals(2, composites.size());
    }

    @Test
    void testUpdateCompositeFunction() throws SQLException {
        CompositeFunctionDto composite = new CompositeFunctionDto(null, testUserId, "original", firstFunctionId, secondFunctionId, null);
        Long compositeId = compositeRepository.create(composite);

        CompositeFunctionDto updated = new CompositeFunctionDto(compositeId, testUserId, "updated", secondFunctionId, firstFunctionId, null);
        boolean success = compositeRepository.update(updated);
        assertTrue(success);

        CompositeFunctionDto found = compositeRepository.findById(compositeId);
        assertEquals("updated", found.getCompositeName());
        assertEquals(secondFunctionId, found.getFirstFunctionId());
        assertEquals(firstFunctionId, found.getSecondFunctionId());
    }

    @Test
    void testDeleteCompositeFunction() throws SQLException {
        CompositeFunctionDto composite = new CompositeFunctionDto(null, testUserId, "todelete", firstFunctionId, secondFunctionId, null);
        Long compositeId = compositeRepository.create(composite);

        boolean deleted = compositeRepository.delete(compositeId);
        assertTrue(deleted);

        CompositeFunctionDto found = compositeRepository.findById(compositeId);
        assertNull(found);
    }
}