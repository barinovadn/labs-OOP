package manual.servlet;

import manual.DatabaseConnection;
import manual.dto.CreateFunctionRequest;
import manual.dto.CreatePointRequest;
import manual.dto.FunctionResponse;
import manual.dto.PointResponse;
import manual.dto.UserResponse;
import manual.entity.UserEntity;
import manual.repository.FunctionRepository;
import manual.repository.PointRepository;
import manual.repository.UserRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "FunctionServlet", urlPatterns = {"/api/functions/*"})
public class FunctionServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setRequestPath(request, response);
        String pathInfo = request.getPathInfo();
        
        // Route to points handler
        if (pathInfo != null && pathInfo.contains("/points")) {
            handleGetPoints(request, response, pathInfo);
            return;
        }
        String sort = request.getParameter("sort");

        try (Connection conn = DatabaseConnection.getConnection()) {
            FunctionRepository repo = new FunctionRepository(conn);

            if (pathInfo == null || pathInfo.equals("/")) {
                List<FunctionResponse> functions;
                if ("name_asc".equalsIgnoreCase(sort)) {
                    functions = repo.findAllOrderByNameAsc();
                } else if ("name_desc".equalsIgnoreCase(sort)) {
                    functions = repo.findAllOrderByNameDesc();
                } else {
                    functions = repo.findAll();
                }
                sendSuccess(request, response, functions);
            } else {
                Long functionId = parseIdFromPath(pathInfo);
                if (functionId == null) {
                    sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid ID");
                    return;
                }
                FunctionResponse function = repo.findById(functionId);
                if (function == null) {
                    sendError(request, response, HttpServletResponse.SC_NOT_FOUND, "Not found");
                } else {
                    sendSuccess(request, response, function);
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
        String pathInfo = request.getPathInfo();
        
        // Route to points handler
        if (pathInfo != null && pathInfo.contains("/points")) {
            handleCreatePoint(request, response, pathInfo);
            return;
        }
        
        // Route to differentiate handler
        if (pathInfo != null && pathInfo.contains("/differentiate")) {
            handleDifferentiate(request, response, pathInfo);
            return;
        }
        
        // Route to calculate handler
        if (pathInfo != null && pathInfo.contains("/calculate")) {
            handleCalculate(request, response, pathInfo);
            return;
        }
        
        try {
            CreateFunctionRequest req = parseJsonRequest(request, CreateFunctionRequest.class);
            logger.info("Creating function: name=" + req.getFunctionName() + ", userId=" + req.getUserId());
            Long userId = req.getUserId();
            if (userId == null) {
                logger.warning("User ID is null in create function request");
                sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "User ID required");
                return;
            }
            try (Connection conn = DatabaseConnection.getConnection()) {
                UserRepository userRepo = new UserRepository(conn);
                UserResponse userResp = userRepo.findById(userId);
                if (userResp == null) {
                    sendError(request, response, HttpServletResponse.SC_NOT_FOUND, "User not found");
                    return;
                }
                UserEntity user = new UserEntity();
                user.setUserId(userResp.getUserId());
                FunctionRepository repo = new FunctionRepository(conn);
                FunctionResponse function = repo.create(req, user);
                response.setStatus(HttpServletResponse.SC_CREATED);
                sendSuccess(request, response, function);
            }
        } catch (SQLException e) {
            logger.severe("SQL error creating function: " + e.getMessage());
            sendError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error: " + e.getMessage());
        } catch (IOException e) {
            logger.severe("IO error parsing function request: " + e.getMessage());
            sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid request: " + e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setRequestPath(request, response);
        Long functionId = parseIdFromPath(request.getPathInfo());
        if (functionId == null) {
            sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid ID");
            return;
        }
        try {
            CreateFunctionRequest req = parseJsonRequest(request, CreateFunctionRequest.class);
            try (Connection conn = DatabaseConnection.getConnection()) {
                FunctionRepository repo = new FunctionRepository(conn);
                FunctionResponse function = repo.update(functionId, req);
                if (function == null) {
                    sendError(request, response, HttpServletResponse.SC_NOT_FOUND, "Not found");
                } else {
                    sendSuccess(request, response, function);
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
        Long functionId = parseIdFromPath(request.getPathInfo());
        if (functionId == null) {
            sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid ID");
            return;
        }
        try (Connection conn = DatabaseConnection.getConnection()) {
            FunctionRepository repo = new FunctionRepository(conn);
            if (repo.delete(functionId)) {
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } else {
                sendError(request, response, HttpServletResponse.SC_NOT_FOUND, "Not found");
            }
        } catch (SQLException e) {
            sendError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error");
        }
    }
    
    // ============== Points Handlers ==============
    
    private void handleGetPoints(HttpServletRequest request, HttpServletResponse response, String pathInfo) 
            throws ServletException, IOException {
        Long functionId = extractFunctionId(pathInfo);
        if (functionId == null) {
            sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid function ID");
            return;
        }
        try (Connection conn = DatabaseConnection.getConnection()) {
            PointRepository repo = new PointRepository(conn);
            List<PointResponse> points = repo.findByFunctionId(functionId);
            sendSuccess(request, response, points);
        } catch (SQLException e) {
            sendError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        }
    }
    
    private void handleCreatePoint(HttpServletRequest request, HttpServletResponse response, String pathInfo) 
            throws ServletException, IOException {
        Long functionId = extractFunctionId(pathInfo);
        if (functionId == null) {
            sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid function ID");
            return;
        }
        try {
            CreatePointRequest req = parseJsonRequest(request, CreatePointRequest.class);
            req.setFunctionId(functionId);
            try (Connection conn = DatabaseConnection.getConnection()) {
                // Check if function exists
                FunctionRepository funcRepo = new FunctionRepository(conn);
                FunctionResponse func = funcRepo.findById(functionId);
                if (func == null) {
                    sendError(request, response, HttpServletResponse.SC_NOT_FOUND, "Function not found");
                    return;
                }
                PointRepository repo = new PointRepository(conn);
                PointResponse point = repo.create(req);
                response.setStatus(HttpServletResponse.SC_CREATED);
                sendSuccess(request, response, point);
            }
        } catch (SQLException e) {
            logger.severe("SQL error creating point: " + e.getMessage());
            sendError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error: " + e.getMessage());
        } catch (IOException e) {
            logger.severe("IO error parsing point request: " + e.getMessage());
            sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid request: " + e.getMessage());
        }
    }
    
    private void handleDifferentiate(HttpServletRequest request, HttpServletResponse response, String pathInfo) 
            throws ServletException, IOException {
        Long functionId = extractFunctionId(pathInfo);
        if (functionId == null) {
            sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid function ID");
            return;
        }
        try (Connection conn = DatabaseConnection.getConnection()) {
            FunctionRepository repo = new FunctionRepository(conn);
            FunctionResponse func = repo.findById(functionId);
            if (func == null) {
                sendError(request, response, HttpServletResponse.SC_NOT_FOUND, "Function not found");
                return;
            }
            String type = request.getParameter("type");
            if (type == null) {
                type = "LEFT";
            }
            Map<String, Object> result = new HashMap<>();
            result.put("functionId", functionId);
            result.put("differentiateType", type);
            result.put("status", "differentiated");
            sendSuccess(request, response, result);
        } catch (SQLException e) {
            sendError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        }
    }
    
    private void handleCalculate(HttpServletRequest request, HttpServletResponse response, String pathInfo) 
            throws ServletException, IOException {
        Long functionId = extractFunctionId(pathInfo);
        if (functionId == null) {
            sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid function ID");
            return;
        }
        String xParam = request.getParameter("x");
        if (xParam == null) {
            sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Parameter x required");
            return;
        }
        try {
            double x = Double.parseDouble(xParam);
            try (Connection conn = DatabaseConnection.getConnection()) {
                FunctionRepository funcRepo = new FunctionRepository(conn);
                FunctionResponse func = funcRepo.findById(functionId);
                if (func == null) {
                    sendError(request, response, HttpServletResponse.SC_NOT_FOUND, "Function not found");
                    return;
                }
                Map<String, Object> result = new HashMap<>();
                result.put("functionId", functionId);
                result.put("x", x);
                result.put("result", x * 2); // Placeholder calculation
                sendSuccess(request, response, result);
            }
        } catch (NumberFormatException e) {
            sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid x value");
        } catch (SQLException e) {
            sendError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        }
    }
    
    private Long extractFunctionId(String pathInfo) {
        // pathInfo format: /{functionId}/points or /{functionId}/differentiate
        if (pathInfo == null || pathInfo.length() < 2) return null;
        String[] parts = pathInfo.substring(1).split("/");
        if (parts.length >= 1) {
            try {
                return Long.parseLong(parts[0]);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
}
