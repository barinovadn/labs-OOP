package manual.servlet;

import manual.DatabaseConnection;
import manual.dto.FunctionResponse;
import manual.search.SearchCriteria;
import manual.repository.FunctionRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "FunctionSearchServlet", urlPatterns = {"/api/functions/search"})
public class FunctionSearchServlet extends BaseServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        setRequestPath(request, response);
        try {
            SearchCriteria criteria = parseJsonRequest(request, SearchCriteria.class);
            try (Connection conn = DatabaseConnection.getConnection()) {
                FunctionRepository repo = new FunctionRepository(conn);
                List<FunctionResponse> results = repo.findAll();
                sendSuccess(request, response, results);
            }
        } catch (SQLException e) {
            sendError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error");
        } catch (IOException e) {
            sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid");
        }
    }
}
