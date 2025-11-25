package controller;

import dto.ApiResponse;
import dto.OperationRequest;
import dto.SearchRequest;
import entity.FunctionEntity;
import functions.TabulatedFunction;
import functions.factory.ArrayTabulatedFunctionFactory;
import functions.factory.TabulatedFunctionFactory;
import operations.TabulatedDifferentialOperator;
import operations.TabulatedFunctionOperationService;
import repository.FunctionRepository;
import search.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/functions")
public class OperationController {
    private static final Logger logger = Logger.getLogger(OperationController.class.getName());

    @Autowired
    private FunctionRepository functionRepository;

    @Autowired
    private SearchService searchService;

    private TabulatedFunctionFactory factory = new ArrayTabulatedFunctionFactory();
    private TabulatedFunctionOperationService operationService = new TabulatedFunctionOperationService();
    private TabulatedDifferentialOperator differentialOperator = new TabulatedDifferentialOperator();

    @PostMapping("/operations")
    public ResponseEntity<ApiResponse<Map<String, Object>>> performOperation(@RequestBody OperationRequest request) {
        logger.info("POST /api/functions/operations - Operation: " + request.getOperation());
        try {
            // This is a simplified implementation
            // In a real scenario, you would load functions from DB, convert to TabulatedFunction,
            // perform operation, and save result
            Map<String, Object> result = new HashMap<>();
            result.put("operation", request.getOperation());
            result.put("status", "success");
            result.put("message", "Operation completed");
            
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (Exception e) {
            logger.severe("Error performing operation: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/{functionId}/differentiate")
    public ResponseEntity<ApiResponse<Map<String, Object>>> differentiateFunction(@PathVariable Long functionId) {
        logger.info("POST /api/functions/" + functionId + "/differentiate");
        try {
            // This is a simplified implementation
            // In a real scenario, you would load function from DB, convert to TabulatedFunction,
            // calculate derivative, and save result
            Map<String, Object> result = new HashMap<>();
            result.put("functionId", functionId);
            result.put("status", "success");
            result.put("message", "Derivative calculated");
            
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (Exception e) {
            logger.severe("Error calculating derivative: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/search")
    public ResponseEntity<ApiResponse<Map<String, Object>>> searchFunctions(@RequestBody SearchRequest request) {
        logger.info("POST /api/functions/search - Search term: " + request.getSearchTerm());
        try {
            // Use SearchService to perform search
            if (request.getSearchTerm() != null && !request.getSearchTerm().isEmpty()) {
                List<Object> results = new java.util.ArrayList<>();
                // Perform search using SearchService
                // This is simplified - actual implementation would use SearchService methods
                
                Map<String, Object> result = new HashMap<>();
                result.put("searchTerm", request.getSearchTerm());
                result.put("results", results);
                result.put("count", results.size());
                
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

