package manual.servlet;

import manual.DatabaseConnection;
import manual.dto.FunctionResponse;
import manual.repository.FunctionRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "UserFunctionServlet", urlPatterns = {"/api/users/*/functions"})
public class UserFunctionServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        setRequestPath(request, response);
        String pathInfo = request.getPathInfo();
        logger.info("GET request to UserFunctionServlet: " + pathInfo);

        try {
            String[] parts = pathInfo.split("/");
            Long userId = null;
            for (int i = 0; i < parts.length; i++) {
                if (parts[i].equals("users") && i + 1 < parts.length) {
                    try {
                        userId = Long.parseLong(parts[i + 1]);
                        break;
                    } catch (NumberFormatException e) {
                        // continue
                    }
                }
            }

            if (userId == null) {
                sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID");
                return;
            }

            try (Connection conn = DatabaseConnection.getConnection()) {
                FunctionRepository repository = new FunctionRepository(conn);
                List<FunctionResponse> functions = repository.findByUserId(userId);
                sendSuccess(request, response, functions);
            } catch (SQLException e) {
                logger.severe("Database error in UserFunctionServlet: " + e.getMessage());
                sendError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
            }
        } catch (Exception e) {
            logger.severe("Error in UserFunctionServlet: " + e.getMessage());
            sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal server error");
        }
    }
}

