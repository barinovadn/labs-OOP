package controller;

import dto.ApiResponse;
import dto.CalculationRequest;
import dto.CalculationResponse;
import dto.FunctionRequest;
import dto.FunctionResponse;
import service.FunctionService;
import functions.TabulatedFunction;
import functions.factory.ArrayTabulatedFunctionFactory;
import functions.factory.TabulatedFunctionFactory;
import operations.TabulatedFunctionOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/functions")
public class FunctionController {
    private static final Logger logger = Logger.getLogger(FunctionController.class.getName());

    @Autowired
    private FunctionService functionService;

    private TabulatedFunctionFactory factory = new ArrayTabulatedFunctionFactory();
    private TabulatedFunctionOperationService operationService = new TabulatedFunctionOperationService();

    @PostMapping
    public ResponseEntity<ApiResponse<FunctionResponse>> createFunction(@RequestBody FunctionRequest request) {
        logger.info("POST /api/functions - Creating function");
        try {
            FunctionResponse response = functionService.createFunction(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Function created successfully", response));
        } catch (Exception e) {
            logger.severe("Error creating function: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FunctionResponse>> getFunctionById(@PathVariable Long id) {
        logger.info("GET /api/functions/" + id);
        try {
            FunctionResponse response = functionService.getFunctionById(id);
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (Exception e) {
            logger.severe("Error getting function: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }


    @GetMapping
    public ResponseEntity<ApiResponse<List<FunctionResponse>>> getAllFunctions(
            @RequestParam(required = false) String sortBy) {
        logger.info("GET /api/functions?sortBy=" + sortBy);
        try {
            List<FunctionResponse> response = functionService.getAllFunctions(sortBy);
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (Exception e) {
            logger.severe("Error getting functions: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<FunctionResponse>> updateFunction(
            @PathVariable Long id, @RequestBody FunctionRequest request) {
        logger.info("PUT /api/functions/" + id);
        try {
            FunctionResponse response = functionService.updateFunction(id, request);
            return ResponseEntity.ok(ApiResponse.success("Function updated successfully", response));
        } catch (Exception e) {
            logger.severe("Error updating function: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteFunction(@PathVariable Long id) {
        logger.info("DELETE /api/functions/" + id);
        try {
            functionService.deleteFunction(id);
            return ResponseEntity.ok(ApiResponse.success("Function deleted successfully", null));
        } catch (Exception e) {
            logger.severe("Error deleting function: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/{functionId}/calculate")
    public ResponseEntity<ApiResponse<CalculationResponse>> calculateFunction(
            @PathVariable Long functionId, @RequestBody CalculationRequest request) {
        logger.info("POST /api/functions/" + functionId + "/calculate");
        try {
            // This is a simplified implementation
            // In a real scenario, you would load the function from DB and calculate
            CalculationResponse response = new CalculationResponse();
            response.setOperation("calculate");
            response.setResult(0.0);
            response.setFunctionType("TABULATED");
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (Exception e) {
            logger.severe("Error calculating function: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}

