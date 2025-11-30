package manual.api;

import manual.DatabaseConnection;
import manual.entity.FunctionEntity;
import manual.entity.UserEntity;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ApiFullCoverageTest {

    private static class TestApiService extends ApiServiceBase {
        public Connection testGetConnection() throws SQLException {
            return getConnection();
        }

        public double testComputeFunctionValue(FunctionEntity function, double x) {
            return computeFunctionValue(function, x);
        }
    }

    @Test
    void testApiPathsAllConstants() {
        assertEquals("/api", ApiPaths.API_BASE);
        assertEquals("/api/users", ApiPaths.USERS);
        assertEquals("/api/users/{id}", ApiPaths.USER_BY_ID);
        assertEquals("/api/functions", ApiPaths.FUNCTIONS);
        assertEquals("/api/functions/{id}", ApiPaths.FUNCTION_BY_ID);
        assertEquals("/api/points", ApiPaths.POINTS);
        assertEquals("/api/points/{id}", ApiPaths.POINT_BY_ID);
        assertEquals("/api/composite-functions", ApiPaths.COMPOSITE_FUNCTIONS);
        assertEquals("/api/users/{userId}/functions", ApiPaths.USER_FUNCTIONS);
        assertEquals("/api/users/{userId}/composite-functions", ApiPaths.USER_COMPOSITE_FUNCTIONS);
        assertEquals("/api/functions/{functionId}/calculate", ApiPaths.FUNCTION_CALCULATE);
        assertEquals("/api/functions/{functionId}/differentiate", ApiPaths.FUNCTION_DIFFERENTIATE);
        assertEquals("/api/functions/{functionId}/points", ApiPaths.FUNCTION_POINTS);
        assertEquals("/api/functions/operations", ApiPaths.FUNCTION_OPERATIONS);
        assertEquals("/api/functions/search", ApiPaths.FUNCTION_SEARCH);
        assertEquals("sort", ApiPaths.PARAM_SORT);
        assertEquals("name_asc", ApiPaths.PARAM_SORT_NAME_ASC);
        assertEquals("name_desc", ApiPaths.PARAM_SORT_NAME_DESC);
        assertEquals("x_from_asc", ApiPaths.PARAM_SORT_X_FROM_ASC);
        assertEquals("type_name", ApiPaths.PARAM_SORT_TYPE_NAME);
        assertEquals("userId", ApiPaths.PARAM_USER_ID);
        assertEquals("functionId", ApiPaths.PARAM_FUNCTION_ID);
        assertEquals("x", ApiPaths.PARAM_X);
        assertEquals("algorithm", ApiPaths.PARAM_ALGORITHM);
    }

    @Test
    void testApiContractInterfaceMethods() throws NoSuchMethodException {
        assertTrue(ApiContract.class.isInterface());
        assertNotNull(ApiContract.class.getMethod("createUser", manual.dto.CreateUserRequest.class));
        assertNotNull(ApiContract.class.getMethod("getUserById", Long.class));
        assertNotNull(ApiContract.class.getMethod("getAllUsers"));
        assertNotNull(ApiContract.class.getMethod("updateUser", Long.class, manual.dto.CreateUserRequest.class));
        assertNotNull(ApiContract.class.getMethod("deleteUser", Long.class));
        assertNotNull(ApiContract.class.getMethod("createFunction", manual.dto.CreateFunctionRequest.class, Long.class));
        assertNotNull(ApiContract.class.getMethod("getFunctionById", Long.class));
        assertNotNull(ApiContract.class.getMethod("getAllFunctions"));
        assertNotNull(ApiContract.class.getMethod("deleteFunction", Long.class));
        assertNotNull(ApiContract.class.getMethod("createPoint", Long.class, manual.dto.CreatePointRequest.class));
        assertNotNull(ApiContract.class.getMethod("getPointById", Long.class));
        assertNotNull(ApiContract.class.getMethod("getPointsByFunctionId", Long.class));
        assertNotNull(ApiContract.class.getMethod("updatePoint", Long.class, manual.dto.CreatePointRequest.class));
        assertNotNull(ApiContract.class.getMethod("deletePoint", Long.class));
    }

    @Test
    void testApiServiceBaseGetConnection() throws SQLException {
        TestApiService service = new TestApiService();
        Connection conn = service.testGetConnection();
        assertNotNull(conn);
        assertFalse(conn.isClosed());
        conn.close();
    }

    @Test
    void testComputeFunctionValueSimplePositiveNumber() {
        TestApiService service = new TestApiService();
        FunctionEntity func = createFunction("42");
        assertEquals(42.0, service.testComputeFunctionValue(func, 0.0), 0.001);
    }

    @Test
    void testComputeFunctionValueSimpleNegativeNumber() {
        TestApiService service = new TestApiService();
        FunctionEntity func = createFunction("-15.5");
        assertEquals(-15.5, service.testComputeFunctionValue(func, 0.0), 0.001);
    }

    @Test
    void testComputeFunctionValueZero() {
        TestApiService service = new TestApiService();
        FunctionEntity func = createFunction("0");
        assertEquals(0.0, service.testComputeFunctionValue(func, 5.0), 0.001);
    }

    @Test
    void testComputeFunctionValueDecimal() {
        TestApiService service = new TestApiService();
        FunctionEntity func = createFunction("3.14159");
        assertEquals(3.14159, service.testComputeFunctionValue(func, 0.0), 0.00001);
    }

    @Test
    void testComputeFunctionValueComplexExpressionReturnsZero() {
        TestApiService service = new TestApiService();
        FunctionEntity func = createFunction("x*x + 2*x + 1");
        assertEquals(0.0, service.testComputeFunctionValue(func, 0.0), 0.001);
    }

    @Test
    void testComputeFunctionValueComplexSinExpression() {
        TestApiService service = new TestApiService();
        FunctionEntity func = createFunction("sin(x)");
        assertEquals(0.0, service.testComputeFunctionValue(func, 0.0), 0.001);
    }

    @Test
    void testComputeFunctionValueComplexAdditionExpression() {
        TestApiService service = new TestApiService();
        FunctionEntity func = createFunction("complex+expression");
        assertEquals(0.0, service.testComputeFunctionValue(func, 0.0), 0.001);
    }

    @Test
    void testComputeFunctionValueNullExpressionThrows() {
        TestApiService service = new TestApiService();
        FunctionEntity func = createFunction(null);
        assertThrows(NullPointerException.class, () -> {
            service.testComputeFunctionValue(func, 0.0);
        });
    }

    private FunctionEntity createFunction(String expression) {
        UserEntity user = new UserEntity("test", "pass", "test@test.com");
        user.setUserId(1L);
        FunctionEntity function = new FunctionEntity();
        function.setFunctionId(1L);
        function.setUser(user);
        function.setFunctionName("Test Function");
        function.setFunctionType("POLYNOMIAL");
        function.setFunctionExpression(expression);
        function.setXFrom(0.0);
        function.setXTo(10.0);
        function.setCreatedAt(LocalDateTime.now());
        return function;
    }
}
