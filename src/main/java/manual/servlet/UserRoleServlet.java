package manual.servlet;

import manual.DatabaseConnection;
import manual.repository.RoleRepository;
import manual.security.Role;

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

@WebServlet(name = "UserRoleServlet", urlPatterns = {"/api/users/*/roles"})
public class UserRoleServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        setRequestPath(request, response);
        logger.info("GET request to UserRoleServlet");

        Long userId = parseIdFromPath(request.getPathInfo());
        if (userId == null) {
            sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            RoleRepository roleRepo = new RoleRepository(conn);
            List<Role> roles = roleRepo.findRolesByUserId(userId);
            
            Map<String, Object> data = new HashMap<>();
            data.put("userId", userId);
            data.put("roles", roles.stream().map(Role::getRoleName).toArray());
            
            sendSuccess(request, response, data);
        } catch (SQLException e) {
            logger.severe("Database error: " + e.getMessage());
            sendError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        setRequestPath(request, response);
        logger.info("POST request to UserRoleServlet");

        Long userId = parseIdFromPath(request.getPathInfo());
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

            try (Connection conn = DatabaseConnection.getConnection()) {
                RoleRepository roleRepo = new RoleRepository(conn);
                boolean assigned = roleRepo.assignRoleToUser(userId, roleName);
                
                if (assigned) {
                    Map<String, Object> data = new HashMap<>();
                    data.put("userId", userId);
                    data.put("roleName", roleName);
                    response.setStatus(HttpServletResponse.SC_CREATED);
                    sendSuccess(request, response, data);
                } else {
                    sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Failed to assign role");
                }
            }
        } catch (SQLException e) {
            logger.severe("Database error: " + e.getMessage());
            sendError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        } catch (IOException e) {
            sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid request");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        setRequestPath(request, response);
        logger.info("DELETE request to UserRoleServlet");

        Long userId = parseIdFromPath(request.getPathInfo());
        if (userId == null) {
            sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID");
            return;
        }

        String roleName = request.getParameter("roleName");
        if (roleName == null || roleName.isEmpty()) {
            sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "roleName parameter is required");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            RoleRepository roleRepo = new RoleRepository(conn);
            boolean removed = roleRepo.removeRoleFromUser(userId, roleName);
            
            if (removed) {
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } else {
                sendError(request, response, HttpServletResponse.SC_NOT_FOUND, "Role not found");
            }
        } catch (SQLException e) {
            logger.severe("Database error: " + e.getMessage());
            sendError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        }
    }
}
