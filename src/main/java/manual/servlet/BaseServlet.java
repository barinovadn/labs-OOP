package manual.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import manual.security.SecurityContext;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

public abstract class BaseServlet extends HttpServlet {
    protected static final Logger logger = Logger.getLogger(BaseServlet.class.getName());
    protected final ObjectMapper objectMapper;
    
    public BaseServlet() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    protected void sendJsonResponse(HttpServletResponse response, int statusCode, Object data) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(statusCode);
        String json = objectMapper.writeValueAsString(data);
        response.getWriter().write(json);
    }

    protected void sendSuccess(HttpServletRequest request, HttpServletResponse response, Object data) throws IOException {
        sendJsonResponse(response, HttpServletResponse.SC_OK, new ApiResponse(true, data, null));
    }

    protected void sendError(HttpServletRequest request, HttpServletResponse response, int statusCode, String message) throws IOException {
        sendJsonResponse(response, statusCode, new ApiResponse(false, null, message));
    }

    protected <T> T parseJsonRequest(HttpServletRequest request, Class<T> clazz) throws IOException {
        return objectMapper.readValue(request.getReader(), clazz);
    }

    protected Long parseIdFromPath(String pathInfo) {
        if (pathInfo == null || pathInfo.length() < 2) {
            return null;
        }
        String[] parts = pathInfo.substring(1).split("/");
        for (int i = parts.length - 1; i >= 0; i--) {
            try {
                return Long.parseLong(parts[i]);
            } catch (NumberFormatException e) {
                continue;
            }
        }
        return null;
    }

    protected SecurityContext getSecurityContext(HttpServletRequest request) {
        return (SecurityContext) request.getAttribute("securityContext");
    }

    protected void setRequestPath(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Request-Path", request.getRequestURI());
    }

    private static class ApiResponse {
        public boolean success;
        public Object data;
        public String error;

        public ApiResponse(boolean success, Object data, String error) {
            this.success = success;
            this.data = data;
            this.error = error;
        }
    }
}
