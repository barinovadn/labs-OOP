package manual.servlet;

import manual.DatabaseConnection;
import manual.dto.CompositeFunctionResponse;
import manual.dto.CreateCompositeFunctionRequest;
import manual.entity.FunctionEntity;
import manual.entity.UserEntity;
import manual.repository.CompositeFunctionRepository;
import manual.repository.FunctionRepository;
import manual.repository.UserRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "CompositeFunctionServlet", urlPatterns = {"/api/composite-functions/*"})
public class CompositeFunctionServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        setRequestPath(request, response);
        logger.info("GET request to CompositeFunctionServlet: " + request.getPathInfo());

        try (Connection conn = DatabaseConnection.getConnection()) {
            CompositeFunctionRepository repository = new CompositeFunctionRepository(conn);
            String pathInfo = request.getPathInfo();

            if (pathInfo == null || pathInfo.equals("/")) {
                sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Composite function ID required");
            } else {
                Long compositeId = parseIdFromPath(pathInfo);
                if (compositeId == null) {
                    sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid composite function ID");
                    return;
                }

                CompositeFunctionResponse composite = repository.findById(compositeId);
                if (composite == null) {
                    sendError(request, response, HttpServletResponse.SC_NOT_FOUND, "Composite function not found");
                } else {
                    sendSuccess(request, response, composite);
                }
            }
        } catch (SQLException e) {
            logger.severe("Database error in CompositeFunctionServlet: " + e.getMessage());
            sendError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        setRequestPath(request, response);
        logger.info("POST request to CompositeFunctionServlet");

        try {
            CreateCompositeFunctionRequest createRequest = parseJsonRequest(request, CreateCompositeFunctionRequest.class);
            Long userId = createRequest.getUserId();
            
            if (userId == null) {
                sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "User ID is required");
                return;
            }

            try (Connection conn = DatabaseConnection.getConnection()) {
                UserRepository userRepo = new UserRepository(conn);
                manual.dto.UserResponse userResponse = userRepo.findById(userId);
                if (userResponse == null) {
                    sendError(request, response, HttpServletResponse.SC_NOT_FOUND, "User not found");
                    return;
                }
                UserEntity user = mapUserToEntity(userResponse);

                FunctionRepository functionRepo = new FunctionRepository(conn);
                manual.dto.FunctionResponse firstFuncResponse = functionRepo.findById(createRequest.getFirstFunctionId());
                manual.dto.FunctionResponse secondFuncResponse = functionRepo.findById(createRequest.getSecondFunctionId());
                
                if (firstFuncResponse == null || secondFuncResponse == null) {
                    sendError(request, response, HttpServletResponse.SC_NOT_FOUND, "One or both functions not found");
                    return;
                }

                FunctionEntity firstFunction = mapFunctionToEntity(firstFuncResponse);
                FunctionEntity secondFunction = mapFunctionToEntity(secondFuncResponse);

                CompositeFunctionRepository repository = new CompositeFunctionRepository(conn);
                CompositeFunctionResponse composite = repository.create(createRequest, user, firstFunction, secondFunction);
                response.setStatus(HttpServletResponse.SC_CREATED);
                sendSuccess(request, response, composite);
            } catch (SQLException e) {
                logger.severe("Database error creating composite function: " + e.getMessage());
                sendError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to create composite function");
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
        logger.info("PUT request to CompositeFunctionServlet: " + request.getPathInfo());

        try {
            Long compositeId = parseIdFromPath(request.getPathInfo());
            if (compositeId == null) {
                sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid composite function ID");
                return;
            }

            CreateCompositeFunctionRequest updateRequest = parseJsonRequest(request, CreateCompositeFunctionRequest.class);
            
            try (Connection conn = DatabaseConnection.getConnection()) {
                CompositeFunctionRepository repository = new CompositeFunctionRepository(conn);
                CompositeFunctionResponse composite = repository.update(compositeId, updateRequest);
                if (composite == null) {
                    sendError(request, response, HttpServletResponse.SC_NOT_FOUND, "Composite function not found");
                } else {
                    sendSuccess(request, response, composite);
                }
            } catch (SQLException e) {
                logger.severe("Database error updating composite function: " + e.getMessage());
                sendError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to update composite function");
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
        logger.info("DELETE request to CompositeFunctionServlet: " + request.getPathInfo());

        Long compositeId = parseIdFromPath(request.getPathInfo());
        if (compositeId == null) {
            sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid composite function ID");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            CompositeFunctionRepository repository = new CompositeFunctionRepository(conn);
            boolean deleted = repository.delete(compositeId);
            if (deleted) {
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } else {
                sendError(request, response, HttpServletResponse.SC_NOT_FOUND, "Composite function not found");
            }
        } catch (SQLException e) {
            logger.severe("Database error deleting composite function: " + e.getMessage());
            sendError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to delete composite function");
        }
    }

    private UserEntity mapUserToEntity(manual.dto.UserResponse response) {
        UserEntity entity = new UserEntity();
        entity.setUserId(response.getUserId());
        entity.setUsername(response.getUsername());
        entity.setEmail(response.getEmail());
        return entity;
    }

    private FunctionEntity mapFunctionToEntity(manual.dto.FunctionResponse response) {
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
}

