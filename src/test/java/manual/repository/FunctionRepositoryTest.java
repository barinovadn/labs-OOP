package manual.repository;

import manual.DatabaseConnection;
import manual.entity.UserEntity;
import manual.dto.CreateUserRequest;
import manual.dto.CreateFunctionRequest;
import manual.dto.UserResponse;
import manual.dto.FunctionResponse;
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
    private UserEntity testUser;

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

        CreateUserRequest userRequest = new CreateUserRequest("functestuser", "pass", "functest@email.com");
        UserResponse userResponse = userRepository.create(userRequest);

        testUser = new UserEntity("functestuser", "pass", "functest@email.com");
        testUser.setUserId(userResponse.getUserId());
        testUser.setCreatedAt(userResponse.getCreatedAt());
    }

    @AfterAll
    void cleanup() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    @Test
    void testCreateAndFindFunction() throws SQLException {
        CreateFunctionRequest request = new CreateFunctionRequest(testUser.getUserId(), "testfunc", "SQR", "x*x", 0.0, 10.0);

        FunctionResponse response = functionRepository.create(request, testUser);
        assertNotNull(response);
        assertNotNull(response.getFunctionId());
        assertEquals("testfunc", response.getFunctionName());
        assertEquals("SQR", response.getFunctionType());
        assertEquals("x*x", response.getFunctionExpression());
    }

    @Test
    void testFindFunctionsByUserId() throws SQLException {
        CreateFunctionRequest func1 = new CreateFunctionRequest(testUser.getUserId(), "func1", "SQR", "x*x", 0.0, 5.0);
        CreateFunctionRequest func2 = new CreateFunctionRequest(testUser.getUserId(), "func2", "LINEAR", "2*x", 0.0, 5.0);

        functionRepository.create(func1, testUser);
        functionRepository.create(func2, testUser);

        List<FunctionResponse> functions = functionRepository.findByUserId(testUser.getUserId());
        assertEquals(2, functions.size());
    }
}