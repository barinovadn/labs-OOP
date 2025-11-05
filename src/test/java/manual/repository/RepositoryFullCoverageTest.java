package manual.repository;

import manual.DatabaseConnection;
import manual.entity.UserEntity;
import manual.entity.FunctionEntity;
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
class RepositoryFullCoverageTest {
    private Connection connection;
    private UserRepository userRepository;
    private FunctionRepository functionRepository;
    private PointRepository pointRepository;
    private CompositeFunctionRepository compositeRepository;
    private UserEntity testUser;
    private FunctionEntity testFunction;

    @BeforeAll
    void setup() throws SQLException {
        connection = DatabaseConnection.getConnection();
        userRepository = new UserRepository(connection);
        functionRepository = new FunctionRepository(connection);
        pointRepository = new PointRepository(connection);
        compositeRepository = new CompositeFunctionRepository(connection);
    }

    @BeforeEach
    void init() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM computed_points");
            stmt.execute("DELETE FROM composite_functions");
            stmt.execute("DELETE FROM functions");
            stmt.execute("DELETE FROM users");
        }

        CreateUserRequest userRequest = new CreateUserRequest("testuser", "pass", "test@test.com");
        var userResponse = userRepository.create(userRequest);

        testUser = new UserEntity("testuser", "pass", "test@test.com");
        testUser.setUserId(userResponse.getUserId());
        testUser.setCreatedAt(userResponse.getCreatedAt());

        CreateFunctionRequest funcRequest = new CreateFunctionRequest(testUser.getUserId(), "testfunc", "SQR", "x*x", 0.0, 10.0);
        var funcResponse = functionRepository.create(funcRequest, testUser);

        testFunction = new FunctionEntity(testUser, "testfunc", "SQR", "x*x", 0.0, 10.0);
        testFunction.setFunctionId(funcResponse.getFunctionId());
        testFunction.setCreatedAt(funcResponse.getCreatedAt());
    }

    @AfterAll
    void cleanup() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    @Test
    void testPointUpdateSuccess() throws SQLException {
        CreatePointRequest createRequest = new CreatePointRequest(testFunction.getFunctionId(), 1.0, 1.0);
        var createdPoint = pointRepository.create(createRequest, testFunction);

        CreatePointRequest updateRequest = new CreatePointRequest(testFunction.getFunctionId(), 2.0, 4.0);
        var updatedPoint = pointRepository.update(createdPoint.getPointId(), updateRequest);

        assertNotNull(updatedPoint);
        assertEquals(2.0, updatedPoint.getXValue());
        assertEquals(4.0, updatedPoint.getYValue());
    }

    @Test
    void testPointUpdateFailure() throws SQLException {
        CreatePointRequest updateRequest = new CreatePointRequest(testFunction.getFunctionId(), 2.0, 4.0);
        var result = pointRepository.update(999999L, updateRequest);
        assertNull(result);
    }

    @Test
    void testFunctionUpdateSuccess() throws SQLException {
        CreateFunctionRequest updateRequest = new CreateFunctionRequest(testUser.getUserId(), "updated", "LINEAR", "2*x", 0.0, 5.0);
        var updatedFunction = functionRepository.update(testFunction.getFunctionId(), updateRequest);

        assertNotNull(updatedFunction);
        assertEquals("updated", updatedFunction.getFunctionName());
        assertEquals("LINEAR", updatedFunction.getFunctionType());
    }

    @Test
    void testFunctionUpdateFailure() throws SQLException {
        CreateFunctionRequest updateRequest = new CreateFunctionRequest(testUser.getUserId(), "updated", "LINEAR", "2*x", 0.0, 5.0);
        var result = functionRepository.update(999999L, updateRequest);
        assertNull(result);
    }

    @Test
    void testFindAllFunctions() throws SQLException {
        CreateFunctionRequest func1 = new CreateFunctionRequest(testUser.getUserId(), "func1", "SQR", "x*x", 0.0, 5.0);
        CreateFunctionRequest func2 = new CreateFunctionRequest(testUser.getUserId(), "func2", "LINEAR", "2*x", 0.0, 5.0);

        functionRepository.create(func1, testUser);
        functionRepository.create(func2, testUser);

        var functions = functionRepository.findAll();
        assertTrue(functions.size() >= 3);
    }

    @Test
    void testPointFindByIdNotFound() throws SQLException {
        var result = pointRepository.findById(999999L);
        assertNull(result);
    }

    @Test
    void testCompositeFunctionUpdate() throws SQLException {
        CreateFunctionRequest secondFunc = new CreateFunctionRequest(testUser.getUserId(), "g", "LINEAR", "x+1", 0.0, 10.0);
        var secondFuncResponse = functionRepository.create(secondFunc, testUser);

        var secondFunction = new FunctionEntity(testUser, "g", "LINEAR", "x+1", 0.0, 10.0);
        secondFunction.setFunctionId(secondFuncResponse.getFunctionId());

        CreateCompositeFunctionRequest createRequest = new CreateCompositeFunctionRequest(testUser.getUserId(), "original", testFunction.getFunctionId(), secondFunction.getFunctionId());
        var createdComposite = compositeRepository.create(createRequest, testUser, testFunction, secondFunction);

        CreateCompositeFunctionRequest updateRequest = new CreateCompositeFunctionRequest(testUser.getUserId(), "updated", secondFunction.getFunctionId(), testFunction.getFunctionId());
        var updatedComposite = compositeRepository.update(createdComposite.getCompositeId(), updateRequest);

        assertNotNull(updatedComposite);
        assertEquals("updated", updatedComposite.getCompositeName());
    }

    @Test
    void testCompositeFunctionUpdateFailure() throws SQLException {
        CreateCompositeFunctionRequest updateRequest = new CreateCompositeFunctionRequest(testUser.getUserId(), "updated", 1L, 2L);
        var result = compositeRepository.update(999999L, updateRequest);
        assertNull(result);
    }

    @Test
    void testPointCreateWithNullFunction() {
        CreatePointRequest request = new CreatePointRequest(testFunction.getFunctionId(), 1.0, 1.0);
        assertThrows(NullPointerException.class, () -> pointRepository.create(request, null));
    }

    @Test
    void testFunctionCreateWithNullUser() {
        CreateFunctionRequest request = new CreateFunctionRequest(testUser.getUserId(), "func", "TYPE", "x", 0.0, 1.0);
        assertThrows(NullPointerException.class, () -> functionRepository.create(request, null));
    }

    @Test
    void testCompositeCreateWithNullParameters() {
        CreateCompositeFunctionRequest request = new CreateCompositeFunctionRequest(testUser.getUserId(), "comp", 1L, 2L);
        assertThrows(NullPointerException.class, () -> compositeRepository.create(request, null, null, null));
    }
}