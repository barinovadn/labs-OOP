package manual.servlet;

import manual.entity.UserEntity;
import manual.security.Role;
import manual.security.SecurityContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServletFullTest {

    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    private PrintWriter printWriter;

    @BeforeEach
    void setUp() throws Exception {
        printWriter = new PrintWriter(new StringWriter());
        lenient().when(response.getWriter()).thenReturn(printWriter);
    }

    @Test
    void testBaseServlet() throws Exception {
        class TestServlet extends BaseServlet {
            public Long testParse(String p) { return parseIdFromPath(p); }
            public SecurityContext testCtx(HttpServletRequest r) { return getSecurityContext(r); }
        }
        TestServlet s = new TestServlet();
        assertEquals(123L, s.testParse("/123"));
        assertEquals(456L, s.testParse("/users/456"));
        assertNull(s.testParse(null));
        assertNull(s.testParse(""));
        assertNull(s.testParse("/"));
        assertNull(s.testParse("/abc"));
        when(request.getAttribute("securityContext")).thenReturn(null);
        assertNull(s.testCtx(request));
        UserEntity u = new UserEntity("x", "x", "x@x.com");
        u.setUserId(1L);
        when(request.getAttribute("securityContext")).thenReturn(new SecurityContext(u, Arrays.asList(Role.USER)));
        assertNotNull(s.testCtx(request));
    }

    @Test
    void testAuthServlet() throws Exception {
        AuthServlet s = new AuthServlet();
        when(request.getRequestURI()).thenReturn("/api/auth");
        when(request.getPathInfo()).thenReturn(null);
        s.doPost(request, response);
        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getPathInfo()).thenReturn("/unknown");
        s.doPost(request, response);
        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getPathInfo()).thenReturn("/register");
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("{invalid}")));
        s.doPost(request, response);
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        String json = "{\"username\":\"u" + System.currentTimeMillis() + "\",\"password\":\"p\",\"email\":\"e" + System.currentTimeMillis() + "@t.com\"}";
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(json)));
        s.doPost(request, response);
    }

    @Test
    void testUserServletGet() throws Exception {
        UserServlet s = new UserServlet();
        when(request.getRequestURI()).thenReturn("/api/users");

        when(request.getPathInfo()).thenReturn(null);
        s.doGet(request, response);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getPathInfo()).thenReturn("/");
        s.doGet(request, response);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getPathInfo()).thenReturn("/abc");
        s.doGet(request, response);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getPathInfo()).thenReturn("/999");
        s.doGet(request, response);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getPathInfo()).thenReturn("/1");
        s.doGet(request, response);
    }

    @Test
    void testUserServletPost() throws Exception {
        UserServlet s = new UserServlet();
        when(request.getRequestURI()).thenReturn("/api/users");

        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("{invalid}")));
        s.doPost(request, response);
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        String json = "{\"username\":\"utest" + System.currentTimeMillis() + "\",\"password\":\"p\",\"email\":\"e" + System.currentTimeMillis() + "@t.com\"}";
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(json)));
        s.doPost(request, response);
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    void testUserServletPut() throws Exception {
        UserServlet s = new UserServlet();
        when(request.getRequestURI()).thenReturn("/api/users/abc");
        when(request.getPathInfo()).thenReturn("/abc");
        s.doPut(request, response);
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getPathInfo()).thenReturn("/999");
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("{\"username\":\"x\",\"password\":\"x\",\"email\":\"x@x.com\"}")));
        s.doPut(request, response);
        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getPathInfo()).thenReturn("/1");
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("{\"username\":\"x\",\"password\":\"x\",\"email\":\"x@x.com\"}")));
        s.doPut(request, response);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getPathInfo()).thenReturn("/1");
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("{invalid}")));
        s.doPut(request, response);
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void testUserServletDelete() throws Exception {
        UserServlet s = new UserServlet();
        when(request.getRequestURI()).thenReturn("/api/users/abc");
        when(request.getPathInfo()).thenReturn("/abc");
        s.doDelete(request, response);

        reset(response); lenient().when(response.getWriter()).thenReturn(printWriter);
        when(request.getPathInfo()).thenReturn("/999");
        s.doDelete(request, response);

        reset(response); lenient().when(response.getWriter()).thenReturn(printWriter);
        when(request.getPathInfo()).thenReturn("/1");
        s.doDelete(request, response);
    }

    @Test
    void testFunctionServletGet() throws Exception {
        FunctionServlet s = new FunctionServlet();
        when(request.getRequestURI()).thenReturn("/api/functions");

        when(request.getPathInfo()).thenReturn(null);
        when(request.getParameter("sort")).thenReturn(null);
        s.doGet(request, response);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getPathInfo()).thenReturn("/");
        when(request.getParameter("sort")).thenReturn("name_asc");
        s.doGet(request, response);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getPathInfo()).thenReturn("/");
        when(request.getParameter("sort")).thenReturn("name_desc");
        s.doGet(request, response);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getPathInfo()).thenReturn("/abc");
        s.doGet(request, response);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getPathInfo()).thenReturn("/999");
        s.doGet(request, response);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getPathInfo()).thenReturn("/1");
        s.doGet(request, response);
    }

    @Test
    void testFunctionServletPost() throws Exception {
        FunctionServlet s = new FunctionServlet();
        when(request.getRequestURI()).thenReturn("/api/functions");

        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("{\"userId\":null}")));
        s.doPost(request, response);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("{\"userId\":999}")));
        s.doPost(request, response);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        String json = "{\"userId\":1,\"functionName\":\"fn" + System.currentTimeMillis() + "\",\"functionType\":\"LINEAR\",\"functionExpression\":\"x\",\"xFrom\":0.0,\"xTo\":10.0}";
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(json)));
        s.doPost(request, response);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("{invalid}")));
        s.doPost(request, response);
    }

    @Test
    void testFunctionServletPut() throws Exception {
        FunctionServlet s = new FunctionServlet();
        when(request.getRequestURI()).thenReturn("/api/functions");

        when(request.getPathInfo()).thenReturn("/abc");
        s.doPut(request, response);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getPathInfo()).thenReturn("/999");
        String json = "{\"functionName\":\"f\",\"functionType\":\"LINEAR\",\"functionExpression\":\"x\",\"xFrom\":0.0,\"xTo\":10.0}";
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(json)));
        s.doPut(request, response);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getPathInfo()).thenReturn("/1");
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(json)));
        s.doPut(request, response);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getPathInfo()).thenReturn("/1");
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("{invalid}")));
        s.doPut(request, response);
    }

    @Test
    void testFunctionServletDelete() throws Exception {
        FunctionServlet s = new FunctionServlet();
        when(request.getRequestURI()).thenReturn("/api/functions");

        when(request.getPathInfo()).thenReturn("/abc");
        s.doDelete(request, response);

        reset(response); lenient().when(response.getWriter()).thenReturn(printWriter);
        when(request.getPathInfo()).thenReturn("/999");
        s.doDelete(request, response);

        reset(response); lenient().when(response.getWriter()).thenReturn(printWriter);
        when(request.getPathInfo()).thenReturn("/1");
        s.doDelete(request, response);
    }

    @Test
    void testCompositeFunctionServletGet() throws Exception {
        CompositeFunctionServlet s = new CompositeFunctionServlet();
        when(request.getRequestURI()).thenReturn("/api/composite-functions/abc");
        when(request.getPathInfo()).thenReturn("/abc");
        s.doGet(request, response);
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getRequestURI()).thenReturn("/api/composite-functions/999");
        when(request.getPathInfo()).thenReturn("/999");
        s.doGet(request, response);
    }

    @Test
    void testCompositeFunctionServletPost() throws Exception {
        CompositeFunctionServlet s = new CompositeFunctionServlet();
        when(request.getRequestURI()).thenReturn("/api/composite-functions");
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("{\"userId\":null}")));
        s.doPost(request, response);
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("{\"userId\":1}")));
        s.doPost(request, response);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("{invalid}")));
        s.doPost(request, response);
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void testCompositeFunctionServletPutDelete() throws Exception {
        CompositeFunctionServlet s = new CompositeFunctionServlet();
        when(request.getRequestURI()).thenReturn("/api/composite-functions/abc");
        when(request.getPathInfo()).thenReturn("/abc");
        s.doPut(request, response);
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getRequestURI()).thenReturn("/api/composite-functions/999");
        when(request.getPathInfo()).thenReturn("/999");
        s.doPut(request, response);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getPathInfo()).thenReturn("/abc");
        s.doDelete(request, response);
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getPathInfo()).thenReturn("/999");
        s.doDelete(request, response);
    }

    @Test
    void testFunctionDifferentiateServlet() throws Exception {
        FunctionDifferentiateServlet s = new FunctionDifferentiateServlet();

        when(request.getRequestURI()).thenReturn("/api/nofunctions/1/differentiate");
        s.doPost(request, response);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getRequestURI()).thenReturn("/api/functions/abc/differentiate");
        s.doPost(request, response);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getRequestURI()).thenReturn("/api/functions/999/differentiate");
        s.doPost(request, response);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getRequestURI()).thenReturn("/api/functions/1/differentiate");
        s.doPost(request, response);
    }

    @Test
    void testFunctionOperationServlet() throws Exception {
        FunctionOperationServlet s = new FunctionOperationServlet();
        when(request.getRequestURI()).thenReturn("/api/functions/operations");

        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("{\"operation\":\"add\"}")));
        s.doPost(request, response);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("{invalid}")));
        s.doPost(request, response);
    }

    @Test
    void testFunctionSearchServlet() throws Exception {
        FunctionSearchServlet s = new FunctionSearchServlet();
        when(request.getRequestURI()).thenReturn("/api/functions/search");

        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("{}")));
        s.doPost(request, response);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("{invalid}")));
        s.doPost(request, response);
    }

    @Test
    void testPointServletGet() throws Exception {
        PointServlet s = new PointServlet();
        when(request.getRequestURI()).thenReturn("/api/points/abc");
        when(request.getPathInfo()).thenReturn("/abc");
        s.doGet(request, response);
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getRequestURI()).thenReturn("/api/points/999");
        when(request.getPathInfo()).thenReturn("/999");
        s.doGet(request, response);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getRequestURI()).thenReturn("/api/functions/1/points");
        s.doGet(request, response);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getRequestURI()).thenReturn("/api/functions/abc/points");
        s.doGet(request, response);
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void testPointServletPostCalculate() throws Exception {
        PointServlet s = new PointServlet();
        when(request.getRequestURI()).thenReturn("/api/functions/1/calculate");
        when(request.getParameter("x")).thenReturn(null);
        s.doPost(request, response);
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getParameter("x")).thenReturn("abc");
        s.doPost(request, response);
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getParameter("x")).thenReturn("5.0");
        s.doPost(request, response);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getRequestURI()).thenReturn("/api/functions/abc/calculate");
        s.doPost(request, response);
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void testPointServletPostPoints() throws Exception {
        PointServlet s = new PointServlet();
        when(request.getRequestURI()).thenReturn("/api/functions/1/points");
        s.doPost(request, response);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getRequestURI()).thenReturn("/api/functions/abc/points");
        s.doPost(request, response);
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getRequestURI()).thenReturn("/api/other");
        s.doPost(request, response);
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void testPointServletPutDelete() throws Exception {
        PointServlet s = new PointServlet();
        when(request.getRequestURI()).thenReturn("/api/points");
        when(request.getPathInfo()).thenReturn("/abc");
        s.doPut(request, response);
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getPathInfo()).thenReturn("/999");
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("{\"xvalue\":1,\"yvalue\":2}")));
        s.doPut(request, response);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getPathInfo()).thenReturn("/abc");
        s.doDelete(request, response);
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getPathInfo()).thenReturn("/999");
        s.doDelete(request, response);
    }

    @Test
    void testUserRoleServlet() throws Exception {
        UserRoleServlet s = new UserRoleServlet();
        when(request.getRequestURI()).thenReturn("/api/users/1/roles");

        when(request.getPathInfo()).thenReturn("/abc");
        s.doGet(request, response);
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getPathInfo()).thenReturn("/1");
        s.doGet(request, response);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getPathInfo()).thenReturn("/abc");
        s.doPost(request, response);
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getPathInfo()).thenReturn("/1");
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("{\"roleName\":null}")));
        s.doPost(request, response);
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("{\"roleName\":\"\"}")));
        s.doPost(request, response);
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("{\"roleName\":\"ADMIN\"}")));
        s.doPost(request, response);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("{invalid}")));
        s.doPost(request, response);
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getPathInfo()).thenReturn("/abc");
        s.doDelete(request, response);
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getPathInfo()).thenReturn("/1");
        when(request.getParameter("roleName")).thenReturn(null);
        s.doDelete(request, response);
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getParameter("roleName")).thenReturn("");
        s.doDelete(request, response);
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getParameter("roleName")).thenReturn("USER");
        s.doDelete(request, response);
    }

    @Test
    void testUserFunctionServlet() throws Exception {
        UserFunctionServlet s = new UserFunctionServlet();
        when(request.getRequestURI()).thenReturn("/api/users/1/functions");

        when(request.getPathInfo()).thenReturn(null);
        s.doGet(request, response);
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getPathInfo()).thenReturn("/abc");
        s.doGet(request, response);
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getPathInfo()).thenReturn("/1");
        s.doGet(request, response);
    }

    @Test
    void testUserCompositeFunctionServlet() throws Exception {
        UserCompositeFunctionServlet s = new UserCompositeFunctionServlet();
        when(request.getRequestURI()).thenReturn("/api/users/1/composite-functions");

        when(request.getPathInfo()).thenReturn(null);
        s.doGet(request, response);
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getPathInfo()).thenReturn("/abc");
        s.doGet(request, response);
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getPathInfo()).thenReturn("/1");
        s.doGet(request, response);
    }

    @Test
    void testFunctionServletGetPoints() throws Exception {
        FunctionServlet s = new FunctionServlet();
        when(request.getRequestURI()).thenReturn("/api/functions/1/points");
        when(request.getPathInfo()).thenReturn("/1/points");
        s.doGet(request, response);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getPathInfo()).thenReturn("/abc/points");
        s.doGet(request, response);
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getPathInfo()).thenReturn("/points");
        s.doGet(request, response);
    }

    @Test
    void testFunctionServletPostPoints() throws Exception {
        FunctionServlet s = new FunctionServlet();
        when(request.getRequestURI()).thenReturn("/api/functions/1/points");
        when(request.getPathInfo()).thenReturn("/abc/points");
        s.doPost(request, response);
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getPathInfo()).thenReturn("/999999/points");
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("{\"xValue\":1.0,\"yValue\":2.0}")));
        s.doPost(request, response);
        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getPathInfo()).thenReturn("/1/points");
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("{\"xValue\":" + System.currentTimeMillis() + ".5,\"yValue\":2.5}")));
        s.doPost(request, response);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getPathInfo()).thenReturn("/1/points");
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("{invalid}")));
        s.doPost(request, response);
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void testFunctionServletDifferentiate() throws Exception {
        FunctionServlet s = new FunctionServlet();
        when(request.getRequestURI()).thenReturn("/api/functions/1/differentiate");
        when(request.getPathInfo()).thenReturn("/abc/differentiate");
        s.doPost(request, response);
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getPathInfo()).thenReturn("/999999/differentiate");
        s.doPost(request, response);
        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getPathInfo()).thenReturn("/1/differentiate");
        lenient().when(request.getParameter("type")).thenReturn(null);
        s.doPost(request, response);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getPathInfo()).thenReturn("/1/differentiate");
        lenient().when(request.getParameter("type")).thenReturn("MIDDLE");
        s.doPost(request, response);
    }

    @Test
    void testFunctionServletCalculate() throws Exception {
        FunctionServlet s = new FunctionServlet();
        when(request.getRequestURI()).thenReturn("/api/functions/1/calculate");
        when(request.getPathInfo()).thenReturn("/abc/calculate");
        s.doPost(request, response);
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getPathInfo()).thenReturn("/1/calculate");
        when(request.getParameter("x")).thenReturn(null);
        s.doPost(request, response);
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getPathInfo()).thenReturn("/1/calculate");
        when(request.getParameter("x")).thenReturn("not_a_number");
        s.doPost(request, response);
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getPathInfo()).thenReturn("/999999/calculate");
        when(request.getParameter("x")).thenReturn("5.0");
        s.doPost(request, response);
        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getPathInfo()).thenReturn("/1/calculate");
        when(request.getParameter("x")).thenReturn("5.0");
        s.doPost(request, response);
    }

    @Test
    void testCompositeFunctionServletPostFull() throws Exception {
        CompositeFunctionServlet s = new CompositeFunctionServlet();
        when(request.getRequestURI()).thenReturn("/api/composite-functions");
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("{\"userId\":1,\"firstFunctionId\":null}")));
        s.doPost(request, response);
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("{\"userId\":999999,\"firstFunctionId\":1,\"secondFunctionId\":1}")));
        s.doPost(request, response);
        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("{\"userId\":1,\"firstFunctionId\":999999,\"secondFunctionId\":1}")));
        s.doPost(request, response);
        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("{\"userId\":1,\"firstFunctionId\":1,\"secondFunctionId\":999999}")));
        s.doPost(request, response);
        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void testCompositeFunctionServletCreateSuccess() throws Exception {
        long ts = System.currentTimeMillis();
        UserServlet userServlet = new UserServlet();
        when(request.getRequestURI()).thenReturn("/api/users");
        String userJson = "{\"username\":\"comptest" + ts + "\",\"password\":\"p\",\"email\":\"comp" + ts + "@t.com\"}";
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(userJson)));
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        when(response.getWriter()).thenReturn(pw);
        userServlet.doPost(request, response);
        pw.flush();
        String userResult = sw.toString();
        Long userId = extractIdFromJson(userResult, "userId");
        assertNotNull(userId);

        FunctionServlet funcServlet = new FunctionServlet();
        reset(response); sw = new StringWriter(); pw = new PrintWriter(sw); when(response.getWriter()).thenReturn(pw);
        when(request.getRequestURI()).thenReturn("/api/functions");
        when(request.getPathInfo()).thenReturn(null);
        String func1Json = "{\"userId\":" + userId + ",\"functionName\":\"f1" + ts + "\",\"functionType\":\"LINEAR\",\"functionExpression\":\"x\",\"xFrom\":0.0,\"xTo\":10.0}";
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(func1Json)));
        funcServlet.doPost(request, response);
        pw.flush();
        Long funcId1 = extractIdFromJson(sw.toString(), "functionId");
        assertNotNull(funcId1);

        reset(response); sw = new StringWriter(); pw = new PrintWriter(sw); when(response.getWriter()).thenReturn(pw);
        String func2Json = "{\"userId\":" + userId + ",\"functionName\":\"f2" + ts + "\",\"functionType\":\"LINEAR\",\"functionExpression\":\"2x\",\"xFrom\":0.0,\"xTo\":10.0}";
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(func2Json)));
        funcServlet.doPost(request, response);
        pw.flush();
        Long funcId2 = extractIdFromJson(sw.toString(), "functionId");
        assertNotNull(funcId2);

        CompositeFunctionServlet s = new CompositeFunctionServlet();
        reset(response); sw = new StringWriter(); pw = new PrintWriter(sw); when(response.getWriter()).thenReturn(pw);
        when(request.getRequestURI()).thenReturn("/api/composite-functions");
        String compositeJson = "{\"userId\":" + userId + ",\"compositeName\":\"comp" + ts + "\",\"firstFunctionId\":" + funcId1 + ",\"secondFunctionId\":" + funcId2 + "}";
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(compositeJson)));
        s.doPost(request, response);
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

    private Long extractIdFromJson(String json, String field) {
        try {
            int idx = json.indexOf("\"" + field + "\":");
            if (idx < 0) return null;
            int start = idx + field.length() + 3;
            int end = start;
            while (end < json.length() && (Character.isDigit(json.charAt(end)) || json.charAt(end) == '-')) end++;
            return Long.parseLong(json.substring(start, end));
        } catch (Exception e) { return null; }
    }

    @Test
    void testCompositeFunctionServletGetPutFound() throws Exception {
        CompositeFunctionServlet s = new CompositeFunctionServlet();
        when(request.getRequestURI()).thenReturn("/api/composite-functions/1");
        when(request.getPathInfo()).thenReturn("/1");
        s.doGet(request, response);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getPathInfo()).thenReturn("/1");
        s.doPut(request, response);
    }

    @Test
    void testPointServletGetPutDeleteFound() throws Exception {
        PointServlet s = new PointServlet();
        when(request.getRequestURI()).thenReturn("/api/points/1");
        when(request.getPathInfo()).thenReturn("/1");
        s.doGet(request, response);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getPathInfo()).thenReturn("/1");
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("{\"xValue\":1.0,\"yValue\":2.0}")));
        s.doPut(request, response);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getPathInfo()).thenReturn("/1");
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("{invalid}")));
        s.doPut(request, response);
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);

        reset(response); lenient().when(response.getWriter()).thenReturn(printWriter);
        when(request.getPathInfo()).thenReturn("/1");
        s.doDelete(request, response);
    }

    @Test
    void testFunctionServletGetByIdFound() throws Exception {
        FunctionServlet s = new FunctionServlet();
        when(request.getRequestURI()).thenReturn("/api/functions/1");
        when(request.getPathInfo()).thenReturn("/1");
        lenient().when(request.getParameter("sort")).thenReturn(null);
        s.doGet(request, response);
    }

    @Test
    void testFunctionServletPutFound() throws Exception {
        FunctionServlet s = new FunctionServlet();
        when(request.getRequestURI()).thenReturn("/api/functions/1");
        when(request.getPathInfo()).thenReturn("/1");
        String json = "{\"functionName\":\"updated\",\"functionType\":\"LINEAR\",\"functionExpression\":\"x\",\"xFrom\":0.0,\"xTo\":10.0}";
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(json)));
        s.doPut(request, response);
    }

    @Test
    void testFunctionServletExtractFunctionIdEdgeCases() throws Exception {
        FunctionServlet s = new FunctionServlet();
        when(request.getRequestURI()).thenReturn("/api/functions");
        when(request.getPathInfo()).thenReturn("//points");
        s.doGet(request, response);

        reset(response); when(response.getWriter()).thenReturn(printWriter);
        when(request.getPathInfo()).thenReturn("/");
        when(request.getParameter("sort")).thenReturn(null);
        s.doGet(request, response);
    }

    @Test
    void testUserRoleServletDeleteFound() throws Exception {
        UserRoleServlet s = new UserRoleServlet();
        when(request.getRequestURI()).thenReturn("/api/users/1/roles");
        when(request.getPathInfo()).thenReturn("/1");
        when(request.getParameter("roleName")).thenReturn("NONEXISTENT");
        s.doDelete(request, response);
    }

    @Test
    void testFunctionServletPathInfoNull() throws Exception {
        FunctionServlet s = new FunctionServlet();
        when(request.getRequestURI()).thenReturn("/api/functions");
        when(request.getPathInfo()).thenReturn(null);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("{\"userId\":1}")));
        s.doPost(request, response);
    }
}
