package manual.servlet;

import manual.DatabaseConnection;
import manual.dto.CompositeFunctionResponse;
import manual.dto.CreateCompositeFunctionRequest;
import manual.repository.CompositeFunctionRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet(name = "CompositeFunctionServlet", urlPatterns = {"/api/composite-functions/*"})
public class CompositeFunctionServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        setRequestPath(request, response);
        Long id = parseIdFromPath(request.getPathInfo());
        if (id == null) {
            sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid ID");
            return;
        }
        try (Connection conn = DatabaseConnection.getConnection()) {
            CompositeFunctionRepository repo = new CompositeFunctionRepository(conn);
            CompositeFunctionResponse composite = repo.findById(id);
            if (composite == null) {
                sendError(request, response, HttpServletResponse.SC_NOT_FOUND, "Not found");
            } else {
                sendSuccess(request, response, composite);
            }
        } catch (SQLException e) {
            sendError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        setRequestPath(request, response);
        try {
            CreateCompositeFunctionRequest req = parseJsonRequest(request, CreateCompositeFunctionRequest.class);
            if (req.getUserId() == null) {
                sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "userId required");
                return;
            }
            sendError(request, response, HttpServletResponse.SC_NOT_FOUND, "User not found");
        } catch (IOException e) {
            sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid request");
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        setRequestPath(request, response);
        Long id = parseIdFromPath(request.getPathInfo());
        if (id == null) {
            sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid ID");
            return;
        }
        try (Connection conn = DatabaseConnection.getConnection()) {
            CompositeFunctionRepository repo = new CompositeFunctionRepository(conn);
            CompositeFunctionResponse composite = repo.findById(id);
            if (composite == null) {
                sendError(request, response, HttpServletResponse.SC_NOT_FOUND, "Not found");
            } else {
                sendSuccess(request, response, composite);
            }
        } catch (SQLException e) {
            sendError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        setRequestPath(request, response);
        Long id = parseIdFromPath(request.getPathInfo());
        if (id == null) {
            sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid ID");
            return;
        }
        try (Connection conn = DatabaseConnection.getConnection()) {
            CompositeFunctionRepository repo = new CompositeFunctionRepository(conn);
            if (repo.delete(id)) {
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } else {
                sendError(request, response, HttpServletResponse.SC_NOT_FOUND, "Not found");
            }
        } catch (SQLException e) {
            sendError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        }
    }
}
