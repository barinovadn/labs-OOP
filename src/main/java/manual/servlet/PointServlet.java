package manual.servlet;

import manual.DatabaseConnection;
import manual.api.ApiServiceBase;
import manual.dto.CreatePointRequest;
import manual.dto.PointResponse;
import manual.entity.FunctionEntity;
import manual.repository.FunctionRepository;
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
        String pathInfo = request.getPathInfo();
        logger.info("GET request to PointServlet: " + pathInfo);

        try (Connection conn = DatabaseConnection.getConnection()) {
            PointRepository repository = new PointRepository(conn);
            String requestURI = request.getRequestURI();
            
            if (requestURI.contains("/functions/") && requestURI.contains("/points")) {
                String[] parts = requestURI.split("/");
                Long functionId = null;
                for (int i = 0; i < parts.length; i++) {
                    if (parts[i].equals("functions") && i + 1 < parts.length) {
                        try {
                            functionId = Long.parseLong(parts[i + 1]);
                            break;
                        } catch (NumberFormatException e) {
                            // continue
                        }
                    }
                }
                if (functionId == null) {
                    sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid function ID");
                    return;
                }
                List<PointResponse> points = repository.findByFunctionId(functionId);
                sendSuccess(request, response, points);
            } else {
                Long pointId = parseIdFromPath(pathInfo);
                if (pointId == null) {
                    sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid point ID");
                    return;
                }
                PointResponse point = repository.findById(pointId);
                if (point == null) {
                    sendError(request, response, HttpServletResponse.SC_NOT_FOUND, "Point not found");
                } else {
                    sendSuccess(request, response, point);
                }
            }
        } catch (SQLException e) {
            logger.severe("Database error in PointServlet: " + e.getMessage());
            sendError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        setRequestPath(request, response);
        String pathInfo = request.getPathInfo();
        logger.info("POST request to PointServlet: " + pathInfo);

        try (Connection conn = DatabaseConnection.getConnection()) {
            String requestURI = request.getRequestURI();
            if (requestURI.contains("/calculate")) {
                String[] parts = requestURI.split("/");
                Long functionId = null;
                for (int i = 0; i < parts.length; i++) {
                    if (parts[i].equals("functions") && i + 1 < parts.length) {
                        try {
                            functionId = Long.parseLong(parts[i + 1]);
                            break;
                        } catch (NumberFormatException e) {
                            // continue
                        }
                    }
                }
                
                if (functionId == null) {
                    sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid function ID");
                    return;
                }

                String xParam = request.getParameter("x");
                if (xParam == null) {
                    sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Parameter x is required");
                    return;
                }

                try {
                    Double x = Double.parseDouble(xParam);
                    PointService pointService = new PointService();
                    PointResponse point = pointService.calculateFunctionValueWithCache(functionId, x);
                    sendSuccess(request, response, point);
                } catch (NumberFormatException e) {
                    sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid x parameter");
                } catch (SQLException e) {
                    logger.severe("Error calculating function value: " + e.getMessage());
                    sendError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to calculate function value");
                }
            } else if (requestURI.contains("/points")) {
                String[] parts = requestURI.split("/");
                Long functionId = null;
                for (int i = 0; i < parts.length; i++) {
                    if (parts[i].equals("functions") && i + 1 < parts.length) {
                        try {
                            functionId = Long.parseLong(parts[i + 1]);
                            break;
                        } catch (NumberFormatException e) {
                            // continue
                        }
                    }
                }
                
                if (functionId == null) {
                    sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid function ID");
                    return;
                }

                CreatePointRequest createRequest = parseJsonRequest(request, CreatePointRequest.class);
                FunctionRepository functionRepo = new FunctionRepository(conn);
                manual.dto.FunctionResponse functionResponse = functionRepo.findById(functionId);
                
                if (functionResponse == null) {
                    sendError(request, response, HttpServletResponse.SC_NOT_FOUND, "Function not found");
                    return;
                }

                FunctionEntity function = mapToEntity(functionResponse);
                PointRepository repository = new PointRepository(conn);
                PointResponse point = repository.create(createRequest, function);
                response.setStatus(HttpServletResponse.SC_CREATED);
                sendSuccess(request, response, point);
            } else {
                sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid path");
            }
        } catch (SQLException e) {
            logger.severe("Database error in PointServlet: " + e.getMessage());
            sendError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        } catch (IOException e) {
            logger.severe("Error parsing request: " + e.getMessage());
            sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid request body");
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        setRequestPath(request, response);
        logger.info("PUT request to PointServlet: " + request.getPathInfo());

        try {
            Long pointId = parseIdFromPath(request.getPathInfo());
            if (pointId == null) {
                sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid point ID");
                return;
            }

            CreatePointRequest updateRequest = parseJsonRequest(request, CreatePointRequest.class);
            
            try (Connection conn = DatabaseConnection.getConnection()) {
                PointRepository repository = new PointRepository(conn);
                PointResponse point = repository.update(pointId, updateRequest);
                if (point == null) {
                    sendError(request, response, HttpServletResponse.SC_NOT_FOUND, "Point not found");
                } else {
                    sendSuccess(request, response, point);
                }
            } catch (SQLException e) {
                logger.severe("Database error updating point: " + e.getMessage());
                sendError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to update point");
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
        logger.info("DELETE request to PointServlet: " + request.getPathInfo());

        Long pointId = parseIdFromPath(request.getPathInfo());
        if (pointId == null) {
            sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid point ID");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            PointRepository repository = new PointRepository(conn);
            boolean deleted = repository.delete(pointId);
            if (deleted) {
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } else {
                sendError(request, response, HttpServletResponse.SC_NOT_FOUND, "Point not found");
            }
        } catch (SQLException e) {
            logger.severe("Database error deleting point: " + e.getMessage());
            sendError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to delete point");
        }
    }

    private FunctionEntity mapToEntity(manual.dto.FunctionResponse response) {
        FunctionEntity entity = new FunctionEntity();
        entity.setFunctionId(response.getFunctionId());
        entity.setFunctionName(response.getFunctionName());
        entity.setFunctionType(response.getFunctionType());
        entity.setFunctionExpression(response.getFunctionExpression());
        entity.setXFrom(response.getXFrom());
        entity.setXTo(response.getXTo());
        entity.setCreatedAt(response.getCreatedAt());
        return entity;
    }

    private static class PointService extends ApiServiceBase {
        public PointResponse calculateFunctionValueWithCache(Long functionId, Double x) throws SQLException {
            return super.calculateFunctionValueWithCache(functionId, x);
        }
    }
}

