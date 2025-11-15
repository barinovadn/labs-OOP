package manual.servlet;

import manual.DatabaseConnection;
import manual.dto.ApiResponse;
import manual.dto.CreateUserRequest;
import manual.dto.UserResponse;
import manual.repository.UserRepository;

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

        try (Connection conn = DatabaseConnection.getConnection()) {
            UserRepository repository = new UserRepository(conn);
            String pathInfo = request.getPathInfo();

            if (pathInfo == null || pathInfo.equals("/")) {
                List<UserResponse> users = repository.findAll();
                sendSuccess(request, response, users);
            } else {
                Long userId = parseIdFromPath(pathInfo);
                if (userId == null) {
                    sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID");
                    return;
                }

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

        try {
            CreateUserRequest createRequest = parseJsonRequest(request, CreateUserRequest.class);
            
            try (Connection conn = DatabaseConnection.getConnection()) {
                UserRepository repository = new UserRepository(conn);
                UserResponse user = repository.create(createRequest);
                response.setStatus(HttpServletResponse.SC_CREATED);
                sendSuccess(request, response, user);
            } catch (SQLException e) {
                logger.severe("Database error creating user: " + e.getMessage());
                sendError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to create user");
            }
        } catch (IOException e) {
            logger.severe("Error parsing request: " + e.getMessage());
            sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid request body");
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        setRequestPath(request, response);
        logger.info("PUT request to UserServlet: " + request.getPathInfo());

        try {
            Long userId = parseIdFromPath(request.getPathInfo());
            if (userId == null) {
                sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID");
                return;
            }

            CreateUserRequest updateRequest = parseJsonRequest(request, CreateUserRequest.class);
            
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

        Long userId = parseIdFromPath(request.getPathInfo());
        if (userId == null) {
            sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID");
            return;
        }

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

