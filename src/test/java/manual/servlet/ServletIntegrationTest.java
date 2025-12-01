package manual.servlet;

import manual.DatabaseConnection;
import manual.dto.CreateUserRequest;
import manual.entity.UserEntity;
import manual.repository.FunctionRepository;
import manual.repository.PointRepository;
import manual.repository.RoleRepository;
import manual.repository.UserRepository;
import manual.repository.CompositeFunctionRepository;
import manual.security.SecurityContext;
import manual.security.Role;
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
import java.sql.Connection;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServletIntegrationTest {

    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;

    private Long testUserId;
    private Long testFunctionId;
    private Long testFunctionId2;
    private Long testPointId;
    private Long testCompositeId;

    @BeforeEach
    void setUp() throws Exception {
        long ts = System.currentTimeMillis();
        try (Connection conn = DatabaseConnection.getConnection()) {
            UserRepository userRepo = new UserRepository(conn);
            CreateUserRequest userReq = new CreateUserRequest();
            userReq.setUsername("inttest" + ts);
            userReq.setPassword("pass" + ts);
            userReq.setEmail("int" + ts + "@test.com");
            manual.dto.UserResponse userResp = userRepo.create(userReq);
            testUserId = userResp.getUserId();

            RoleRepository roleRepo = new RoleRepository(conn);
            roleRepo.assignRoleToUser(testUserId, "USER");

            FunctionRepository funcRepo = new FunctionRepository(conn);
            manual.dto.CreateFunctionRequest funcReq = new manual.dto.CreateFunctionRequest();
            funcReq.setFunctionName("intfunc" + ts);
            funcReq.setFunctionType("LINEAR");
            funcReq.setFunctionExpression("x");
            funcReq.setXFrom(0.0);
            funcReq.setXTo(10.0);
            UserEntity user = new UserEntity();
            user.setUserId(testUserId);
            manual.dto.FunctionResponse funcResp = funcRepo.create(funcReq, user);
            testFunctionId = funcResp.getFunctionId();

            funcReq.setFunctionName("intfunc2" + ts);
            manual.dto.FunctionResponse funcResp2 = funcRepo.create(funcReq, user);
            testFunctionId2 = funcResp2.getFunctionId();

            PointRepository pointRepo = new PointRepository(conn);
            manual.dto.CreatePointRequest pointReq = new manual.dto.CreatePointRequest();
            pointReq.setXValue(1.0 + ts % 1000);
            pointReq.setYValue(2.0);
            manual.entity.FunctionEntity funcEntity = new manual.entity.FunctionEntity();
            funcEntity.setFunctionId(testFunctionId);
            manual.dto.PointResponse pointResp = pointRepo.create(pointReq, funcEntity);
            testPointId = pointResp.getPointId();

            CompositeFunctionRepository compRepo = new CompositeFunctionRepository(conn);
            manual.dto.CreateCompositeFunctionRequest compReq = new manual.dto.CreateCompositeFunctionRequest();
            compReq.setCompositeName("intcomp" + ts);
            manual.entity.FunctionEntity func1 = new manual.entity.FunctionEntity();
            func1.setFunctionId(testFunctionId);
            manual.entity.FunctionEntity func2 = new manual.entity.FunctionEntity();
            func2.setFunctionId(testFunctionId2);
            manual.dto.CompositeFunctionResponse compResp = compRepo.create(compReq, user, func1, func2);
            testCompositeId = compResp.getCompositeId();
        }
    }

    private PrintWriter setupResponse() throws Exception {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        when(response.getWriter()).thenReturn(pw);
        return pw;
    }

    @Test
    void testFunctionServletSuccessPaths() throws Exception {
        FunctionServlet s = new FunctionServlet();
        when(request.getRequestURI()).thenReturn("/api/functions/" + testFunctionId);
        when(request.getPathInfo()).thenReturn("/" + testFunctionId);
        lenient().when(request.getParameter("sort")).thenReturn(null);
        setupResponse();
        s.doGet(request, response);

        reset(response); setupResponse();
        when(request.getPathInfo()).thenReturn("/" + testFunctionId);
        String json = "{\"functionName\":\"updated\",\"functionType\":\"LINEAR\",\"functionExpression\":\"x\",\"xFrom\":0.0,\"xTo\":10.0}";
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(json)));
        s.doPut(request, response);

        reset(response); setupResponse();
        when(request.getPathInfo()).thenReturn("/" + testFunctionId + "/points");
        s.doGet(request, response);

        reset(response); setupResponse();
        when(request.getPathInfo()).thenReturn("/" + testFunctionId + "/points");
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("{\"xValue\":" + (System.currentTimeMillis() % 10000) + ".1,\"yValue\":3.0}")));
        s.doPost(request, response);
        verify(response).setStatus(HttpServletResponse.SC_CREATED);

        reset(response); setupResponse();
        when(request.getPathInfo()).thenReturn("/" + testFunctionId + "/differentiate");
        lenient().when(request.getParameter("type")).thenReturn("LEFT");
        s.doPost(request, response);

        reset(response); setupResponse();
        when(request.getPathInfo()).thenReturn("/" + testFunctionId + "/differentiate");
        lenient().when(request.getParameter("type")).thenReturn(null);
        s.doPost(request, response);

        reset(response); setupResponse();
        when(request.getPathInfo()).thenReturn("/" + testFunctionId + "/calculate");
        when(request.getParameter("x")).thenReturn("5.0");
        s.doPost(request, response);
    }

    @Test
    void testPointServletSuccessPaths() throws Exception {
        PointServlet s = new PointServlet();
        when(request.getRequestURI()).thenReturn("/api/points/" + testPointId);
        when(request.getPathInfo()).thenReturn("/" + testPointId);
        setupResponse();
        s.doGet(request, response);

        reset(response); setupResponse();
        when(request.getPathInfo()).thenReturn("/" + testPointId);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("{\"xValue\":5.0,\"yValue\":10.0}")));
        s.doPut(request, response);

        reset(response); lenient().when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));
        when(request.getPathInfo()).thenReturn("/" + testPointId);
        s.doDelete(request, response);
        verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Test
    void testCompositeFunctionServletSuccessPaths() throws Exception {
        CompositeFunctionServlet s = new CompositeFunctionServlet();
        when(request.getRequestURI()).thenReturn("/api/composite-functions/" + testCompositeId);
        when(request.getPathInfo()).thenReturn("/" + testCompositeId);
        setupResponse();
        s.doGet(request, response);

        reset(response); setupResponse();
        when(request.getPathInfo()).thenReturn("/" + testCompositeId);
        s.doPut(request, response);

        reset(response); lenient().when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));
        when(request.getPathInfo()).thenReturn("/" + testCompositeId);
        s.doDelete(request, response);
        verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Test
    void testUserRoleServletSuccessPaths() throws Exception {
        UserRoleServlet s = new UserRoleServlet();
        when(request.getRequestURI()).thenReturn("/api/users/" + testUserId + "/roles");
        when(request.getPathInfo()).thenReturn("/" + testUserId);
        setupResponse();
        s.doGet(request, response);

        reset(response); setupResponse();
        when(request.getPathInfo()).thenReturn("/" + testUserId);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("{\"roleName\":\"OPERATOR\"}")));
        s.doPost(request, response);
        verify(response).setStatus(HttpServletResponse.SC_CREATED);

        reset(response); lenient().when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));
        when(request.getPathInfo()).thenReturn("/" + testUserId);
        when(request.getParameter("roleName")).thenReturn("OPERATOR");
        s.doDelete(request, response);
        verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Test
    void testFunctionServletDeleteSuccess() throws Exception {
        long ts = System.currentTimeMillis();
        Long tempFuncId;
        try (Connection conn = DatabaseConnection.getConnection()) {
            FunctionRepository funcRepo = new FunctionRepository(conn);
            manual.dto.CreateFunctionRequest funcReq = new manual.dto.CreateFunctionRequest();
            funcReq.setFunctionName("todelete" + ts);
            funcReq.setFunctionType("LINEAR");
            funcReq.setFunctionExpression("x");
            funcReq.setXFrom(0.0);
            funcReq.setXTo(10.0);
            UserEntity user = new UserEntity();
            user.setUserId(testUserId);
            manual.dto.FunctionResponse funcResp = funcRepo.create(funcReq, user);
            tempFuncId = funcResp.getFunctionId();
        }

        FunctionServlet s = new FunctionServlet();
        when(request.getRequestURI()).thenReturn("/api/functions/" + tempFuncId);
        when(request.getPathInfo()).thenReturn("/" + tempFuncId);
        lenient().when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));
        s.doDelete(request, response);
        verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}

