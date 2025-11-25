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
import repository.FunctionRepository;
import repository.PointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.logging.Logger;
import entity.FunctionEntity;
import entity.PointEntity;

@RestController
@RequestMapping("/api/functions")
public class FunctionController {
    private static final Logger logger = Logger.getLogger(FunctionController.class.getName());

    @Autowired
    private FunctionService functionService;

    @Autowired
    private FunctionRepository functionRepository;

    @Autowired
    private PointRepository pointRepository;

    private TabulatedFunctionFactory factory = new ArrayTabulatedFunctionFactory();
    private TabulatedFunctionOperationService operationService = new TabulatedFunctionOperationService();

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
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
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
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
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<List<FunctionResponse>>> getAllFunctions(
            @RequestParam(required = false) String sort) {
        logger.info("GET /api/functions?sort=" + sort);
        try {
            List<FunctionResponse> response = functionService.getAllFunctions(sort);
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (Exception e) {
            logger.severe("Error getting functions: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
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
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
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
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<CalculationResponse>> calculateFunction(
            @PathVariable Long functionId, @RequestBody CalculationRequest request) {
        logger.info("POST /api/functions/" + functionId + "/calculate - Operation: " + 
                    request.getOperation() + ", X value: " + request.getFunction());
        try {
            FunctionEntity functionEntity = functionRepository.findById(functionId)
                    .orElseThrow(() -> new RuntimeException("Function not found with id: " + functionId));
            
            List<entity.PointEntity> points = pointRepository.findByFunctionIdOrderByXValue(functionId);
            if (points.size() < 2) {
                throw new RuntimeException("Function must have at least 2 points to calculate");
            }
            
            double[] xValues = new double[points.size()];
            double[] yValues = new double[points.size()];
            for (int i = 0; i < points.size(); i++) {
                xValues[i] = points.get(i).getXValue();
                yValues[i] = points.get(i).getYValue();
            }
            TabulatedFunction tabulatedFunction = new functions.ArrayTabulatedFunction(xValues, yValues);
            
            Double xValue = null;
            if (request.getFunction() != null && request.getFunction().getXValues() != null && 
                request.getFunction().getXValues().length > 0) {
                xValue = request.getFunction().getXValues()[0];
            } else if (request.getFunction() != null && request.getFunction().getXFrom() != null) {
                xValue = request.getFunction().getXFrom();
            }
            
            if (xValue == null) {
                throw new RuntimeException("X value is required for calculation");
            }
            
            long startTime = System.currentTimeMillis();
            double result = tabulatedFunction.apply(xValue);
            long computationTime = System.currentTimeMillis() - startTime;
            
            CalculationResponse response = new CalculationResponse();
            response.setOperation(request.getOperation() != null ? request.getOperation() : "calculate");
            response.setResult(result);
            response.setFunctionType(functionEntity.getFunctionType());
            response.setComputationTimeMs(computationTime);
            response.setDetails("Calculated value at x=" + xValue + " for function " + functionId);
            
            logger.info("Function calculated successfully. Result: " + result + " at x=" + xValue);
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (Exception e) {
            logger.severe("Error calculating function: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}

