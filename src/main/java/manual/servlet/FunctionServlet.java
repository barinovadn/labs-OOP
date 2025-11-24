package manual.servlet;

import java.io.BufferedReader;
import java.io.IOException;

import manual.DatabaseConnection;
import manual.dto.CreateFunctionRequest;
import manual.dto.FunctionResponse;
import manual.entity.UserEntity;
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

@WebServlet(name = "FunctionServlet", urlPatterns = {"/api/functions/*"})
public class FunctionServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        setRequestPath(request, response);
        logger.info("GET request to FunctionServlet: " + request.getPathInfo());

        try (Connection conn = DatabaseConnection.getConnection()) {
            FunctionRepository repository = new FunctionRepository(conn);
            String pathInfo = request.getPathInfo();
            String sort = request.getParameter("sort");

            if (pathInfo == null || pathInfo.equals("/")) {
                List<FunctionResponse> functions;
                if (sort == null) {
                    functions = repository.findAll();
                } else {
                    switch (sort) {
                        case "name_asc":
                            functions = repository.findAllOrderByNameAsc();
                            break;
                        case "name_desc":
                            functions = repository.findAllOrderByNameDesc();
                            break;
                        case "x_from_asc":
                            functions = repository.findAllOrderByXFromAsc();
                            break;
                        case "type_name":
                            functions = repository.findAllOrderByTypeAndName();
                            break;
                        default:
                            functions = repository.findAll();
                    }
                }
                sendSuccess(request, response, functions);
            } else {
                Long functionId = parseIdFromPath(pathInfo);
                if (functionId == null) {
                    sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid function ID");
                    return;
                }

                FunctionResponse function = repository.findById(functionId);
                if (function == null) {
                    sendError(request, response, HttpServletResponse.SC_NOT_FOUND, "Function not found");
                } else {
                    sendSuccess(request, response, function);
                }
            }
        } catch (SQLException e) {
            logger.severe("Database error in FunctionServlet: " + e.getMessage());
            sendError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        setRequestPath(request, response);
        logger.info("POST request to FunctionServlet");

        try {
            StringBuilder sb = new StringBuilder();
            String line;
            try (BufferedReader reader = request.getReader()) {
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }
            
            String jsonBody = sb.toString();
            logger.info("Received JSON: " + jsonBody);
            
            CreateFunctionRequest createRequest = objectMapper.readValue(jsonBody, CreateFunctionRequest.class);
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
                
                FunctionRepository repository = new FunctionRepository(conn);
                FunctionResponse function = repository.create(createRequest, mapToEntity(userResponse));
                
                response.setStatus(HttpServletResponse.SC_CREATED);
                sendSuccess(request, response, function);
                
            } catch (SQLException e) {
                logger.severe("Database error creating function: " + e.getMessage());
                sendError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to create function");
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
        logger.info("PUT request to FunctionServlet: " + request.getPathInfo());

        try {
            Long functionId = parseIdFromPath(request.getPathInfo());
            if (functionId == null) {
                sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid function ID");
                return;
            }

            CreateFunctionRequest updateRequest = parseJsonRequest(request, CreateFunctionRequest.class);
            
            try (Connection conn = DatabaseConnection.getConnection()) {
                FunctionRepository repository = new FunctionRepository(conn);
                FunctionResponse function = repository.update(functionId, updateRequest);
                if (function == null) {
                    sendError(request, response, HttpServletResponse.SC_NOT_FOUND, "Function not found");
                } else {
                    sendSuccess(request, response, function);
                }
            } catch (SQLException e) {
                logger.severe("Database error updating function: " + e.getMessage());
                sendError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to update function");
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
        logger.info("DELETE request to FunctionServlet: " + request.getPathInfo());

        Long functionId = parseIdFromPath(request.getPathInfo());
        if (functionId == null) {
            sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid function ID");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            FunctionRepository repository = new FunctionRepository(conn);
            boolean deleted = repository.delete(functionId);
            if (deleted) {
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } else {
                sendError(request, response, HttpServletResponse.SC_NOT_FOUND, "Function not found");
            }
        } catch (SQLException e) {
            logger.severe("Database error deleting function: " + e.getMessage());
            sendError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to delete function");
        }
    }

    private UserEntity mapToEntity(manual.dto.UserResponse response) {
    UserEntity entity = new UserEntity();
    entity.setUserId(response.getUserId());
    entity.setUsername(response.getUsername());
    entity.setEmail(response.getEmail());
    entity.setCreatedAt(response.getCreatedAt());
    return entity;
}
}

