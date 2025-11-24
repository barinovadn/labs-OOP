package manual.servlet;

import manual.repository.RoleRepository;
import manual.DatabaseConnection;
import manual.dto.ApiResponse;
import manual.dto.CreateUserRequest;
import manual.dto.UserResponse;
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
import java.util.List;

@WebServlet(name = "UserServlet", urlPatterns = {"/api/users/*"})
public class UserServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        setRequestPath(request, response);
        logger.info("GET request to UserServlet: " + request.getPathInfo());

        SecurityContext securityContext = getSecurityContext(request);
        if (securityContext == null || !securityContext.isAuthenticated()) {
            logger.warning("Unauthenticated request to UserServlet");
            sendError(request, response, HttpServletResponse.SC_UNAUTHORIZED, "Authentication required");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            UserRepository repository = new UserRepository(conn);
            String pathInfo = request.getPathInfo();

            if (pathInfo == null || pathInfo.equals("/")) {
                if (!RoleChecker.canReadAll(securityContext)) {
                    logger.warning("User " + securityContext.getUserId() + " attempted to list all users without permission");
                    sendError(request, response, HttpServletResponse.SC_FORBIDDEN, "Insufficient permissions");
                    return;
                }
                logger.info("User " + securityContext.getUserId() + " listing all users");
                List<UserResponse> users = repository.findAll();
                sendSuccess(request, response, users);
            } else {
                Long userId = parseIdFromPath(pathInfo);
                if (userId == null) {
                    sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID");
                    return;
                }

                if (!RoleChecker.isOwnerOrAdmin(securityContext, userId)) {
                    logger.warning("User " + securityContext.getUserId() + " attempted to access user " + userId + " without permission");
                    sendError(request, response, HttpServletResponse.SC_FORBIDDEN, "Insufficient permissions");
                    return;
                }

                logger.info("User " + securityContext.getUserId() + " accessing user " + userId);
                UserResponse user = repository.findById(userId);
                if (user == null) {
                    sendError(request, response, HttpServletResponse.SC_NOT_FOUND, "User not found");
                } else {
                    sendSuccess(request, response, user);
                }
            }
        } catch (SQLException e) {
            logger.severe("Database error in UserServlet: " + e.getMessage());
            sendError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        setRequestPath(request, response);
        logger.info("POST request to UserServlet");

        Connection conn = null;
        try {
            CreateUserRequest createRequest = parseJsonRequest(request, CreateUserRequest.class);
            
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // НАЧАЛО ТРАНЗАКЦИИ
            
            UserRepository repository = new UserRepository(conn);
            UserResponse user = repository.create(createRequest);
            
            RoleRepository roleRepo = new RoleRepository(conn);
            roleRepo.assignRoleToUser(user.getUserId(), "USER");
            logger.info("Assigned USER role to new user: " + user.getUserId());
            
            conn.commit(); // КОНЕЦ ТРАНЗАКЦИИ
            
            response.setStatus(HttpServletResponse.SC_CREATED);
            sendSuccess(request, response, user);
            
        } catch (Exception e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) {}
            }
            logger.severe("Error creating user: " + e.getMessage());
            sendError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to create user");
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException e) {}
            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        setRequestPath(request, response);
        logger.info("PUT request to UserServlet: " + request.getPathInfo());

        SecurityContext securityContext = getSecurityContext(request);
        if (securityContext == null || !securityContext.isAuthenticated()) {
            logger.warning("Unauthenticated request to update user");
            sendError(request, response, HttpServletResponse.SC_UNAUTHORIZED, "Authentication required");
            return;
        }

        try {
            Long userId = parseIdFromPath(request.getPathInfo());
            if (userId == null) {
                sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID");
                return;
            }

            if (!RoleChecker.isOwnerOrAdmin(securityContext, userId)) {
                logger.warning("User " + securityContext.getUserId() + " attempted to update user " + userId + " without permission");
                sendError(request, response, HttpServletResponse.SC_FORBIDDEN, "Insufficient permissions");
                return;
            }

            CreateUserRequest updateRequest = parseJsonRequest(request, CreateUserRequest.class);
            
            logger.info("User " + securityContext.getUserId() + " updating user " + userId);
            try (Connection conn = DatabaseConnection.getConnection()) {
                UserRepository repository = new UserRepository(conn);
                UserResponse user = repository.update(userId, updateRequest);
                if (user == null) {
                    sendError(request, response, HttpServletResponse.SC_NOT_FOUND, "User not found");
                } else {
                    sendSuccess(request, response, user);
                }
            } catch (SQLException e) {
                logger.severe("Database error updating user: " + e.getMessage());
                sendError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to update user");
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
        logger.info("DELETE request to UserServlet: " + request.getPathInfo());

        SecurityContext securityContext = getSecurityContext(request);
        if (securityContext == null || !securityContext.isAuthenticated()) {
            logger.warning("Unauthenticated request to delete user");
            sendError(request, response, HttpServletResponse.SC_UNAUTHORIZED, "Authentication required");
            return;
        }

        Long userId = parseIdFromPath(request.getPathInfo());
        if (userId == null) {
            sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID");
            return;
        }

        if (!RoleChecker.hasRole(securityContext, Role.ADMIN)) {
            logger.warning("User " + securityContext.getUserId() + " attempted to delete user " + userId + " without ADMIN role");
            sendError(request, response, HttpServletResponse.SC_FORBIDDEN, "Only admins can delete users");
            return;
        }

        logger.info("Admin " + securityContext.getUserId() + " deleting user " + userId);
        try (Connection conn = DatabaseConnection.getConnection()) {
            UserRepository repository = new UserRepository(conn);
            boolean deleted = repository.delete(userId);
            if (deleted) {
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } else {
                sendError(request, response, HttpServletResponse.SC_NOT_FOUND, "User not found");
            }
        } catch (SQLException e) {
            logger.severe("Database error deleting user: " + e.getMessage());
            sendError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to delete user");
        }
    }
}

