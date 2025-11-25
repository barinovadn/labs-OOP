package controller;

import dto.ApiResponse;
import dto.AssignRoleRequest;
import dto.UserRequest;
import dto.UserResponse;
import service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger logger = Logger.getLogger(AuthController.class.getName());

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(@RequestBody UserRequest request) {
        logger.info("POST /api/auth/register - Registering new user: " + request.getUsername());
        try {
            UserResponse response = userService.createUser(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("User registered successfully", response));
        } catch (Exception e) {
            logger.severe("Error registering user: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/assign-roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> assignRoles(@RequestBody AssignRoleRequest request) {
        logger.info("POST /api/auth/assign-roles - Assigning roles to user ID: " + request.getUserId());
        try {
            UserResponse response = userService.assignRoles(request);
            return ResponseEntity.ok(ApiResponse.success("Roles assigned successfully", response));
        } catch (Exception e) {
            logger.severe("Error assigning roles: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}

