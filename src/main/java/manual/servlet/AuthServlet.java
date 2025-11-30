package manual.servlet;

import manual.DatabaseConnection;
import manual.dto.CreateUserRequest;
import manual.dto.UserResponse;
import manual.repository.RoleRepository;
import manual.repository.UserRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet(name = "AuthServlet", urlPatterns = {"/api/auth/*"})
public class AuthServlet extends BaseServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setRequestPath(request, response);

        String pathInfo = request.getPathInfo();
        if (!"/register".equalsIgnoreCase(pathInfo)) {
            sendError(request, response, HttpServletResponse.SC_NOT_FOUND, "Endpoint not found");
            return;
        }

        try {
            CreateUserRequest createRequest = parseJsonRequest(request, CreateUserRequest.class);
            try (Connection conn = DatabaseConnection.getConnection()) {
                UserRepository userRepository = new UserRepository(conn);
                UserResponse createdUser = userRepository.create(createRequest);
                RoleRepository roleRepository = new RoleRepository(conn);
                roleRepository.assignRoleToUser(createdUser.getUserId(), "USER");
                response.setStatus(HttpServletResponse.SC_CREATED);
                sendSuccess(request, response, createdUser);
            }
        } catch (SQLException e) {
            logger.severe("Database error: " + e.getMessage());
            sendError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Registration failed");
        } catch (IOException e) {
            sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid request");
        }
    }
}
