package controller;

import dto.ApiResponse;
import dto.AssignRoleRequest;
import dto.RoleRequest;
import dto.RoleResponse;
import dto.UserResponse;
import service.RoleService;
import service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/roles")
public class RoleController {
    private static final Logger logger = Logger.getLogger(RoleController.class.getName());

    @Autowired
    private RoleService roleService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<RoleResponse>> createRole(@RequestBody RoleRequest request) {
        logger.info("POST /api/roles - Creating role: " + request.getRoleName());
        try {
            RoleResponse response = roleService.createRole(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Role created successfully", response));
        } catch (Exception e) {
            logger.severe("Error creating role: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<RoleResponse>> getRoleById(@PathVariable Long id) {
        logger.info("GET /api/roles/" + id);
        try {
            RoleResponse response = roleService.getRoleById(id);
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (Exception e) {
            logger.severe("Error getting role: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<List<RoleResponse>>> getAllRoles() {
        logger.info("GET /api/roles - Getting all roles");
        try {
            List<RoleResponse> response = roleService.getAllRoles();
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (Exception e) {
            logger.severe("Error getting roles: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<RoleResponse>> updateRole(
            @PathVariable Long id, @RequestBody RoleRequest request) {
        logger.info("PUT /api/roles/" + id);
        try {
            RoleResponse response = roleService.updateRole(id, request);
            return ResponseEntity.ok(ApiResponse.success("Role updated successfully", response));
        } catch (Exception e) {
            logger.severe("Error updating role: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteRole(@PathVariable Long id) {
        logger.info("DELETE /api/roles/" + id);
        try {
            roleService.deleteRole(id);
            return ResponseEntity.ok(ApiResponse.success("Role deleted successfully", null));
        } catch (Exception e) {
            logger.severe("Error deleting role: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}

