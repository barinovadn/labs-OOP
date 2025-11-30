package controller;

import dto.*;
import entity.*;
import repository.*;
import search.SearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OperationControllerTest {

    @Mock
    private FunctionRepository functionRepository;

    @Mock
    private PointRepository pointRepository;

    @Mock
    private SearchService searchService;

    @InjectMocks
    private OperationController controller;

    private FunctionEntity testFunction;
    private FunctionEntity testFunction2;
    private List<PointEntity> testPoints;

    @BeforeEach
    void setUp() {
        UserEntity user = new UserEntity("testuser", "pass", "test@email.com");
        user.setUserId(1L);

        testFunction = new FunctionEntity(user, "TestFunc", "POLYNOMIAL", "x^2", 0.0, 10.0);
        testFunction.setFunctionId(1L);
        testFunction.setCreatedAt(LocalDateTime.now());

        testFunction2 = new FunctionEntity(user, "AnotherFunc", "LINEAR", "2x", 0.0, 5.0);
        testFunction2.setFunctionId(2L);
        testFunction2.setCreatedAt(LocalDateTime.now().plusDays(1));

        testPoints = new ArrayList<>();
        PointEntity point1 = new PointEntity(testFunction, 0.0, 0.0);
        point1.setPointId(1L);
        PointEntity point2 = new PointEntity(testFunction, 1.0, 1.0);
        point2.setPointId(2L);
        PointEntity point3 = new PointEntity(testFunction, 2.0, 4.0);
        point3.setPointId(3L);
        testPoints.add(point1);
        testPoints.add(point2);
        testPoints.add(point3);
    }

    @Test
    void testPerformOperationAddSuccess() {
        OperationRequest request = new OperationRequest();
        request.setOperation("add");
        request.setFunctionAId(1L);
        request.setFunctionBId(1L);

        when(functionRepository.findById(1L)).thenReturn(Optional.of(testFunction));
        when(pointRepository.findByFunctionIdOrderByXValue(1L)).thenReturn(testPoints);

        ResponseEntity<ApiResponse<Map<String, Object>>> response = 
            controller.performOperation(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        assertEquals("add", response.getBody().getData().get("operation"));
    }

    @Test
    void testPerformOperationSubtractSuccess() {
        OperationRequest request = new OperationRequest();
        request.setOperation("subtract");
        request.setFunctionAId(1L);
        request.setFunctionBId(1L);

        when(functionRepository.findById(1L)).thenReturn(Optional.of(testFunction));
        when(pointRepository.findByFunctionIdOrderByXValue(1L)).thenReturn(testPoints);

        ResponseEntity<ApiResponse<Map<String, Object>>> response = 
            controller.performOperation(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
    }

    @Test
    void testPerformOperationMultiplySuccess() {
        OperationRequest request = new OperationRequest();
        request.setOperation("multiply");
        request.setFunctionAId(1L);
        request.setFunctionBId(1L);

        when(functionRepository.findById(1L)).thenReturn(Optional.of(testFunction));
        when(pointRepository.findByFunctionIdOrderByXValue(1L)).thenReturn(testPoints);

        ResponseEntity<ApiResponse<Map<String, Object>>> response = 
            controller.performOperation(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
    }

    @Test
    void testPerformOperationDivideSuccess() {
        OperationRequest request = new OperationRequest();
        request.setOperation("divide");
        request.setFunctionAId(1L);
        request.setFunctionBId(1L);

        // Create points with non-zero y values for division
        List<PointEntity> nonZeroPoints = new ArrayList<>();
        PointEntity p1 = new PointEntity(testFunction, 0.0, 1.0);
        p1.setPointId(1L);
        PointEntity p2 = new PointEntity(testFunction, 1.0, 2.0);
        p2.setPointId(2L);
        PointEntity p3 = new PointEntity(testFunction, 2.0, 4.0);
        p3.setPointId(3L);
        nonZeroPoints.add(p1);
        nonZeroPoints.add(p2);
        nonZeroPoints.add(p3);

        when(functionRepository.findById(1L)).thenReturn(Optional.of(testFunction));
        when(pointRepository.findByFunctionIdOrderByXValue(1L)).thenReturn(nonZeroPoints);

        ResponseEntity<ApiResponse<Map<String, Object>>> response = 
            controller.performOperation(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
    }

    @Test
    void testPerformOperationUnknownOperation() {
        OperationRequest request = new OperationRequest();
        request.setOperation("unknown");
        request.setFunctionAId(1L);
        request.setFunctionBId(1L);

        when(functionRepository.findById(1L)).thenReturn(Optional.of(testFunction));
        when(pointRepository.findByFunctionIdOrderByXValue(1L)).thenReturn(testPoints);

        ResponseEntity<ApiResponse<Map<String, Object>>> response = 
            controller.performOperation(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
    }

    @Test
    void testPerformOperationFunctionNotFound() {
        OperationRequest request = new OperationRequest();
        request.setOperation("add");
        request.setFunctionAId(999L);
        request.setFunctionBId(1L);

        when(functionRepository.findById(999L)).thenReturn(Optional.empty());

        ResponseEntity<ApiResponse<Map<String, Object>>> response = 
            controller.performOperation(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
    }

    @Test
    void testPerformOperationNotEnoughPoints() {
        OperationRequest request = new OperationRequest();
        request.setOperation("add");
        request.setFunctionAId(1L);
        request.setFunctionBId(1L);

        // Only one point
        List<PointEntity> singlePoint = Arrays.asList(testPoints.get(0));

        when(functionRepository.findById(1L)).thenReturn(Optional.of(testFunction));
        when(pointRepository.findByFunctionIdOrderByXValue(1L)).thenReturn(singlePoint);

        ResponseEntity<ApiResponse<Map<String, Object>>> response = 
            controller.performOperation(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testDifferentiateFunctionSuccess() {
        when(functionRepository.findById(1L)).thenReturn(Optional.of(testFunction));
        when(pointRepository.findByFunctionIdOrderByXValue(1L)).thenReturn(testPoints);

        ResponseEntity<ApiResponse<Map<String, Object>>> response = 
            controller.differentiateFunction(1L, "LEFT");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        assertEquals(1L, response.getBody().getData().get("functionId"));
    }

    @Test
    void testDifferentiateFunctionNotFound() {
        when(functionRepository.findById(999L)).thenReturn(Optional.empty());

        ResponseEntity<ApiResponse<Map<String, Object>>> response = 
            controller.differentiateFunction(999L, "LEFT");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
    }

    @Test
    void testDifferentiateFunctionNotEnoughPoints() {
        List<PointEntity> singlePoint = Arrays.asList(testPoints.get(0));

        when(functionRepository.findById(1L)).thenReturn(Optional.of(testFunction));
        when(pointRepository.findByFunctionIdOrderByXValue(1L)).thenReturn(singlePoint);

        ResponseEntity<ApiResponse<Map<String, Object>>> response = 
            controller.differentiateFunction(1L, "MIDDLE");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testSearchFunctionsSuccess() {
        SearchRequest request = new SearchRequest();
        request.setSearchTerm("Test");

        when(functionRepository.findAll()).thenReturn(Arrays.asList(testFunction));

        ResponseEntity<ApiResponse<Map<String, Object>>> response = 
            controller.searchFunctions(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        assertNotNull(response.getBody().getData().get("results"));
    }

    @Test
    void testSearchFunctionsWithSorting() {
        SearchRequest request = new SearchRequest();
        request.setSearchTerm("Test");
        request.setSortField("functionName");
        request.setAscending(true);

        when(functionRepository.findAll()).thenReturn(Arrays.asList(testFunction));

        ResponseEntity<ApiResponse<Map<String, Object>>> response = 
            controller.searchFunctions(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
    }

    @Test
    void testSearchFunctionsWithPagination() {
        SearchRequest request = new SearchRequest();
        request.setSearchTerm("Test");
        request.setPage(0);
        request.setSize(10);

        when(functionRepository.findAll()).thenReturn(Arrays.asList(testFunction));

        ResponseEntity<ApiResponse<Map<String, Object>>> response = 
            controller.searchFunctions(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
    }

    @Test
    void testSearchFunctionsNoSearchTerm() {
        SearchRequest request = new SearchRequest();
        request.setSearchTerm("");

        ResponseEntity<ApiResponse<Map<String, Object>>> response = 
            controller.searchFunctions(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testSearchFunctionsNullSearchTerm() {
        SearchRequest request = new SearchRequest();
        request.setSearchTerm(null);

        ResponseEntity<ApiResponse<Map<String, Object>>> response = 
            controller.searchFunctions(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testSearchFunctionsByType() {
        SearchRequest request = new SearchRequest();
        request.setSearchTerm("POLYNOMIAL");

        when(functionRepository.findAll()).thenReturn(Arrays.asList(testFunction));

        ResponseEntity<ApiResponse<Map<String, Object>>> response = 
            controller.searchFunctions(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, ((List<?>) response.getBody().getData().get("results")).size());
    }

    @Test
    void testSearchFunctionsByExpression() {
        SearchRequest request = new SearchRequest();
        request.setSearchTerm("x^2");

        when(functionRepository.findAll()).thenReturn(Arrays.asList(testFunction));

        ResponseEntity<ApiResponse<Map<String, Object>>> response = 
            controller.searchFunctions(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testSearchFunctionsNoMatch() {
        SearchRequest request = new SearchRequest();
        request.setSearchTerm("notfound");

        when(functionRepository.findAll()).thenReturn(Arrays.asList(testFunction));

        ResponseEntity<ApiResponse<Map<String, Object>>> response = 
            controller.searchFunctions(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, ((List<?>) response.getBody().getData().get("results")).size());
    }

    @Test
    void testSearchFunctionsPageBeyondResults() {
        SearchRequest request = new SearchRequest();
        request.setSearchTerm("Test");
        request.setPage(100);
        request.setSize(10);

        when(functionRepository.findAll()).thenReturn(Arrays.asList(testFunction));

        ResponseEntity<ApiResponse<Map<String, Object>>> response = 
            controller.searchFunctions(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(((List<?>) response.getBody().getData().get("results")).isEmpty());
    }

    @Test
    void testSearchFunctionsWithDescendingSort() {
        SearchRequest request = new SearchRequest();
        request.setSearchTerm("Func");
        request.setSortField("functionName");
        request.setAscending(false);

        when(functionRepository.findAll()).thenReturn(Arrays.asList(testFunction, testFunction2));

        ResponseEntity<ApiResponse<Map<String, Object>>> response = 
            controller.searchFunctions(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        List<?> results = (List<?>) response.getBody().getData().get("results");
        assertEquals(2, results.size());
    }

    @Test
    void testSearchFunctionsWithAscendingSort() {
        SearchRequest request = new SearchRequest();
        request.setSearchTerm("Func");
        request.setSortField("functionName");
        request.setAscending(true);

        when(functionRepository.findAll()).thenReturn(Arrays.asList(testFunction, testFunction2));

        ResponseEntity<ApiResponse<Map<String, Object>>> response = 
            controller.searchFunctions(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        List<?> results = (List<?>) response.getBody().getData().get("results");
        assertEquals(2, results.size());
    }

    @Test
    void testSearchFunctionsWithNullAscending() {
        SearchRequest request = new SearchRequest();
        request.setSearchTerm("Func");
        request.setSortField("functionName");
        request.setAscending(null);

        when(functionRepository.findAll()).thenReturn(Arrays.asList(testFunction, testFunction2));

        ResponseEntity<ApiResponse<Map<String, Object>>> response = 
            controller.searchFunctions(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testSearchFunctionsWithInvalidSortField() {
        SearchRequest request = new SearchRequest();
        request.setSearchTerm("Func");
        request.setSortField("nonExistentField");
        request.setAscending(true);

        when(functionRepository.findAll()).thenReturn(Arrays.asList(testFunction, testFunction2));

        ResponseEntity<ApiResponse<Map<String, Object>>> response = 
            controller.searchFunctions(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testSearchFunctionsSortByFunctionType() {
        SearchRequest request = new SearchRequest();
        request.setSearchTerm("Func");
        request.setSortField("functionType");
        request.setAscending(true);

        when(functionRepository.findAll()).thenReturn(Arrays.asList(testFunction, testFunction2));

        ResponseEntity<ApiResponse<Map<String, Object>>> response = 
            controller.searchFunctions(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testSearchFunctionsWithNullExpression() {
        FunctionEntity funcWithNullExpression = new FunctionEntity();
        funcWithNullExpression.setFunctionId(2L);
        funcWithNullExpression.setFunctionName("NoExpression");
        funcWithNullExpression.setFunctionType("SIMPLE");
        funcWithNullExpression.setFunctionExpression(null);

        SearchRequest request = new SearchRequest();
        request.setSearchTerm("NoExpression");

        when(functionRepository.findAll()).thenReturn(Arrays.asList(funcWithNullExpression));

        ResponseEntity<ApiResponse<Map<String, Object>>> response = 
            controller.searchFunctions(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testSearchFunctionsMultipleResults() {
        FunctionEntity func2 = new FunctionEntity();
        func2.setFunctionId(2L);
        func2.setFunctionName("TestFunc2");
        func2.setFunctionType("LINEAR");
        func2.setFunctionExpression("2x");

        SearchRequest request = new SearchRequest();
        request.setSearchTerm("Test");

        when(functionRepository.findAll()).thenReturn(Arrays.asList(testFunction, func2));

        ResponseEntity<ApiResponse<Map<String, Object>>> response = 
            controller.searchFunctions(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, ((List<?>) response.getBody().getData().get("results")).size());
    }

    @Test
    void testSearchFunctionsExceptionHandling() {
        SearchRequest request = new SearchRequest();
        request.setSearchTerm("Test");

        when(functionRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        ResponseEntity<ApiResponse<Map<String, Object>>> response = 
            controller.searchFunctions(request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
    }

    @Test
    void testDifferentiateFunctionWithMiddleType() {
        when(functionRepository.findById(1L)).thenReturn(Optional.of(testFunction));
        when(pointRepository.findByFunctionIdOrderByXValue(1L)).thenReturn(testPoints);

        ResponseEntity<ApiResponse<Map<String, Object>>> response = 
            controller.differentiateFunction(1L, "MIDDLE");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("MIDDLE", response.getBody().getData().get("type"));
    }

    @Test
    void testDifferentiateFunctionWithRightType() {
        when(functionRepository.findById(1L)).thenReturn(Optional.of(testFunction));
        when(pointRepository.findByFunctionIdOrderByXValue(1L)).thenReturn(testPoints);

        ResponseEntity<ApiResponse<Map<String, Object>>> response = 
            controller.differentiateFunction(1L, "RIGHT");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("RIGHT", response.getBody().getData().get("type"));
    }
}

