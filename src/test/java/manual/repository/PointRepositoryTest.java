package manual.repository;

import manual.DatabaseConnection;
import manual.entity.UserEntity;
import manual.entity.FunctionEntity;
import manual.dto.CreateUserRequest;
import manual.dto.CreateFunctionRequest;
import manual.dto.CreatePointRequest;
import manual.dto.UserResponse;
import manual.dto.FunctionResponse;
import manual.dto.PointResponse;
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
    private UserEntity testUser;
    private FunctionEntity testFunction;

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

        CreateUserRequest userRequest = new CreateUserRequest("pointtestuser", "pass", "pointtest@email.com");
        UserResponse userResponse = userRepository.create(userRequest);

        testUser = new UserEntity("pointtestuser", "pass", "pointtest@email.com");
        testUser.setUserId(userResponse.getUserId());
        testUser.setCreatedAt(userResponse.getCreatedAt());

        CreateFunctionRequest funcRequest = new CreateFunctionRequest(testUser.getUserId(), "testfunc", "SQR", "x*x", 0.0, 10.0);
        FunctionResponse functionResponse = functionRepository.create(funcRequest, testUser);

        testFunction = new FunctionEntity(testUser, "testfunc", "SQR", "x*x", 0.0, 10.0);
        testFunction.setFunctionId(functionResponse.getFunctionId());
        testFunction.setCreatedAt(functionResponse.getCreatedAt());
    }

    @AfterAll
    void cleanup() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    @Test
    void testCreateAndFindPoint() throws SQLException {
        CreatePointRequest request = new CreatePointRequest(testFunction.getFunctionId(), 2.0, 4.0);

        PointResponse response = pointRepository.create(request, testFunction);
        assertNotNull(response);
        assertNotNull(response.getPointId());
        assertEquals(2.0, response.getXValue());
        assertEquals(4.0, response.getYValue());
    }

    @Test
    void testFindPointsByFunctionId() throws SQLException {
        CreatePointRequest point1 = new CreatePointRequest(testFunction.getFunctionId(), 1.0, 1.0);
        CreatePointRequest point2 = new CreatePointRequest(testFunction.getFunctionId(), 3.0, 9.0);

        pointRepository.create(point1, testFunction);
        pointRepository.create(point2, testFunction);

        List<PointResponse> points = pointRepository.findByFunctionId(testFunction.getFunctionId());
        assertEquals(2, points.size());
    }
}