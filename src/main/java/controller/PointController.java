package controller;

import dto.ApiResponse;
import dto.PointRequest;
import dto.PointResponse;
import service.PointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api")
public class PointController {
    private static final Logger logger = Logger.getLogger(PointController.class.getName());

    @Autowired
    private PointService pointService;

    @PostMapping("/functions/{functionId}/points")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<PointResponse>> createPoint(
            @PathVariable Long functionId, @RequestBody PointRequest request) {
        logger.info("POST /api/functions/" + functionId + "/points");
        try {
            request.setFunctionId(functionId);
            PointResponse response = pointService.createPoint(functionId, request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Point created successfully", response));
        } catch (Exception e) {
            logger.severe("Error creating point: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/points/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<PointResponse>> getPointById(@PathVariable Long id) {
        logger.info("GET /api/points/" + id);
        try {
            PointResponse response = pointService.getPointById(id);
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (Exception e) {
            logger.severe("Error getting point: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/functions/{functionId}/points")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<List<PointResponse>>> getPointsByFunctionId(@PathVariable Long functionId) {
        logger.info("GET /api/functions/" + functionId + "/points");
        try {
            List<PointResponse> response = pointService.getPointsByFunctionId(functionId);
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (Exception e) {
            logger.severe("Error getting points: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/points/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<PointResponse>> updatePoint(
            @PathVariable Long id, @RequestBody PointRequest request) {
        logger.info("PUT /api/points/" + id);
        try {
            PointResponse response = pointService.updatePoint(id, request);
            return ResponseEntity.ok(ApiResponse.success("Point updated successfully", response));
        } catch (Exception e) {
            logger.severe("Error updating point: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/points/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<Void>> deletePoint(@PathVariable Long id) {
        logger.info("DELETE /api/points/" + id);
        try {
            pointService.deletePoint(id);
            return ResponseEntity.ok(ApiResponse.success("Point deleted successfully", null));
        } catch (Exception e) {
            logger.severe("Error deleting point: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}

