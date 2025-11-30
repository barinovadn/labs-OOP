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
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "FunctionDifferentiateServlet", urlPatterns = {"/api/functions/*/differentiate"})
public class FunctionDifferentiateServlet extends BaseServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        setRequestPath(request, response);
        Long functionId = extractId(request.getRequestURI());
        if (functionId == null) {
            sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid ID");
            return;
        }
        try (Connection conn = DatabaseConnection.getConnection()) {
            FunctionRepository repo = new FunctionRepository(conn);
            FunctionResponse func = repo.findById(functionId);
            if (func == null) {
                sendError(request, response, HttpServletResponse.SC_NOT_FOUND, "Not found");
                return;
            }
            Map<String, Object> result = new HashMap<>();
            result.put("functionId", functionId);
            result.put("status", "differentiated");
            sendSuccess(request, response, result);
        } catch (SQLException e) {
            sendError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error");
        }
    }

    private Long extractId(String uri) {
        String[] parts = uri.split("/");
        for (int i = 0; i < parts.length - 1; i++) {
            if ("functions".equals(parts[i])) {
                try {
                    return Long.parseLong(parts[i + 1]);
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        }
        return null;
    }
}
