package controller;

import dto.ApiResponse;
import dto.CompositeFunctionResponse;
import dto.FunctionResponse;
import dto.UserRequest;
import dto.UserResponse;
import service.CompositeFunctionService;
import service.FunctionService;
import service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private static final Logger logger = Logger.getLogger(UserController.class.getName());

    @Autowired
    private UserService userService;

    @Autowired
    private FunctionService functionService;

    @Autowired
    private CompositeFunctionService compositeFunctionService;

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@RequestBody UserRequest request) {
        logger.info("POST /api/users - Creating user");
        try {
            UserResponse response = userService.createUser(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("User created successfully", response));
        } catch (Exception e) {
            logger.severe("Error creating user: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long id) {
        logger.info("GET /api/users/" + id);
        try {
            UserResponse response = userService.getUserById(id);
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (Exception e) {
            logger.severe("Error getting user: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        logger.info("GET /api/users - Getting all users");
        try {
            List<UserResponse> response = userService.getAllUsers();
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (Exception e) {
            logger.severe("Error getting users: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(@PathVariable Long id, @RequestBody UserRequest request) {
        logger.info("PUT /api/users/" + id);
        try {
            UserResponse response = userService.updateUser(id, request);
            return ResponseEntity.ok(ApiResponse.success("User updated successfully", response));
        } catch (Exception e) {
            logger.severe("Error updating user: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        logger.info("DELETE /api/users/" + id);
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok(ApiResponse.success("User deleted successfully", null));
        } catch (Exception e) {
            logger.severe("Error deleting user: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/{userId}/functions")
    public ResponseEntity<ApiResponse<List<FunctionResponse>>> getFunctionsByUserId(@PathVariable Long userId) {
        logger.info("GET /api/users/" + userId + "/functions");
        try {
            List<FunctionResponse> response = functionService.getFunctionsByUserId(userId);
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (Exception e) {
            logger.severe("Error getting functions: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/{userId}/composite-functions")
    public ResponseEntity<ApiResponse<List<CompositeFunctionResponse>>> getCompositeFunctionsByUserId(
            @PathVariable Long userId) {
        logger.info("GET /api/users/" + userId + "/composite-functions");
        try {
            List<CompositeFunctionResponse> response = compositeFunctionService.getCompositeFunctionsByUserId(userId);
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (Exception e) {
            logger.severe("Error getting composite functions: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}

