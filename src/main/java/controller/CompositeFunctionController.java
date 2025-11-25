package controller;

import dto.ApiResponse;
import dto.CompositeFunctionRequest;
import dto.CompositeFunctionResponse;
import service.CompositeFunctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/composite-functions")
public class CompositeFunctionController {
    private static final Logger logger = Logger.getLogger(CompositeFunctionController.class.getName());

    @Autowired
    private CompositeFunctionService compositeFunctionService;

    @PostMapping
    public ResponseEntity<ApiResponse<CompositeFunctionResponse>> createCompositeFunction(
            @RequestBody CompositeFunctionRequest request) {
        logger.info("POST /api/composite-functions - Creating composite function");
        try {
            CompositeFunctionResponse response = compositeFunctionService.createCompositeFunction(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Composite function created successfully", response));
        } catch (Exception e) {
            logger.severe("Error creating composite function: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CompositeFunctionResponse>> getCompositeFunctionById(@PathVariable Long id) {
        logger.info("GET /api/composite-functions/" + id);
        try {
            CompositeFunctionResponse response = compositeFunctionService.getCompositeFunctionById(id);
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (Exception e) {
            logger.severe("Error getting composite function: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CompositeFunctionResponse>> updateCompositeFunction(
            @PathVariable Long id, @RequestBody CompositeFunctionRequest request) {
        logger.info("PUT /api/composite-functions/" + id);
        try {
            CompositeFunctionResponse response = compositeFunctionService.updateCompositeFunction(id, request);
            return ResponseEntity.ok(ApiResponse.success("Composite function updated successfully", response));
        } catch (Exception e) {
            logger.severe("Error updating composite function: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCompositeFunction(@PathVariable Long id) {
        logger.info("DELETE /api/composite-functions/" + id);
        try {
            compositeFunctionService.deleteCompositeFunction(id);
            return ResponseEntity.ok(ApiResponse.success("Composite function deleted successfully", null));
        } catch (Exception e) {
            logger.severe("Error deleting composite function: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}

