package manual.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DtoFullTest {

    @Test
    void testApiResponseAllCases() {
        ApiResponse<String> resp = new ApiResponse<>();
        assertNotNull(resp.getTimestamp());
        assertNull(resp.getData());
        assertNull(resp.getMessage());

        LocalDateTime now = LocalDateTime.now();
        resp.setSuccess(true);
        resp.setMessage("test message");
        resp.setData("test data");
        resp.setTimestamp(now);
        resp.setPath("/api/test");
        assertTrue(resp.isSuccess());
        assertEquals("test message", resp.getMessage());
        assertEquals("test data", resp.getData());
        assertEquals(now, resp.getTimestamp());
        assertEquals("/api/test", resp.getPath());

        ApiResponse<Integer> success = ApiResponse.success(42);
        assertTrue(success.isSuccess());
        assertEquals("Success", success.getMessage());
        assertEquals(42, success.getData());

        ApiResponse<String> successMsg = ApiResponse.success("Custom", "data");
        assertTrue(successMsg.isSuccess());
        assertEquals("Custom", successMsg.getMessage());
        assertEquals("data", successMsg.getData());

        ApiResponse<Object> error = ApiResponse.error("Error occurred");
        assertFalse(error.isSuccess());
        assertEquals("Error occurred", error.getMessage());
        assertNull(error.getData());
    }

    @Test
    void testErrorResponseAllCases() {
        ErrorResponse resp = new ErrorResponse();
        LocalDateTime now = LocalDateTime.now();
        List<ErrorResponse.ValidationError> errors = Arrays.asList(
                new ErrorResponse.ValidationError("field1", "error1", "value1")
        );

        resp.setStatus(400);
        resp.setError("Bad Request");
        resp.setMessage("Validation failed");
        resp.setPath("/api/test");
        resp.setTimestamp(now);
        resp.setValidationErrors(errors);

        assertEquals(400, resp.getStatus());
        assertEquals("Bad Request", resp.getError());
        assertEquals("Validation failed", resp.getMessage());
        assertEquals("/api/test", resp.getPath());
        assertEquals(now, resp.getTimestamp());
        assertNotNull(resp.getValidationErrors());
        assertEquals(1, resp.getValidationErrors().size());

        ErrorResponse.ValidationError ve = errors.get(0);
        assertEquals("field1", ve.getField());
        assertEquals("error1", ve.getMessage());
        assertEquals("value1", ve.getRejectedValue());
        ve.setField("f2");
        ve.setMessage("m2");
        ve.setRejectedValue("v2");
        assertEquals("f2", ve.getField());
        assertEquals("m2", ve.getMessage());
        assertEquals("v2", ve.getRejectedValue());
    }

    @Test
    void testRequestDtosAllCases() {
        CreateUserRequest userReq = new CreateUserRequest();
        userReq.setUsername("user");
        userReq.setPassword("pass");
        userReq.setEmail("email@test.com");
        assertEquals("user", userReq.getUsername());
        assertEquals("pass", userReq.getPassword());
        assertEquals("email@test.com", userReq.getEmail());

        CreateUserRequest userReq2 = new CreateUserRequest("u", "p", "e");
        assertEquals("u", userReq2.getUsername());
        assertEquals("p", userReq2.getPassword());
        assertEquals("e", userReq2.getEmail());

        CreateFunctionRequest funcReq = new CreateFunctionRequest();
        funcReq.setUserId(1L);
        funcReq.setFunctionName("name");
        funcReq.setFunctionType("type");
        funcReq.setFunctionExpression("expr");
        funcReq.setXFrom(0.0);
        funcReq.setXTo(10.0);
        assertEquals(1L, funcReq.getUserId());
        assertEquals("name", funcReq.getFunctionName());
        assertEquals("type", funcReq.getFunctionType());
        assertEquals("expr", funcReq.getFunctionExpression());
        assertEquals(0.0, funcReq.getXFrom());
        assertEquals(10.0, funcReq.getXTo());

        CreateFunctionRequest funcReq2 = new CreateFunctionRequest(2L, "n", "t", "e", 1.0, 5.0);
        assertEquals(2L, funcReq2.getUserId());

        CreatePointRequest pointReq = new CreatePointRequest();
        pointReq.setFunctionId(1L);
        pointReq.setXValue(5.0);
        pointReq.setYValue(25.0);
        assertEquals(1L, pointReq.getFunctionId());
        assertEquals(5.0, pointReq.getXValue());
        assertEquals(25.0, pointReq.getYValue());

        CreatePointRequest pointReq2 = new CreatePointRequest(2L, 3.0, 9.0);
        assertEquals(2L, pointReq2.getFunctionId());

        CreateCompositeFunctionRequest compReq = new CreateCompositeFunctionRequest();
        compReq.setUserId(1L);
        compReq.setCompositeName("name");
        compReq.setFirstFunctionId(10L);
        compReq.setSecondFunctionId(20L);
        assertEquals(1L, compReq.getUserId());
        assertEquals("name", compReq.getCompositeName());
        assertEquals(10L, compReq.getFirstFunctionId());
        assertEquals(20L, compReq.getSecondFunctionId());

        CreateCompositeFunctionRequest compReq2 = new CreateCompositeFunctionRequest(2L, "c", 3L, 4L);
        assertEquals(2L, compReq2.getUserId());
    }

    @Test
    void testResponseDtosAllCases() {
        LocalDateTime now = LocalDateTime.now();

        UserResponse userResp = new UserResponse();
        userResp.setUserId(1L);
        userResp.setUsername("user");
        userResp.setEmail("email@test.com");
        userResp.setCreatedAt(now);
        assertEquals(1L, userResp.getUserId());
        assertEquals("user", userResp.getUsername());
        assertEquals("email@test.com", userResp.getEmail());
        assertEquals(now, userResp.getCreatedAt());

        FunctionResponse funcResp = new FunctionResponse();
        funcResp.setFunctionId(1L);
        funcResp.setUserId(2L);
        funcResp.setFunctionName("name");
        funcResp.setFunctionType("type");
        funcResp.setFunctionExpression("expr");
        funcResp.setXFrom(0.0);
        funcResp.setXTo(10.0);
        funcResp.setCreatedAt(now);
        assertEquals(1L, funcResp.getFunctionId());
        assertEquals(2L, funcResp.getUserId());
        assertEquals("name", funcResp.getFunctionName());
        assertEquals("type", funcResp.getFunctionType());
        assertEquals("expr", funcResp.getFunctionExpression());
        assertEquals(0.0, funcResp.getXFrom());
        assertEquals(10.0, funcResp.getXTo());
        assertEquals(now, funcResp.getCreatedAt());

        PointResponse pointResp = new PointResponse();
        pointResp.setPointId(1L);
        pointResp.setFunctionId(2L);
        pointResp.setXValue(5.0);
        pointResp.setYValue(25.0);
        pointResp.setComputedAt(now);
        assertEquals(1L, pointResp.getPointId());
        assertEquals(2L, pointResp.getFunctionId());
        assertEquals(5.0, pointResp.getXValue());
        assertEquals(25.0, pointResp.getYValue());
        assertEquals(now, pointResp.getComputedAt());

        CompositeFunctionResponse compResp = new CompositeFunctionResponse();
        compResp.setCompositeId(1L);
        compResp.setUserId(2L);
        compResp.setCompositeName("name");
        compResp.setFirstFunctionId(10L);
        compResp.setSecondFunctionId(20L);
        compResp.setCreatedAt(now);
        assertEquals(1L, compResp.getCompositeId());
        assertEquals(2L, compResp.getUserId());
        assertEquals("name", compResp.getCompositeName());
        assertEquals(10L, compResp.getFirstFunctionId());
        assertEquals(20L, compResp.getSecondFunctionId());
        assertEquals(now, compResp.getCreatedAt());
    }

    @Test
    void testCalculationDtosAllCases() {
        CalculationRequest calcReq = new CalculationRequest();
        TabulatedFunctionRequest func = new TabulatedFunctionRequest();
        TabulatedFunctionRequest funcA = new TabulatedFunctionRequest();
        TabulatedFunctionRequest funcB = new TabulatedFunctionRequest();

        calcReq.setOperation("add");
        calcReq.setFunction(func);
        calcReq.setFunctionA(funcA);
        calcReq.setFunctionB(funcB);
        calcReq.setThreads(4);
        calcReq.setStep(0.01);
        calcReq.setDifferentialType("MIDDLE");
        assertEquals("add", calcReq.getOperation());
        assertEquals(func, calcReq.getFunction());
        assertEquals(funcA, calcReq.getFunctionA());
        assertEquals(funcB, calcReq.getFunctionB());
        assertEquals(4, calcReq.getThreads());
        assertEquals(0.01, calcReq.getStep());
        assertEquals("MIDDLE", calcReq.getDifferentialType());

        CalculationResponse calcResp = new CalculationResponse();
        calcResp.setOperation("subtract");
        calcResp.setResult(42.5);
        calcResp.setFunctionType("ArrayTabulatedFunction");
        calcResp.setComputationTimeMs(100L);
        calcResp.setDetails("details");
        assertEquals("subtract", calcResp.getOperation());
        assertEquals(42.5, calcResp.getResult());
        assertEquals("ArrayTabulatedFunction", calcResp.getFunctionType());
        assertEquals(100L, calcResp.getComputationTimeMs());
        assertEquals("details", calcResp.getDetails());

        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {1.0, 4.0, 9.0};
        TabulatedFunctionRequest tfReq = new TabulatedFunctionRequest();
        tfReq.setType("ARRAY");
        tfReq.setXValues(xValues);
        tfReq.setYValues(yValues);
        tfReq.setXFrom(0.0);
        tfReq.setXTo(10.0);
        tfReq.setPointsCount(100);
        tfReq.setMathFunctionType("SQR");
        tfReq.setConstantValue(5.0);
        assertEquals("ARRAY", tfReq.getType());
        assertArrayEquals(xValues, tfReq.getXValues());
        assertArrayEquals(yValues, tfReq.getYValues());
        assertEquals(0.0, tfReq.getXFrom());
        assertEquals(10.0, tfReq.getXTo());
        assertEquals(100, tfReq.getPointsCount());
        assertEquals("SQR", tfReq.getMathFunctionType());
        assertEquals(5.0, tfReq.getConstantValue());

        String str = tfReq.toString();
        assertNotNull(str);
        assertTrue(str.contains("ARRAY"));
    }
}

