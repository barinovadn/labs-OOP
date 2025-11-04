package manual.repository;

import manual.DatabaseConnection;
import manual.dto.*;
import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EdgeCaseTests {
    private Connection connection;
    private UserRepository userRepository;
    private FunctionRepository functionRepository;
    private PointRepository pointRepository;
    private CompositeFunctionRepository compositeRepository;
    private Long testUserId;
    private Long testFunctionId;

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

        UserDto user = new UserDto(null, "edgeuser", "pass", "edge@email.com", null);
        testUserId = userRepository.create(user);

        FunctionDto function = new FunctionDto(null, testUserId, "edgefunc", "TYPE", "x", 0.0, 5.0, null);
        testFunctionId = functionRepository.create(function);
    }

    @AfterAll
    void cleanupAll() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    @Test
    void testFunctionRepositoryWhileLoopCoverage() throws SQLException {
        FunctionDto func1 = new FunctionDto(null, testUserId, "func1", "TYPE1", "x", 0.0, 1.0, null);
        FunctionDto func2 = new FunctionDto(null, testUserId, "func2", "TYPE2", "2*x", 0.0, 2.0, null);
        FunctionDto func3 = new FunctionDto(null, testUserId, "func3", "TYPE3", "3*x", 0.0, 3.0, null);

        functionRepository.create(func1);
        functionRepository.create(func2);
        functionRepository.create(func3);

        List<FunctionDto> functions = functionRepository.findByUserId(testUserId);
        assertEquals(4, functions.size());
    }

    @Test
    void testPointRepositoryWhileLoopCoverage() throws SQLException {
        PointDto point1 = new PointDto(null, testFunctionId, 1.0, 1.0, null);
        PointDto point2 = new PointDto(null, testFunctionId, 2.0, 4.0, null);
        PointDto point3 = new PointDto(null, testFunctionId, 3.0, 9.0, null);

        pointRepository.create(point1);
        pointRepository.create(point2);
        pointRepository.create(point3);

        List<PointDto> points = pointRepository.findByFunctionId(testFunctionId);
        assertEquals(3, points.size());
    }

    @Test
    void testUserRepositoryWhileLoopCoverage() throws SQLException {
        UserDto user1 = new UserDto(null, "userA", "passA", "a@test.com", null);
        UserDto user2 = new UserDto(null, "userB", "passB", "b@test.com", null);
        UserDto user3 = new UserDto(null, "userC", "passC", "c@test.com", null);

        userRepository.create(user1);
        userRepository.create(user2);
        userRepository.create(user3);

        List<UserDto> users = userRepository.findAll();
        assertEquals(4, users.size());
    }

    @Test
    void testCompositeRepositoryWhileLoopCoverage() throws SQLException {
        FunctionDto secondFunc = new FunctionDto(null, testUserId, "second", "TYPE", "x+1", 0.0, 5.0, null);
        Long secondFunctionId = functionRepository.create(secondFunc);

        CompositeFunctionDto comp1 = new CompositeFunctionDto(null, testUserId, "comp1", testFunctionId, secondFunctionId, null);
        CompositeFunctionDto comp2 = new CompositeFunctionDto(null, testUserId, "comp2", secondFunctionId, testFunctionId, null);

        compositeRepository.create(comp1);
        compositeRepository.create(comp2);

        List<CompositeFunctionDto> composites = compositeRepository.findByUserId(testUserId);
        assertEquals(2, composites.size());
    }

    @Test
    void testReturnKeysNextFalse() throws SQLException {
        UserDto user = new UserDto(null, "keysuser", "pass", "keys@email.com", null);
        Long userId = userRepository.create(user);
        assertNotNull(userId);
    }

    @Test
    void testExecuteUpdateReturnsZero() throws SQLException {
        UserDto user = new UserDto(99999L, "nonexistent", "pass", "none@email.com", null);
        boolean result = userRepository.update(user);
        assertFalse(result);
    }
}