package manual.servlet;

import manual.DatabaseConnection;
import manual.dto.CompositeFunctionResponse;
import manual.dto.CreateCompositeFunctionRequest;
import manual.dto.FunctionResponse;
import manual.dto.UserResponse;
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
            if (req.getFirstFunctionId() == null || req.getSecondFunctionId() == null) {
                sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "firstFunctionId and secondFunctionId required");
                return;
            }
            
            try (Connection conn = DatabaseConnection.getConnection()) {
                // Check if user exists
                UserRepository userRepo = new UserRepository(conn);
                UserResponse userResp = userRepo.findById(req.getUserId());
                if (userResp == null) {
                    sendError(request, response, HttpServletResponse.SC_NOT_FOUND, "User not found");
                    return;
                }
                
                // Check if functions exist
                FunctionRepository funcRepo = new FunctionRepository(conn);
                FunctionResponse func1 = funcRepo.findById(req.getFirstFunctionId());
                FunctionResponse func2 = funcRepo.findById(req.getSecondFunctionId());
                
                if (func1 == null || func2 == null) {
                    sendError(request, response, HttpServletResponse.SC_NOT_FOUND, "Function not found");
                    return;
                }
                
                UserEntity user = new UserEntity();
                user.setUserId(userResp.getUserId());
                
                FunctionEntity firstFunc = new FunctionEntity();
                firstFunc.setFunctionId(req.getFirstFunctionId());
                
                FunctionEntity secondFunc = new FunctionEntity();
                secondFunc.setFunctionId(req.getSecondFunctionId());
                
                CompositeFunctionRepository repo = new CompositeFunctionRepository(conn);
                CompositeFunctionResponse composite = repo.create(req, user, firstFunc, secondFunc);
                response.setStatus(HttpServletResponse.SC_CREATED);
                sendSuccess(request, response, composite);
            }
        } catch (SQLException e) {
            sendError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error: " + e.getMessage());
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
