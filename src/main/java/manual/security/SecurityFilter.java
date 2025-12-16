package manual.security;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import manual.DatabaseConnection;
import manual.entity.UserEntity;
import manual.repository.RoleRepository;
import manual.repository.UserRepository;

@WebFilter(filterName = "SecurityFilter", urlPatterns = "/api/*")
public class SecurityFilter implements Filter {
    private static final Logger logger = Logger.getLogger(SecurityFilter.class.getName());
    private static final String AUTH_HEADER = "Authorization";
    private static final String BASIC_PREFIX = "Basic ";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("SecurityFilter initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        String origin = httpRequest.getHeader("Origin");
        if (origin != null) {
            httpResponse.setHeader("Access-Control-Allow-Origin", origin);
            httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
        } else {
            httpResponse.setHeader("Access-Control-Allow-Origin", "*");
        }
        httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        httpResponse.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        httpResponse.setHeader("Access-Control-Expose-Headers", "Request-Path");
        
        String contextPath = httpRequest.getContextPath();
        String requestURI = httpRequest.getRequestURI();
        String requestPath = requestURI;
        
        if (contextPath != null && !contextPath.isEmpty() && requestURI.startsWith(contextPath)) {
            requestPath = requestURI.substring(contextPath.length());
        }
        
        String method = httpRequest.getMethod();
        
        logger.info("Security filter: " + method + " " + requestPath + " (full URI: " + requestURI + ", context: " + contextPath + ")");
        
        if ("OPTIONS".equals(method)) {
            logger.fine("CORS preflight request, allowing");
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            chain.doFilter(request, response);
            return;
        }
        
        if (isPublicEndpoint(requestPath, method)) {
            logger.fine("Public endpoint, skipping authentication");
            chain.doFilter(request, response);
            return;
        }

        String authHeader = httpRequest.getHeader(AUTH_HEADER);
        if (authHeader == null) {
            authHeader = httpRequest.getHeader("authorization");
        }
        if (authHeader == null) {
            authHeader = httpRequest.getHeader("AUTHORIZATION");
        }
        
        if (authHeader == null || authHeader.trim().isEmpty() || !authHeader.startsWith(BASIC_PREFIX)) {
            logger.warning("Missing or invalid Authorization header for " + requestPath);
            sendUnauthorized(httpResponse, "Authorization required");
            return;
        }

        try {
            String credentials = authHeader.substring(BASIC_PREFIX.length());
            if (credentials == null || credentials.trim().isEmpty()) {
                logger.warning("Empty credentials in Authorization header");
                sendUnauthorized(httpResponse, "Invalid credentials format");
                return;
            }
            
            String decoded;
            try {
                decoded = new String(Base64.getDecoder().decode(credentials));
            } catch (IllegalArgumentException e) {
                logger.warning("Invalid Base64 encoding in credentials: " + e.getMessage());
                sendUnauthorized(httpResponse, "Invalid credentials format");
                return;
            }
            
            if (decoded == null || decoded.isEmpty()) {
                logger.warning("Empty decoded credentials");
                sendUnauthorized(httpResponse, "Invalid credentials format");
                return;
            }
            
            String[] parts = decoded.split(":", 2);
            
            if (parts.length != 2 || parts[0] == null || parts[0].isEmpty() || parts[1] == null) {
                logger.warning("Invalid credentials format");
                sendUnauthorized(httpResponse, "Invalid credentials format");
                return;
            }

            String username = parts[0];
            String password = parts[1];

            logger.fine("Authenticating user: " + username);

            try (Connection conn = DatabaseConnection.getConnection()) {
                UserRepository userRepo = new UserRepository(conn);
                UserEntity user = userRepo.findEntityByUsername(username);

                if (user == null) {
                    logger.warning("Authentication failed: user not found: " + username);
                    sendUnauthorized(httpResponse, "Invalid credentials");
                    return;
                }
                
                String storedPassword = user.getPassword();
                if (storedPassword == null || !storedPassword.equals(password)) {
                    logger.warning("Authentication failed for user: " + username + " (password mismatch)");
                    sendUnauthorized(httpResponse, "Invalid credentials");
                    return;
                }

                logger.info("User authenticated: " + username + " (ID: " + user.getUserId() + ")");

                RoleRepository roleRepo = new RoleRepository(conn);
                List<Role> roles = roleRepo.findRolesByUserId(user.getUserId());
                
                if (roles.isEmpty()) {
                    logger.warning("User " + username + " has no roles assigned");
                    sendForbidden(httpResponse, "User has no roles assigned");
                    return;
                }

                logger.fine("User " + username + " has roles: " + roles);

                SecurityContext securityContext = new SecurityContext(user, roles);
                httpRequest.setAttribute("securityContext", securityContext);

                chain.doFilter(request, response);
            } catch (SQLException e) {
                logger.severe("Database error during authentication: " + e.getMessage());
                sendError(httpResponse, "Database error");
            }
        } catch (Exception e) {
            logger.severe("Error processing authentication: " + (e.getMessage() != null ? e.getMessage() : e.getClass().getName()));
            if (e.getCause() != null) {
                logger.severe("Caused by: " + e.getCause().getMessage());
            }
            sendUnauthorized(httpResponse, "Authentication failed");
        }
    }

    private boolean isPublicEndpoint(String path, String method) {
        if (method.equals("POST") && path.endsWith("/api/auth/register")) {
            return true;
        }
        if (method.equals("POST") && path.endsWith("/api/users")) {
            return true;
        }
        if (method.equals("OPTIONS")) {
            return true;
        }
        return false;
    }

    private void sendUnauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"success\":false,\"error\":\"Unauthorized\",\"message\":\"" + 
            message.replace("\"", "\\\"") + "\"}");
    }

    private void sendForbidden(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"success\":false,\"error\":\"Forbidden\",\"message\":\"" + 
            message.replace("\"", "\\\"") + "\"}");
    }

    private void sendError(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"success\":false,\"error\":\"Internal Server Error\",\"message\":\"" + 
            message.replace("\"", "\\\"") + "\"}");
    }

    @Override
    public void destroy() {
        logger.info("SecurityFilter destroyed");
    }
}


