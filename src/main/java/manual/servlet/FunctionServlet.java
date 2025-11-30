package manual.servlet;

import manual.DatabaseConnection;
import manual.dto.CreateFunctionRequest;
import manual.dto.FunctionResponse;
import manual.dto.UserResponse;
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
        String pathInfo = request.getPathInfo();
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
        try {
            CreateFunctionRequest req = parseJsonRequest(request, CreateFunctionRequest.class);
            Long userId = req.getUserId();
            if (userId == null) {
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
            sendError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error");
        } catch (IOException e) {
            sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid");
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
}
