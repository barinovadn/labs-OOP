package manual.repository;

import manual.DatabaseConnection;
import manual.entity.UserEntity;
import manual.entity.FunctionEntity;
import manual.dto.CreateUserRequest;
import manual.dto.CreateFunctionRequest;
import manual.dto.CreateCompositeFunctionRequest;
import manual.dto.UserResponse;
import manual.dto.FunctionResponse;
import manual.dto.CompositeFunctionResponse;
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
    private UserEntity testUser;
    private FunctionEntity firstFunction;
    private FunctionEntity secondFunction;

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

        CreateUserRequest userRequest = new CreateUserRequest("comptestuser", "pass", "comptest@email.com");
        UserResponse userResponse = userRepository.create(userRequest);

        testUser = new UserEntity("comptestuser", "pass", "comptest@email.com");
        testUser.setUserId(userResponse.getUserId());
        testUser.setCreatedAt(userResponse.getCreatedAt());

        CreateFunctionRequest func1 = new CreateFunctionRequest(testUser.getUserId(), "f", "SQR", "x*x", 0.0, 10.0);
        CreateFunctionRequest func2 = new CreateFunctionRequest(testUser.getUserId(), "g", "LINEAR", "x+1", 0.0, 10.0);
        FunctionResponse func1Response = functionRepository.create(func1, testUser);
        FunctionResponse func2Response = functionRepository.create(func2, testUser);

        firstFunction = new FunctionEntity(testUser, "f", "SQR", "x*x", 0.0, 10.0);
        firstFunction.setFunctionId(func1Response.getFunctionId());
        firstFunction.setCreatedAt(func1Response.getCreatedAt());

        secondFunction = new FunctionEntity(testUser, "g", "LINEAR", "x+1", 0.0, 10.0);
        secondFunction.setFunctionId(func2Response.getFunctionId());
        secondFunction.setCreatedAt(func2Response.getCreatedAt());
    }

    @AfterAll
    void cleanup() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    @Test
    void testCreateAndFindCompositeFunction() throws SQLException {
        CreateCompositeFunctionRequest request = new CreateCompositeFunctionRequest(testUser.getUserId(), "f(g(x))", firstFunction.getFunctionId(), secondFunction.getFunctionId());

        CompositeFunctionResponse response = compositeRepository.create(request, testUser, firstFunction, secondFunction);
        assertNotNull(response);
        assertNotNull(response.getCompositeId());
        assertEquals("f(g(x))", response.getCompositeName());
    }

    @Test
    void testFindCompositesByUserId() throws SQLException {
        CreateCompositeFunctionRequest comp1 = new CreateCompositeFunctionRequest(testUser.getUserId(), "comp1", firstFunction.getFunctionId(), secondFunction.getFunctionId());
        CreateCompositeFunctionRequest comp2 = new CreateCompositeFunctionRequest(testUser.getUserId(), "comp2", secondFunction.getFunctionId(), firstFunction.getFunctionId());

        compositeRepository.create(comp1, testUser, firstFunction, secondFunction);
        compositeRepository.create(comp2, testUser, secondFunction, firstFunction);

        List<CompositeFunctionResponse> composites = compositeRepository.findByUserId(testUser.getUserId());
        assertEquals(2, composites.size());
    }
}