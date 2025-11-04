package manual.repository;

import manual.DatabaseConnection;
import manual.dto.FunctionDto;
import manual.dto.PointDto;
import manual.dto.UserDto;
import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PointRepositoryTest {
    private Connection connection;
    private UserRepository userRepository;
    private FunctionRepository functionRepository;
    private PointRepository pointRepository;
    private Long testUserId;
    private Long testFunctionId;

    @BeforeAll
    void setup() throws SQLException {
        connection = DatabaseConnection.getConnection();
        userRepository = new UserRepository(connection);
        functionRepository = new FunctionRepository(connection);
        pointRepository = new PointRepository(connection);
    }

    @BeforeEach
    void cleanupBeforeTest() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM computed_points");
            stmt.execute("DELETE FROM composite_functions");
            stmt.execute("DELETE FROM functions");
            stmt.execute("DELETE FROM users");
        }

        UserDto user = new UserDto(null, "pointtestuser", "pass", "pointtest@email.com", null);
        testUserId = userRepository.create(user);

        FunctionDto function = new FunctionDto(null, testUserId, "testfunc", "SQR", "x*x", 0.0, 10.0, null);
        testFunctionId = functionRepository.create(function);
    }

    @AfterAll
    void cleanup() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    @Test
    void testCreateAndFindPoint() throws SQLException {
        PointDto point = new PointDto(null, testFunctionId, 2.0, 4.0, null);

        Long pointId = pointRepository.create(point);
        assertNotNull(pointId);

        PointDto found = pointRepository.findById(pointId);
        assertNotNull(found);
        assertEquals(2.0, found.getXValue());
        assertEquals(4.0, found.getYValue());
        assertEquals(testFunctionId, found.getFunctionId());
    }

    @Test
    void testFindPointsByFunctionId() throws SQLException {
        PointDto point1 = new PointDto(null, testFunctionId, 1.0, 1.0, null);
        PointDto point2 = new PointDto(null, testFunctionId, 3.0, 9.0, null);

        pointRepository.create(point1);
        pointRepository.create(point2);

        List<PointDto> points = pointRepository.findByFunctionId(testFunctionId);
        assertEquals(2, points.size());
    }

    @Test
    void testUpdatePoint() throws SQLException {
        PointDto point = new PointDto(null, testFunctionId, 5.0, 25.0, null);
        Long pointId = pointRepository.create(point);

        PointDto updated = new PointDto(pointId, testFunctionId, 6.0, 36.0, null);
        boolean success = pointRepository.update(updated);
        assertTrue(success);

        PointDto found = pointRepository.findById(pointId);
        assertEquals(6.0, found.getXValue());
        assertEquals(36.0, found.getYValue());
    }

    @Test
    void testDeletePoint() throws SQLException {
        PointDto point = new PointDto(null, testFunctionId, 7.0, 49.0, null);
        Long pointId = pointRepository.create(point);

        boolean deleted = pointRepository.delete(pointId);
        assertTrue(deleted);

        PointDto found = pointRepository.findById(pointId);
        assertNull(found);
    }
}