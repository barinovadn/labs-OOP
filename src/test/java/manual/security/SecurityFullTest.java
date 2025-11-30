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
}

