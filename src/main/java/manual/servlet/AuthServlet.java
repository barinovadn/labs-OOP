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
    private static final String REGISTER_PATH = "/register";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setRequestPath(request, response);

        String pathInfo = request.getPathInfo();
        if (REGISTER_PATH.equalsIgnoreCase(pathInfo)) {
            handleRegister(request, response);
            return;
        }

        sendError(request, response, HttpServletResponse.SC_NOT_FOUND, "Endpoint not found");
    }

    private void handleRegister(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Connection conn = null;
        try {
            CreateUserRequest createRequest = parseJsonRequest(request, CreateUserRequest.class);

            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            UserRepository userRepository = new UserRepository(conn);
            UserResponse createdUser = userRepository.create(createRequest);

            RoleRepository roleRepository = new RoleRepository(conn);
            roleRepository.assignRoleToUser(createdUser.getUserId(), "USER");

            conn.commit();

            response.setStatus(HttpServletResponse.SC_CREATED);
            sendSuccess(request, response, createdUser);
        } catch (SQLException e) {
            logger.severe("Database error during registration: " + e.getMessage());
            rollbackQuietly(conn);
            sendError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to register user");
        } catch (IOException e) {
            logger.warning("Invalid registration payload: " + e.getMessage());
            sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid request body");
        } finally {
            closeQuietly(conn);
        }
    }

    private void rollbackQuietly(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException ignored) {
            }
        }
    }

    private void closeQuietly(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ignored) {
            }
        }
    }
}

