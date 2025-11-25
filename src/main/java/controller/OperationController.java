package controller;

import dto.ApiResponse;
import dto.OperationRequest;
import dto.SearchRequest;
import entity.FunctionEntity;
import entity.PointEntity;
import functions.ArrayTabulatedFunction;
import functions.TabulatedFunction;
import functions.factory.ArrayTabulatedFunctionFactory;
import functions.factory.TabulatedFunctionFactory;
import operations.TabulatedDifferentialOperator;
import operations.TabulatedFunctionOperationService;
import repository.FunctionRepository;
import repository.PointRepository;
import search.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/functions")
public class OperationController {
    private static final Logger logger = Logger.getLogger(OperationController.class.getName());

    @Autowired
    private FunctionRepository functionRepository;

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private SearchService searchService;

    private TabulatedFunctionFactory factory = new ArrayTabulatedFunctionFactory();
    private TabulatedFunctionOperationService operationService = new TabulatedFunctionOperationService();
    private TabulatedDifferentialOperator differentialOperator = new TabulatedDifferentialOperator();

    private TabulatedFunction convertToTabulatedFunction(FunctionEntity functionEntity) {
        logger.info("Converting FunctionEntity to TabulatedFunction for function ID: " + functionEntity.getFunctionId());
        List<PointEntity> points = pointRepository.findByFunctionIdOrderByXValue(functionEntity.getFunctionId());
        
        if (points.size() < 2) {
            throw new RuntimeException("Function must have at least 2 points");
        }
        
        double[] xValues = new double[points.size()];
        double[] yValues = new double[points.size()];
        
        for (int i = 0; i < points.size(); i++) {
            xValues[i] = points.get(i).getXValue();
            yValues[i] = points.get(i).getYValue();
        }
        
        return new ArrayTabulatedFunction(xValues, yValues);
    }

    @PostMapping("/operations")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> performOperation(@RequestBody OperationRequest request) {
        logger.info("POST /api/functions/operations - Operation: " + request.getOperation() + 
                    ", FunctionA: " + request.getFunctionAId() + ", FunctionB: " + request.getFunctionBId());
        try {
            FunctionEntity functionA = functionRepository.findById(request.getFunctionAId())
                    .orElseThrow(() -> new RuntimeException("Function A not found with id: " + request.getFunctionAId()));
            FunctionEntity functionB = functionRepository.findById(request.getFunctionBId())
                    .orElseThrow(() -> new RuntimeException("Function B not found with id: " + request.getFunctionBId()));
            
            TabulatedFunction tabulatedA = convertToTabulatedFunction(functionA);
            TabulatedFunction tabulatedB = convertToTabulatedFunction(functionB);
            
            TabulatedFunction resultFunction;
            String operationName = request.getOperation().toLowerCase();
            
            switch (operationName) {
                case "add":
                    resultFunction = operationService.add(tabulatedA, tabulatedB);
                    break;
                case "subtract":
                    resultFunction = operationService.subtract(tabulatedA, tabulatedB);
                    break;
                case "multiply":
                    resultFunction = operationService.multiply(tabulatedA, tabulatedB);
                    break;
                case "divide":
                    resultFunction = operationService.divide(tabulatedA, tabulatedB);
                    break;
                default:
                    throw new RuntimeException("Unknown operation: " + request.getOperation());
            }
            
            Map<String, Object> result = new HashMap<>();
            result.put("operation", request.getOperation());
            result.put("functionAId", request.getFunctionAId());
            result.put("functionBId", request.getFunctionBId());
            result.put("status", "success");
            result.put("message", "Operation completed successfully");
            result.put("resultPointsCount", resultFunction.getCount());
            
            logger.info("Operation " + request.getOperation() + " completed successfully");
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (Exception e) {
            logger.severe("Error performing operation: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/{functionId}/differentiate")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> differentiateFunction(
            @PathVariable Long functionId,
            @RequestParam(required = false, defaultValue = "LEFT") String type) {
        logger.info("POST /api/functions/" + functionId + "/differentiate?type=" + type);
        try {
            FunctionEntity functionEntity = functionRepository.findById(functionId)
                    .orElseThrow(() -> new RuntimeException("Function not found with id: " + functionId));
            
            TabulatedFunction tabulatedFunction = convertToTabulatedFunction(functionEntity);
            TabulatedFunction derivative = differentialOperator.derive(tabulatedFunction);
            
            Map<String, Object> result = new HashMap<>();
            result.put("functionId", functionId);
            result.put("type", type);
            result.put("status", "success");
            result.put("message", "Derivative calculated successfully");
            result.put("derivativePointsCount", derivative.getCount());
            
            logger.info("Derivative calculated successfully for function ID: " + functionId);
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (Exception e) {
            logger.severe("Error calculating derivative: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> searchFunctions(@RequestBody SearchRequest request) {
        logger.info("POST /api/functions/search - Search term: " + request.getSearchTerm() + 
                    ", Criteria: " + request.getCriteria());
        try {
            if (request.getSearchTerm() != null && !request.getSearchTerm().isEmpty()) {
                List<Object> results = new ArrayList<>();
                
                List<FunctionEntity> functions = functionRepository.findAll();
                String searchTermLower = request.getSearchTerm().toLowerCase();
                
                for (FunctionEntity function : functions) {
                    if (function.getFunctionName().toLowerCase().contains(searchTermLower) ||
                        function.getFunctionType().toLowerCase().contains(searchTermLower) ||
                        (function.getFunctionExpression() != null && 
                         function.getFunctionExpression().toLowerCase().contains(searchTermLower))) {
                        results.add(function);
                    }
                }
                
                if (request.getSortField() != null && !results.isEmpty()) {
                    results.sort((a, b) -> {
                        try {
                            java.lang.reflect.Field field = a.getClass().getDeclaredField(request.getSortField());
                            field.setAccessible(true);
                            Comparable valueA = (Comparable) field.get(a);
                            Comparable valueB = (Comparable) field.get(b);
                            int comparison = valueA.compareTo(valueB);
                            return request.getAscending() != null && request.getAscending() ? comparison : -comparison;
                        } catch (Exception e) {
                            return 0;
                        }
                    });
                }
                
                int totalCount = results.size();
                
                if (request.getPage() != null && request.getSize() != null) {
                    int start = request.getPage() * request.getSize();
                    int end = Math.min(start + request.getSize(), results.size());
                    if (start < results.size()) {
                        results = results.subList(start, end);
                    } else {
                        results = new ArrayList<>();
                    }
                }
                
                Map<String, Object> result = new HashMap<>();
                result.put("searchTerm", request.getSearchTerm());
                result.put("results", results);
                result.put("count", results.size());
                result.put("totalCount", totalCount);
                
                logger.info("Search completed. Found " + totalCount + " total results, returning " + results.size());
                return ResponseEntity.ok(ApiResponse.success(result));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error("Search term is required"));
            }
        } catch (Exception e) {
            logger.severe("Error searching functions: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}

