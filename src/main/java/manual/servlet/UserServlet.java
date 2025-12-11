package manual.servlet;

import manual.DatabaseConnection;
import manual.dto.CreateUserRequest;
import manual.dto.UserResponse;
import manual.repository.UserRepository;
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
        String pathInfo = request.getPathInfo();
        if ("/me".equals(pathInfo) || "/me/".equals(pathInfo)) {
            SecurityContext context = getSecurityContext(request);
            if (context == null || !context.isAuthenticated()) {
                sendError(request, response, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                return;
            }
            UserResponse me = new UserResponse(
                    context.getUserId(),
                    context.getUsername(),
                    context.getUser().getEmail(),
                    context.getUser().getCreatedAt()
            );
            sendSuccess(request, response, me);
            return;
        }
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            UserRepository repo = new UserRepository(conn);
            
            if (pathInfo == null || pathInfo.equals("/")) {
                List<UserResponse> users = repo.findAll();
                sendSuccess(request, response, users);
            } else {
                Long userId = parseIdFromPath(pathInfo);
                if (userId == null) {
                    sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID");
                    return;
                }
                UserResponse user = repo.findById(userId);
                if (user == null) {
                    sendError(request, response, HttpServletResponse.SC_NOT_FOUND, "Not found");
                } else {
                    sendSuccess(request, response, user);
                }
            }
        } catch (SQLException e) {
            sendError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        setRequestPath(request, response);
        try {
            CreateUserRequest req = parseJsonRequest(request, CreateUserRequest.class);
            try (Connection conn = DatabaseConnection.getConnection()) {
                UserRepository repo = new UserRepository(conn);
                if (repo.existsByUsernameAndUserIdNot(req.getUsername(), null)) {
                    sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Username already exists");
                    return;
                }
                if (repo.existsByEmailAndUserIdNot(req.getEmail(), null)) {
                    sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Email already exists");
                    return;
                }
                UserResponse user = repo.create(req);
                response.setStatus(HttpServletResponse.SC_CREATED);
                sendSuccess(request, response, user);
            }
        } catch (SQLException e) {
            sendError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error");
        } catch (IOException e) {
            sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid");
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        setRequestPath(request, response);
        Long userId = parseIdFromPath(request.getPathInfo());
        if (userId == null) {
            sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid ID");
            return;
        }
        try {
            CreateUserRequest req = parseJsonRequest(request, CreateUserRequest.class);
            try (Connection conn = DatabaseConnection.getConnection()) {
                UserRepository repo = new UserRepository(conn);
                if (repo.existsByUsernameAndUserIdNot(req.getUsername(), userId)) {
                    sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Username already exists");
                    return;
                }
                if (repo.existsByEmailAndUserIdNot(req.getEmail(), userId)) {
                    sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Email already exists");
                    return;
                }
                UserResponse user = repo.update(userId, req);
                if (user == null) {
                    sendError(request, response, HttpServletResponse.SC_NOT_FOUND, "Not found");
                } else {
                    sendSuccess(request, response, user);
                }
            }
        } catch (SQLException e) {
            sendError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error");
        } catch (IOException e) {
            sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        setRequestPath(request, response);
        Long userId = parseIdFromPath(request.getPathInfo());
        if (userId == null) {
            sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid ID");
            return;
        }

        SecurityContext context = getSecurityContext(request);
        if (context == null || !context.isAuthenticated()) {
            sendError(request, response, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            return;
        }
        boolean isAllowed = RoleChecker.isOwnerOrAdmin(context, userId);
        if (!isAllowed) {
            sendError(request, response, HttpServletResponse.SC_FORBIDDEN, "Forbidden");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            UserRepository repo = new UserRepository(conn);
            if (repo.delete(userId)) {
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } else {
                sendError(request, response, HttpServletResponse.SC_NOT_FOUND, "Not found");
            }
        } catch (SQLException e) {
            sendError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error");
        }
    }
}
