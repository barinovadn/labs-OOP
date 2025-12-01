package manual.security;

import manual.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityFullTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain chain;
    @Mock
    private FilterConfig filterConfig;

    @Test
    void testRoleEnumAllCases() {
        assertEquals(Role.ADMIN, Role.fromString("ADMIN"));
        assertEquals(Role.ADMIN, Role.fromString("admin"));
        assertEquals(Role.ADMIN, Role.fromString("Admin"));
        assertEquals(Role.USER, Role.fromString("USER"));
        assertEquals(Role.USER, Role.fromString("user"));
        assertEquals(Role.OPERATOR, Role.fromString("OPERATOR"));
        assertEquals(Role.OPERATOR, Role.fromString("operator"));
        assertNull(Role.fromString(null));
        assertNull(Role.fromString("invalid"));
        assertNull(Role.fromString(""));

        assertEquals("ADMIN", Role.ADMIN.getRoleName());
        assertEquals("USER", Role.USER.getRoleName());
        assertEquals("OPERATOR", Role.OPERATOR.getRoleName());
    }

    @Test
    void testSecurityContextAllCases() {
        UserEntity user = new UserEntity("test", "pass", "test@test.com");
        user.setUserId(1L);
        List<Role> roles = Arrays.asList(Role.USER, Role.OPERATOR);
        SecurityContext context = new SecurityContext(user, roles);

        assertEquals(user, context.getUser());
        assertEquals(roles, context.getRoles());
        assertTrue(context.isAuthenticated());
        assertEquals(1L, context.getUserId());
        assertTrue(context.hasRole(Role.USER));
        assertTrue(context.hasRole(Role.OPERATOR));
        assertFalse(context.hasRole(Role.ADMIN));

        SecurityContext nullContext = new SecurityContext(null, Collections.emptyList());
        assertNull(nullContext.getUser());
        assertFalse(nullContext.isAuthenticated());
        assertNull(nullContext.getUserId());

        SecurityContext nullRolesContext = new SecurityContext(user, null);
        assertFalse(nullRolesContext.hasRole(Role.USER));
    }

    @Test
    void testRoleCheckerAllScenarios() {
        assertFalse(RoleChecker.hasAnyRole(null, Role.USER));

        SecurityContext unauthContext = new SecurityContext(null, null);
        assertFalse(RoleChecker.hasAnyRole(unauthContext, Role.USER));

        UserEntity user = new UserEntity("test", "pass", "test@test.com");
        user.setUserId(1L);
        SecurityContext nullRolesContext = new SecurityContext(user, null);
        assertFalse(RoleChecker.hasAnyRole(nullRolesContext, Role.USER));

        SecurityContext emptyRolesContext = new SecurityContext(user, Collections.emptyList());
        assertFalse(RoleChecker.hasAnyRole(emptyRolesContext, Role.USER));

        SecurityContext userContext = new SecurityContext(user, Arrays.asList(Role.USER, Role.OPERATOR));
        assertTrue(RoleChecker.hasAnyRole(userContext, Role.USER));
        assertTrue(RoleChecker.hasAnyRole(userContext, Role.OPERATOR));
        assertTrue(RoleChecker.hasAnyRole(userContext, Role.ADMIN, Role.USER));
        assertFalse(RoleChecker.hasAnyRole(userContext, Role.ADMIN));
        assertTrue(RoleChecker.hasRole(userContext, Role.USER));
        assertFalse(RoleChecker.hasRole(userContext, Role.ADMIN));

        assertFalse(RoleChecker.isOwnerOrAdmin(null, 1L));
        assertFalse(RoleChecker.isOwnerOrAdmin(unauthContext, 1L));

        UserEntity admin = new UserEntity("admin", "pass", "admin@test.com");
        admin.setUserId(1L);
        SecurityContext adminContext = new SecurityContext(admin, Arrays.asList(Role.ADMIN));
        assertTrue(RoleChecker.isOwnerOrAdmin(adminContext, 999L));

        UserEntity owner = new UserEntity("owner", "pass", "owner@test.com");
        owner.setUserId(42L);
        SecurityContext ownerContext = new SecurityContext(owner, Arrays.asList(Role.USER));
        assertTrue(RoleChecker.isOwnerOrAdmin(ownerContext, 42L));
        assertFalse(RoleChecker.isOwnerOrAdmin(ownerContext, 999L));

        assertTrue(RoleChecker.canReadAll(adminContext));
        UserEntity operator = new UserEntity("operator", "pass", "operator@test.com");
        operator.setUserId(2L);
        SecurityContext operatorContext = new SecurityContext(operator, Arrays.asList(Role.OPERATOR));
        assertTrue(RoleChecker.canReadAll(operatorContext));
        assertFalse(RoleChecker.canReadAll(ownerContext));

        assertTrue(RoleChecker.canWriteAll(adminContext));
        assertFalse(RoleChecker.canWriteAll(operatorContext));
        assertFalse(RoleChecker.canWriteAll(ownerContext));
    }

    @Test
    void testSecurityFilterLifecycle() throws Exception {
        SecurityFilter filter = new SecurityFilter();
        assertDoesNotThrow(() -> filter.init(filterConfig));
        assertDoesNotThrow(() -> filter.destroy());
    }

    @Test
    void testSecurityFilterPublicEndpoints() throws Exception {
        SecurityFilter filter = new SecurityFilter();

        when(request.getRequestURI()).thenReturn("/api/auth/register");
        when(request.getMethod()).thenReturn("POST");
        filter.doFilter(request, response, chain);
        verify(chain).doFilter(request, response);

        reset(chain, request);
        when(request.getRequestURI()).thenReturn("/api/users");
        when(request.getMethod()).thenReturn("POST");
        filter.doFilter(request, response, chain);
        verify(chain).doFilter(request, response);

        reset(chain, request);
        when(request.getRequestURI()).thenReturn("/api/functions");
        when(request.getMethod()).thenReturn("OPTIONS");
        filter.doFilter(request, response, chain);
        verify(chain).doFilter(request, response);
    }

    @Test
    void testSecurityFilterAuthenticationFailures() throws Exception {
        SecurityFilter filter = new SecurityFilter();
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        when(request.getRequestURI()).thenReturn("/api/functions");
        when(request.getMethod()).thenReturn("GET");
        when(request.getHeader("Authorization")).thenReturn(null);
        when(response.getWriter()).thenReturn(pw);
        filter.doFilter(request, response, chain);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        reset(response, request);
        sw = new StringWriter();
        pw = new PrintWriter(sw);
        when(request.getRequestURI()).thenReturn("/api/functions");
        when(request.getMethod()).thenReturn("GET");
        when(request.getHeader("Authorization")).thenReturn("Bearer token");
        when(response.getWriter()).thenReturn(pw);
        filter.doFilter(request, response, chain);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        reset(response, request);
        sw = new StringWriter();
        pw = new PrintWriter(sw);
        when(request.getRequestURI()).thenReturn("/api/functions");
        when(request.getMethod()).thenReturn("GET");
        String invalidBase64 = java.util.Base64.getEncoder().encodeToString("nocolon".getBytes());
        when(request.getHeader("Authorization")).thenReturn("Basic " + invalidBase64);
        when(response.getWriter()).thenReturn(pw);
        filter.doFilter(request, response, chain);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Test
    void testSecurityFilterWithValidCredentials() throws Exception {
        SecurityFilter filter = new SecurityFilter();
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        when(request.getRequestURI()).thenReturn("/api/functions");
        when(request.getMethod()).thenReturn("GET");
        String validCreds = java.util.Base64.getEncoder().encodeToString("testuser:testpass".getBytes());
        when(request.getHeader("Authorization")).thenReturn("Basic " + validCreds);
        lenient().when(response.getWriter()).thenReturn(pw);
        filter.doFilter(request, response, chain);

        reset(response, request, chain);
        sw = new StringWriter();
        pw = new PrintWriter(sw);
        when(request.getRequestURI()).thenReturn("/api/functions");
        when(request.getMethod()).thenReturn("GET");
        String wrongPass = java.util.Base64.getEncoder().encodeToString("testuser:wrongpass".getBytes());
        when(request.getHeader("Authorization")).thenReturn("Basic " + wrongPass);
        when(response.getWriter()).thenReturn(pw);
        filter.doFilter(request, response, chain);

        reset(response, request, chain);
        sw = new StringWriter();
        pw = new PrintWriter(sw);
        when(request.getRequestURI()).thenReturn("/api/functions");
        when(request.getMethod()).thenReturn("GET");
        String noUser = java.util.Base64.getEncoder().encodeToString("nonexistent:pass".getBytes());
        when(request.getHeader("Authorization")).thenReturn("Basic " + noUser);
        when(response.getWriter()).thenReturn(pw);
        filter.doFilter(request, response, chain);
    }

    @Test
    void testSecurityFilterWithRealUser() throws Exception {
        long ts = System.currentTimeMillis();
        String username = "sectest" + ts;
        String password = "pass" + ts;
        Long userId;
        
        try (java.sql.Connection conn = manual.DatabaseConnection.getConnection()) {
            manual.repository.UserRepository userRepo = new manual.repository.UserRepository(conn);
            manual.dto.CreateUserRequest req = new manual.dto.CreateUserRequest();
            req.setUsername(username);
            req.setPassword(password);
            req.setEmail("sec" + ts + "@test.com");
            manual.dto.UserResponse resp = userRepo.create(req);
            userId = resp.getUserId();
            
            manual.repository.RoleRepository roleRepo = new manual.repository.RoleRepository(conn);
            roleRepo.assignRoleToUser(userId, "USER");
        }

        SecurityFilter filter = new SecurityFilter();
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        when(request.getRequestURI()).thenReturn("/api/functions");
        when(request.getMethod()).thenReturn("GET");
        String creds = java.util.Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
        when(request.getHeader("Authorization")).thenReturn("Basic " + creds);
        lenient().when(response.getWriter()).thenReturn(pw);
        filter.doFilter(request, response, chain);
        verify(chain).doFilter(request, response);
    }

    @Test
    void testSecurityFilterUserWithNoRoles() throws Exception {
        long ts = System.currentTimeMillis();
        String username = "noroles" + ts;
        String password = "pass" + ts;
        
        try (java.sql.Connection conn = manual.DatabaseConnection.getConnection()) {
            manual.repository.UserRepository userRepo = new manual.repository.UserRepository(conn);
            manual.dto.CreateUserRequest req = new manual.dto.CreateUserRequest();
            req.setUsername(username);
            req.setPassword(password);
            req.setEmail("noroles" + ts + "@test.com");
            userRepo.create(req);
        }

        SecurityFilter filter = new SecurityFilter();
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        when(request.getRequestURI()).thenReturn("/api/functions");
        when(request.getMethod()).thenReturn("GET");
        String creds = java.util.Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
        when(request.getHeader("Authorization")).thenReturn("Basic " + creds);
        when(response.getWriter()).thenReturn(pw);
        filter.doFilter(request, response, chain);
        verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
    }

    @Test
    void testCustomUserDetailsAllMethods() {
        UserEntity user = new UserEntity("test", "pass", "test@test.com");
        user.setUserId(1L);
        List<Role> roles = Arrays.asList(Role.USER, Role.OPERATOR);
        CustomUserDetails details = new CustomUserDetails(user, roles);

        assertEquals(user, details.getUserEntity());
        assertEquals(1L, details.getUserId());
        assertEquals("test", details.getUsername());
        assertEquals("pass", details.getPassword());
        assertEquals("test@test.com", details.getEmail());
        assertEquals(roles, details.getRoles());
        assertTrue(details.hasRole(Role.USER));
        assertFalse(details.hasRole(Role.ADMIN));
        assertTrue(details.hasAnyRole(Role.ADMIN, Role.USER));
        assertFalse(details.hasAnyRole(Role.ADMIN));
        assertTrue(details.isAccountNonExpired());
        assertTrue(details.isAccountNonLocked());
        assertTrue(details.isCredentialsNonExpired());
        assertTrue(details.isEnabled());
        assertNotNull(details.getAuthorities());
        assertNotNull(details.toString());

        CustomUserDetails same = new CustomUserDetails(user, roles);
        assertEquals(details, same);
        assertEquals(details.hashCode(), same.hashCode());

        UserEntity user2 = new UserEntity("other", "pass", "other@test.com");
        user2.setUserId(2L);
        CustomUserDetails different = new CustomUserDetails(user2, roles);
        assertNotEquals(details, different);

        assertNotEquals(details, null);
        assertNotEquals(details, "string");
        assertEquals(details, details);

        CustomUserDetails nullUser = new CustomUserDetails(null, roles);
        assertNull(nullUser.getUserId());
        assertNull(nullUser.getUsername());
        assertNull(nullUser.getPassword());
        assertNull(nullUser.getEmail());
        assertNull(nullUser.getUserEntity());

        CustomUserDetails nullRoles = new CustomUserDetails(user, null);
        assertFalse(nullRoles.hasRole(Role.USER));
        assertFalse(nullRoles.hasAnyRole(Role.USER));
        assertFalse(nullRoles.hasAnyRole());
        assertNotNull(nullRoles.getRoles());

        CustomUserDetails nullBoth = new CustomUserDetails(null, null);
        assertNotEquals(nullBoth, details);
        assertNotEquals(details, nullBoth);
    }

    @Test
    void testSecurityContextGetUsername() {
        UserEntity user = new UserEntity("testuser", "pass", "test@test.com");
        user.setUserId(1L);
        SecurityContext ctx = new SecurityContext(user, Arrays.asList(Role.USER));
        assertEquals("testuser", ctx.getUsername());
        assertNotNull(ctx.getUserDetails());

        SecurityContext nullCtx = new SecurityContext(null, null);
        assertNull(nullCtx.getUsername());
    }

    @Test
    void testSecurityContextHasAnyRole() {
        UserEntity user = new UserEntity("test", "pass", "test@test.com");
        user.setUserId(1L);
        SecurityContext ctx = new SecurityContext(user, Arrays.asList(Role.USER, Role.OPERATOR));
        assertTrue(ctx.hasAnyRole(Role.USER));
        assertTrue(ctx.hasAnyRole(Role.ADMIN, Role.USER));
        assertFalse(ctx.hasAnyRole(Role.ADMIN));
        assertFalse(ctx.hasAnyRole());
    }
}

