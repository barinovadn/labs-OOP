package manual.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import manual.dto.ApiResponse;
import manual.dto.ErrorResponse;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

public abstract class BaseServlet extends HttpServlet {
    protected static final Logger logger = Logger.getLogger(BaseServlet.class.getName());
    protected final ObjectMapper objectMapper = new ObjectMapper();

    protected void sendJsonResponse(HttpServletResponse response, int statusCode, Object data) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(statusCode);
        
        PrintWriter out = response.getWriter();
        objectMapper.writeValue(out, data);
        out.flush();
    }

    protected void sendSuccess(HttpServletRequest request, HttpServletResponse response, Object data) throws IOException {
        ApiResponse<Object> apiResponse = ApiResponse.success(data);
        apiResponse.setPath(getRequestPath(request));
        sendJsonResponse(response, HttpServletResponse.SC_OK, apiResponse);
    }

    protected void sendError(HttpServletRequest request, HttpServletResponse response, int statusCode, String message) throws IOException {
        ErrorResponse errorResponse = new ErrorResponse(statusCode, 
            String.valueOf(statusCode), message, getRequestPath(request));
        sendJsonResponse(response, statusCode, errorResponse);
    }

    protected <T> T parseJsonRequest(HttpServletRequest request, Class<T> clazz) throws IOException {
        return objectMapper.readValue(request.getReader(), clazz);
    }

    protected Long parseIdFromPath(String pathInfo) {
        if (pathInfo == null || pathInfo.isEmpty()) {
            return null;
        }
        String[] parts = pathInfo.split("/");
        if (parts.length > 0 && !parts[parts.length - 1].isEmpty()) {
            try {
                return Long.parseLong(parts[parts.length - 1]);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    private String getRequestPath(HttpServletRequest request) {
        String path = request.getRequestURI();
        if (request.getQueryString() != null) {
            path += "?" + request.getQueryString();
        }
        return path;
    }

    protected void setRequestPath(HttpServletRequest request, HttpServletResponse response) {
        String path = getRequestPath(request);
        response.setHeader("Request-Path", path);
    }
}

