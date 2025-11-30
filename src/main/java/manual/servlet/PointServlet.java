package manual.servlet;

import manual.DatabaseConnection;
import manual.dto.CreatePointRequest;
import manual.dto.PointResponse;
import manual.repository.PointRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "PointServlet", urlPatterns = {"/api/points/*", "/api/functions/*/points", "/api/functions/*/calculate"})
public class PointServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        setRequestPath(request, response);
        String uri = request.getRequestURI();
        try (Connection conn = DatabaseConnection.getConnection()) {
            PointRepository repo = new PointRepository(conn);
            if (uri.contains("/functions/") && uri.contains("/points")) {
                Long funcId = extractFunctionId(uri);
                if (funcId == null) {
                    sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid function ID");
                    return;
                }
                List<PointResponse> points = repo.findByFunctionId(funcId);
                sendSuccess(request, response, points);
            } else {
                Long pointId = parseIdFromPath(request.getPathInfo());
                if (pointId == null) {
                    sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid point ID");
                    return;
                }
                PointResponse point = repo.findById(pointId);
                if (point == null) {
                    sendError(request, response, HttpServletResponse.SC_NOT_FOUND, "Not found");
                } else {
                    sendSuccess(request, response, point);
                }
            }
        } catch (SQLException e) {
            sendError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        setRequestPath(request, response);
        String uri = request.getRequestURI();
        if (uri.contains("/calculate")) {
            Long funcId = extractFunctionId(uri);
            if (funcId == null) {
                sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid function ID");
                return;
            }
            String x = request.getParameter("x");
            if (x == null) {
                sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Parameter x required");
                return;
            }
            try {
                Double.parseDouble(x);
                sendError(request, response, HttpServletResponse.SC_NOT_FOUND, "Point not found");
            } catch (NumberFormatException e) {
                sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid x");
            }
        } else if (uri.contains("/points")) {
            Long funcId = extractFunctionId(uri);
            if (funcId == null) {
                sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid function ID");
                return;
            }
            sendError(request, response, HttpServletResponse.SC_NOT_FOUND, "Function not found");
        } else {
            sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid path");
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        setRequestPath(request, response);
        Long pointId = parseIdFromPath(request.getPathInfo());
        if (pointId == null) {
            sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid point ID");
            return;
        }
        try {
            CreatePointRequest req = parseJsonRequest(request, CreatePointRequest.class);
            try (Connection conn = DatabaseConnection.getConnection()) {
                PointRepository repo = new PointRepository(conn);
                PointResponse point = repo.update(pointId, req);
                if (point == null) {
                    sendError(request, response, HttpServletResponse.SC_NOT_FOUND, "Not found");
                } else {
                    sendSuccess(request, response, point);
                }
            }
        } catch (SQLException e) {
            sendError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        } catch (IOException e) {
            sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid request");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        setRequestPath(request, response);
        Long pointId = parseIdFromPath(request.getPathInfo());
        if (pointId == null) {
            sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid point ID");
            return;
        }
        try (Connection conn = DatabaseConnection.getConnection()) {
            PointRepository repo = new PointRepository(conn);
            if (repo.delete(pointId)) {
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } else {
                sendError(request, response, HttpServletResponse.SC_NOT_FOUND, "Not found");
            }
        } catch (SQLException e) {
            sendError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        }
    }

    private Long extractFunctionId(String uri) {
        String[] parts = uri.split("/");
        for (int i = 0; i < parts.length; i++) {
            if ("functions".equals(parts[i]) && i + 1 < parts.length) {
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
