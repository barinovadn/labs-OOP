package dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DtoTest {

    @Test
    void testFunctionRequestAllFields() {
        FunctionRequest request = new FunctionRequest();
        
        request.setUserId(1L);
        request.setFunctionName("TestFunction");
        request.setFunctionType("POLYNOMIAL");
        request.setFunctionExpression("x^2");
        request.setXFrom(0.0);
        request.setXTo(10.0);
        
        TabulatedFunctionRequest tabFunc = new TabulatedFunctionRequest();
        request.setTabulatedFunction(tabFunc);
        
        assertEquals(1L, request.getUserId());
        assertEquals("TestFunction", request.getFunctionName());
        assertEquals("POLYNOMIAL", request.getFunctionType());
        assertEquals("x^2", request.getFunctionExpression());
        assertEquals(0.0, request.getXFrom());
        assertEquals(10.0, request.getXTo());
        assertNotNull(request.getTabulatedFunction());
    }

    @Test
    void testFunctionResponseAllFields() {
        FunctionResponse response = new FunctionResponse();
        LocalDateTime now = LocalDateTime.now();
        
        response.setFunctionId(1L);
        response.setUserId(2L);
        response.setFunctionName("TestFunc");
        response.setFunctionType("LINEAR");
        response.setFunctionExpression("2x+1");
        response.setXFrom(-5.0);
        response.setXTo(5.0);
        response.setCreatedAt(now);
        
        assertEquals(1L, response.getFunctionId());
        assertEquals(2L, response.getUserId());
        assertEquals("TestFunc", response.getFunctionName());
        assertEquals("LINEAR", response.getFunctionType());
        assertEquals("2x+1", response.getFunctionExpression());
        assertEquals(-5.0, response.getXFrom());
        assertEquals(5.0, response.getXTo());
        assertEquals(now, response.getCreatedAt());
    }

    @Test
    void testPointRequestConstructorAndGettersSetters() {
        PointRequest request1 = new PointRequest();
        request1.setFunctionId(1L);
        request1.setXValue(2.5);
        request1.setYValue(6.25);
        
        assertEquals(1L, request1.getFunctionId());
        assertEquals(2.5, request1.getXValue());
        assertEquals(6.25, request1.getYValue());
        
        PointRequest request2 = new PointRequest(3L, 4.0, 16.0);
        assertEquals(3L, request2.getFunctionId());
        assertEquals(4.0, request2.getXValue());
        assertEquals(16.0, request2.getYValue());
    }

    @Test
    void testPointResponseConstructorAndGettersSetters() {
        LocalDateTime now = LocalDateTime.now();
        
        PointResponse response1 = new PointResponse();
        response1.setPointId(1L);
        response1.setFunctionId(2L);
        response1.setXValue(3.0);
        response1.setYValue(9.0);
        response1.setComputedAt(now);
        
        assertEquals(1L, response1.getPointId());
        assertEquals(2L, response1.getFunctionId());
        assertEquals(3.0, response1.getXValue());
        assertEquals(9.0, response1.getYValue());
        assertEquals(now, response1.getComputedAt());
        
        PointResponse response2 = new PointResponse(5L, 6L, 7.0, 49.0, now);
        assertEquals(5L, response2.getPointId());
        assertEquals(6L, response2.getFunctionId());
        assertEquals(7.0, response2.getXValue());
        assertEquals(49.0, response2.getYValue());
        assertEquals(now, response2.getComputedAt());
    }

    @Test
    void testUserRequestConstructorAndGettersSetters() {
        UserRequest request1 = new UserRequest();
        request1.setUsername("user1");
        request1.setPassword("pass123");
        request1.setEmail("user1@test.com");
        
        assertEquals("user1", request1.getUsername());
        assertEquals("pass123", request1.getPassword());
        assertEquals("user1@test.com", request1.getEmail());
        
        UserRequest request2 = new UserRequest("user2", "pass456", "user2@test.com");
        assertEquals("user2", request2.getUsername());
        assertEquals("pass456", request2.getPassword());
        assertEquals("user2@test.com", request2.getEmail());
    }

    @Test
    void testUserResponseConstructorAndGettersSetters() {
        LocalDateTime now = LocalDateTime.now();
        
        UserResponse response1 = new UserResponse();
        response1.setUserId(1L);
        response1.setUsername("testuser");
        response1.setEmail("test@email.com");
        response1.setCreatedAt(now);
        
        assertEquals(1L, response1.getUserId());
        assertEquals("testuser", response1.getUsername());
        assertEquals("test@email.com", response1.getEmail());
        assertEquals(now, response1.getCreatedAt());
        
        UserResponse response2 = new UserResponse(2L, "user2", "user2@email.com", now);
        assertEquals(2L, response2.getUserId());
        assertEquals("user2", response2.getUsername());
        assertEquals("user2@email.com", response2.getEmail());
        assertEquals(now, response2.getCreatedAt());
    }

    @Test
    void testRoleRequestAndResponse() {
        RoleRequest roleRequest = new RoleRequest();
        roleRequest.setRoleName("ADMIN");
        roleRequest.setDescription("Administrator role");
        
        assertEquals("ADMIN", roleRequest.getRoleName());
        assertEquals("Administrator role", roleRequest.getDescription());
        
        RoleResponse roleResponse = new RoleResponse();
        roleResponse.setRoleId(1L);
        roleResponse.setRoleName("USER");
        roleResponse.setDescription("Basic user role");
        
        assertEquals(1L, roleResponse.getRoleId());
        assertEquals("USER", roleResponse.getRoleName());
        assertEquals("Basic user role", roleResponse.getDescription());
    }

    @Test
    void testApiResponseStaticMethodsAndFields() {
        ApiResponse<String> success1 = ApiResponse.success("test data");
        assertTrue(success1.isSuccess());
        assertEquals("Success", success1.getMessage());
        assertEquals("test data", success1.getData());
        assertNotNull(success1.getTimestamp());
        
        ApiResponse<Integer> success2 = ApiResponse.success("Custom message", 42);
        assertTrue(success2.isSuccess());
        assertEquals("Custom message", success2.getMessage());
        assertEquals(42, success2.getData());
        
        ApiResponse<Object> error = ApiResponse.error("Error occurred");
        assertFalse(error.isSuccess());
        assertEquals("Error occurred", error.getMessage());
        assertNull(error.getData());
        
        ApiResponse<String> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("msg");
        response.setData("data");
        response.setPath("/api/test");
        LocalDateTime time = LocalDateTime.now();
        response.setTimestamp(time);
        
        assertTrue(response.isSuccess());
        assertEquals("msg", response.getMessage());
        assertEquals("data", response.getData());
        assertEquals("/api/test", response.getPath());
        assertEquals(time, response.getTimestamp());
    }

    @Test
    void testErrorResponseAndValidationError() {
        ErrorResponse error1 = new ErrorResponse();
        assertNotNull(error1.getTimestamp());
        
        ErrorResponse error2 = new ErrorResponse(404, "Not Found", "Resource not found", "/api/test");
        assertEquals(404, error2.getStatus());
        assertEquals("Not Found", error2.getError());
        assertEquals("Resource not found", error2.getMessage());
        assertEquals("/api/test", error2.getPath());
        assertNotNull(error2.getTimestamp());
        
        LocalDateTime time = LocalDateTime.now();
        error2.setTimestamp(time);
        error2.setStatus(500);
        error2.setError("Internal Error");
        error2.setMessage("Something went wrong");
        error2.setPath("/api/error");
        
        assertEquals(time, error2.getTimestamp());
        assertEquals(500, error2.getStatus());
        assertEquals("Internal Error", error2.getError());
        assertEquals("Something went wrong", error2.getMessage());
        assertEquals("/api/error", error2.getPath());
        
        ErrorResponse.ValidationError validationError = 
            new ErrorResponse.ValidationError("fieldName", "must not be null", null);
        assertEquals("fieldName", validationError.getField());
        assertEquals("must not be null", validationError.getMessage());
        assertNull(validationError.getRejectedValue());
        
        validationError.setField("email");
        validationError.setMessage("invalid format");
        validationError.setRejectedValue("bad@");
        
        assertEquals("email", validationError.getField());
        assertEquals("invalid format", validationError.getMessage());
        assertEquals("bad@", validationError.getRejectedValue());
        
        error2.setValidationErrors(Arrays.asList(validationError));
        assertNotNull(error2.getValidationErrors());
        assertEquals(1, error2.getValidationErrors().size());
    }

    @Test
    void testCalculationRequestAllFields() {
        CalculationRequest request = new CalculationRequest();
        
        request.setOperation("INTEGRATE");
        request.setThreads(4);
        request.setStep(0.01);
        request.setDifferentialType("MIDDLE");
        
        TabulatedFunctionRequest funcA = new TabulatedFunctionRequest();
        TabulatedFunctionRequest funcB = new TabulatedFunctionRequest();
        TabulatedFunctionRequest func = new TabulatedFunctionRequest();
        
        request.setFunction(func);
        request.setFunctionA(funcA);
        request.setFunctionB(funcB);
        
        assertEquals("INTEGRATE", request.getOperation());
        assertEquals(4, request.getThreads());
        assertEquals(0.01, request.getStep());
        assertEquals("MIDDLE", request.getDifferentialType());
        assertNotNull(request.getFunction());
        assertNotNull(request.getFunctionA());
        assertNotNull(request.getFunctionB());
    }

    @Test
    void testCalculationResponseConstructorAndFields() {
        CalculationResponse response1 = new CalculationResponse();
        response1.setOperation("DIFFERENTIATE");
        response1.setResult(3.14);
        response1.setFunctionType("POLYNOMIAL");
        response1.setComputationTimeMs(100L);
        response1.setDetails("Computed derivative");
        
        assertEquals("DIFFERENTIATE", response1.getOperation());
        assertEquals(3.14, response1.getResult());
        assertEquals("POLYNOMIAL", response1.getFunctionType());
        assertEquals(100L, response1.getComputationTimeMs());
        assertEquals("Computed derivative", response1.getDetails());
        
        CalculationResponse response2 = new CalculationResponse("ADD", 10.0, "LINEAR");
        assertEquals("ADD", response2.getOperation());
        assertEquals(10.0, response2.getResult());
        assertEquals("LINEAR", response2.getFunctionType());
    }

    @Test
    void testTabulatedFunctionRequestAllFieldsAndToString() {
        TabulatedFunctionRequest request = new TabulatedFunctionRequest();
        
        request.setType("ARRAY");
        double[] xValues = {0.0, 1.0, 2.0};
        double[] yValues = {0.0, 1.0, 4.0};
        request.setXValues(xValues);
        request.setYValues(yValues);
        request.setXFrom(0.0);
        request.setXTo(10.0);
        request.setPointsCount(11);
        request.setMathFunctionType("SQR");
        request.setConstantValue(5.0);
        
        assertEquals("ARRAY", request.getType());
        assertArrayEquals(xValues, request.getXValues());
        assertArrayEquals(yValues, request.getYValues());
        assertEquals(0.0, request.getXFrom());
        assertEquals(10.0, request.getXTo());
        assertEquals(11, request.getPointsCount());
        assertEquals("SQR", request.getMathFunctionType());
        assertEquals(5.0, request.getConstantValue());
        
        String str = request.toString();
        assertTrue(str.contains("ARRAY"));
        assertTrue(str.contains("SQR"));
    }

    @Test
    void testCompositeFunctionRequestAndResponse() {
        CompositeFunctionRequest request = new CompositeFunctionRequest();
        request.setUserId(1L);
        request.setCompositeName("f(g(x))");
        request.setFirstFunctionId(10L);
        request.setSecondFunctionId(20L);
        
        assertEquals(1L, request.getUserId());
        assertEquals("f(g(x))", request.getCompositeName());
        assertEquals(10L, request.getFirstFunctionId());
        assertEquals(20L, request.getSecondFunctionId());
        
        LocalDateTime now = LocalDateTime.now();
        CompositeFunctionResponse response = new CompositeFunctionResponse();
        response.setCompositeId(100L);
        response.setUserId(1L);
        response.setCompositeName("composite");
        response.setFirstFunctionId(10L);
        response.setSecondFunctionId(20L);
        response.setCreatedAt(now);
        
        assertEquals(100L, response.getCompositeId());
        assertEquals(1L, response.getUserId());
        assertEquals("composite", response.getCompositeName());
        assertEquals(10L, response.getFirstFunctionId());
        assertEquals(20L, response.getSecondFunctionId());
        assertEquals(now, response.getCreatedAt());
    }

    @Test
    void testOperationRequest() {
        OperationRequest request = new OperationRequest();
        
        request.setOperation("add");
        request.setFunctionAId(1L);
        request.setFunctionBId(2L);
        
        assertEquals("add", request.getOperation());
        assertEquals(1L, request.getFunctionAId());
        assertEquals(2L, request.getFunctionBId());
        
        for (String op : Arrays.asList("add", "subtract", "multiply", "divide")) {
            request.setOperation(op);
            assertEquals(op, request.getOperation());
        }
    }

    @Test
    void testSearchRequest() {
        SearchRequest request = new SearchRequest();
        
        request.setSearchTerm("polynomial");
        Map<String, Object> criteria = new HashMap<>();
        criteria.put("type", "LINEAR");
        criteria.put("userId", 1L);
        request.setCriteria(criteria);
        request.setSortField("createdAt");
        request.setAscending(false);
        request.setPage(0);
        request.setSize(20);
        
        assertEquals("polynomial", request.getSearchTerm());
        assertNotNull(request.getCriteria());
        assertEquals(2, request.getCriteria().size());
        assertEquals("LINEAR", request.getCriteria().get("type"));
        assertEquals("createdAt", request.getSortField());
        assertFalse(request.getAscending());
        assertEquals(0, request.getPage());
        assertEquals(20, request.getSize());
    }

    @Test
    void testAssignRoleRequest() {
        AssignRoleRequest request = new AssignRoleRequest();
        
        request.setUserId(1L);
        List<Long> roleIds = Arrays.asList(1L, 2L, 3L);
        request.setRoleIds(roleIds);
        
        assertEquals(1L, request.getUserId());
        assertEquals(3, request.getRoleIds().size());
        assertTrue(request.getRoleIds().containsAll(roleIds));
    }

    @Test
    void testAllDtoDefaultConstructors() {
        assertNotNull(new FunctionRequest());
        assertNotNull(new FunctionResponse());
        assertNotNull(new PointRequest());
        assertNotNull(new PointResponse());
        assertNotNull(new UserRequest());
        assertNotNull(new UserResponse());
        assertNotNull(new RoleRequest());
        assertNotNull(new RoleResponse());
        assertNotNull(new ApiResponse<>());
        assertNotNull(new ErrorResponse());
        assertNotNull(new CalculationRequest());
        assertNotNull(new CalculationResponse());
        assertNotNull(new TabulatedFunctionRequest());
        assertNotNull(new CompositeFunctionRequest());
        assertNotNull(new CompositeFunctionResponse());
        assertNotNull(new OperationRequest());
        assertNotNull(new SearchRequest());
        assertNotNull(new AssignRoleRequest());
    }
}
