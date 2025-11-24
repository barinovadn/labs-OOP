package manual.servlet;

import manual.DatabaseConnection;
import manual.repository.RoleRepository;
import manual.repository.UserRepository;
import manual.security.Role;
import manual.security.RoleChecker;
import manual.security.SecurityContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// @WebServlet(name = "UserRoleServlet", urlPatterns = {"/api/users/*/roles"})
public class UserRoleServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        setRequestPath(request, response);
        String pathInfo = request.getPathInfo();
        logger.info("GET request to UserRoleServlet. PathInfo: '" + pathInfo + "'");
        logger.info("Full URL: " + request.getRequestURL());

        SecurityContext securityContext = getSecurityContext(request);
        if (securityContext == null || !securityContext.isAuthenticated()) {
            logger.warning("Unauthenticated request to get user roles");
            sendError(request, response, HttpServletResponse.SC_UNAUTHORIZED, "Authentication required");
            return;
        }

        Long userId = extractUserIdFromPath(request.getPathInfo());
        if (userId == null) {
            sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID");
            return;
        }

        if (!RoleChecker.isOwnerOrAdmin(securityContext, userId)) {
            logger.warning("User " + securityContext.getUserId() + " attempted to access roles for user " + userId + " without permission");
            sendError(request, response, HttpServletResponse.SC_FORBIDDEN, "Insufficient permissions");
            return;
        }

        logger.info("User " + securityContext.getUserId() + " getting roles for user " + userId);
        try (Connection conn = DatabaseConnection.getConnection()) {
            RoleRepository roleRepo = new RoleRepository(conn);
            List<Role> roles = roleRepo.findRolesByUserId(userId);
            
            Map<String, Object> data = new HashMap<>();
            data.put("userId", userId);
            data.put("roles", roles.stream().map(Role::getRoleName).toArray());
            
            sendSuccess(request, response, data);
        } catch (SQLException e) {
            logger.severe("Database error getting user roles: " + e.getMessage());
            sendError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        setRequestPath(request, response);
        logger.info("POST request to UserRoleServlet: " + request.getPathInfo());

        SecurityContext securityContext = getSecurityContext(request);
        if (securityContext == null || !securityContext.isAuthenticated()) {
            logger.warning("Unauthenticated request to assign role");
            sendError(request, response, HttpServletResponse.SC_UNAUTHORIZED, "Authentication required");
            return;
        }

        if (!RoleChecker.hasRole(securityContext, Role.ADMIN)) {
            logger.warning("User " + securityContext.getUserId() + " attempted to assign role without ADMIN role");
            sendError(request, response, HttpServletResponse.SC_FORBIDDEN, "Only admins can assign roles");
            return;
        }

        Long userId = extractUserIdFromPath(request.getPathInfo());
        if (userId == null) {
            sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID");
            return;
        }

        try {
            Map<String, String> requestData = parseJsonRequest(request, Map.class);
            String roleName = requestData.get("roleName");
            
            if (roleName == null || roleName.isEmpty()) {
                sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "roleName is required");
                return;
            }

            Role role = Role.fromString(roleName);
            if (role == null) {
                sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid role name");
                return;
            }

            logger.info("Admin " + securityContext.getUserId() + " assigning role " + roleName + " to user " + userId);
            try (Connection conn = DatabaseConnection.getConnection()) {
                UserRepository userRepo = new UserRepository(conn);
                if (userRepo.findById(userId) == null) {
                    sendError(request, response, HttpServletResponse.SC_NOT_FOUND, "User not found");
                    return;
                }

                RoleRepository roleRepo = new RoleRepository(conn);
                boolean assigned = roleRepo.assignRoleToUser(userId, roleName);
                
                if (assigned) {
                    Map<String, Object> data = new HashMap<>();
                    data.put("userId", userId);
                    data.put("roleName", roleName);
                    data.put("message", "Role assigned successfully");
                    response.setStatus(HttpServletResponse.SC_CREATED);
                    sendSuccess(request, response, data);
                } else {
                    sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Failed to assign role");
                }
            } catch (SQLException e) {
                logger.severe("Database error assigning role: " + e.getMessage());
                sendError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
            }
        } catch (IOException e) {
            logger.severe("Error parsing request: " + e.getMessage());
            sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid request body");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        setRequestPath(request, response);
        logger.info("DELETE request to UserRoleServlet: " + request.getPathInfo());

        SecurityContext securityContext = getSecurityContext(request);
        if (securityContext == null || !securityContext.isAuthenticated()) {
            logger.warning("Unauthenticated request to remove role");
            sendError(request, response, HttpServletResponse.SC_UNAUTHORIZED, "Authentication required");
            return;
        }

        if (!RoleChecker.hasRole(securityContext, Role.ADMIN)) {
            logger.warning("User " + securityContext.getUserId() + " attempted to remove role without ADMIN role");
            sendError(request, response, HttpServletResponse.SC_FORBIDDEN, "Only admins can remove roles");
            return;
        }

        Long userId = extractUserIdFromPath(request.getPathInfo());
        if (userId == null) {
            sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID");
            return;
        }

        String roleName = request.getParameter("roleName");
        if (roleName == null || roleName.isEmpty()) {
            sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "roleName parameter is required");
            return;
        }

        logger.info("Admin " + securityContext.getUserId() + " removing role " + roleName + " from user " + userId);
        try (Connection conn = DatabaseConnection.getConnection()) {
            RoleRepository roleRepo = new RoleRepository(conn);
            boolean removed = roleRepo.removeRoleFromUser(userId, roleName);
            
            if (removed) {
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } else {
                sendError(request, response, HttpServletResponse.SC_NOT_FOUND, "Role not found or not assigned");
            }
        } catch (SQLException e) {
            logger.severe("Database error removing role: " + e.getMessage());
            sendError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        }
    }

    private Long extractUserIdFromPath(String pathInfo) {
        logger.info("Extracting user ID from path: '" + pathInfo + "'");
        if (pathInfo == null || pathInfo.equals("/")) {
            return null;
        }
        try {
            String idStr = pathInfo.substring(1);
            return Long.parseLong(idStr);
        } catch (Exception e) {
            logger.warning("Failed to parse user ID from path: " + pathInfo);
            return null;
        }
    }
}


