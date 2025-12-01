package controller;

import dto.*;
import service.*;
import entity.*;
import repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ControllerTest {

    @Mock
    private FunctionService functionService;
    
    @Mock
    private PointService pointService;
    
    @Mock
    private UserService userService;
    
    @Mock
    private RoleService roleService;
    
    @Mock
    private CompositeFunctionService compositeFunctionService;
    
    @Mock
    private FunctionRepository functionRepository;
    
    @Mock
    private PointRepository pointRepository;

    @InjectMocks
    private FunctionController functionController;
    
    @InjectMocks
    private PointController pointController;
    
    @InjectMocks
    private UserController userController;
    
    @InjectMocks
    private RoleController roleController;
    
    @InjectMocks
    private AuthController authController;

    private ObjectMapper objectMapper;
    private FunctionResponse testFunctionResponse;
    private PointResponse testPointResponse;
    private UserResponse testUserResponse;
    private RoleResponse testRoleResponse;
    private CompositeFunctionResponse testCompositeResponse;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        
        LocalDateTime now = LocalDateTime.now();
        
        testFunctionResponse = new FunctionResponse();
        testFunctionResponse.setFunctionId(1L);
        testFunctionResponse.setUserId(1L);
        testFunctionResponse.setFunctionName("TestFunc");
        testFunctionResponse.setFunctionType("POLYNOMIAL");
        testFunctionResponse.setCreatedAt(now);
        
        testPointResponse = new PointResponse(1L, 1L, 2.0, 4.0, now);
        
        testUserResponse = new UserResponse(1L, "testuser", "test@email.com", now);
        
        testRoleResponse = new RoleResponse();
        testRoleResponse.setRoleId(1L);
        testRoleResponse.setRoleName("USER");
        testRoleResponse.setDescription("Default user");
        
        testCompositeResponse = new CompositeFunctionResponse();
        testCompositeResponse.setCompositeId(1L);
        testCompositeResponse.setUserId(1L);
        testCompositeResponse.setCompositeName("composite");
        testCompositeResponse.setCreatedAt(now);
    }

    @Test
    void testFunctionControllerCreateFunctionSuccess() {
        FunctionRequest request = new FunctionRequest();
        request.setUserId(1L);
        request.setFunctionName("NewFunc");
        
        when(functionService.createFunction(any(FunctionRequest.class))).thenReturn(testFunctionResponse);

        ResponseEntity<ApiResponse<FunctionResponse>> response = functionController.createFunction(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        assertNotNull(response.getBody().getData());
    }

    @Test
    void testFunctionControllerCreateFunctionError() {
        FunctionRequest request = new FunctionRequest();
        
        when(functionService.createFunction(any(FunctionRequest.class)))
            .thenThrow(new RuntimeException("User not found"));

        ResponseEntity<ApiResponse<FunctionResponse>> response = functionController.createFunction(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
    }

    @Test
    void testFunctionControllerGetFunctionByIdSuccess() {
        when(functionService.getFunctionById(1L)).thenReturn(testFunctionResponse);

        ResponseEntity<ApiResponse<FunctionResponse>> response = functionController.getFunctionById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
    }

    @Test
    void testFunctionControllerGetFunctionByIdNotFound() {
        when(functionService.getFunctionById(999L)).thenThrow(new RuntimeException("Not found"));

        ResponseEntity<ApiResponse<FunctionResponse>> response = functionController.getFunctionById(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
    }

    @Test
    void testFunctionControllerGetAllFunctionsSuccess() {
        when(functionService.getAllFunctions(anyString())).thenReturn(Arrays.asList(testFunctionResponse));

        ResponseEntity<ApiResponse<List<FunctionResponse>>> response = functionController.getAllFunctions("name");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        assertEquals(1, response.getBody().getData().size());
    }

    @Test
    void testFunctionControllerGetAllFunctionsError() {
        when(functionService.getAllFunctions(any())).thenThrow(new RuntimeException("Error"));

        ResponseEntity<ApiResponse<List<FunctionResponse>>> response = functionController.getAllFunctions(null);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testFunctionControllerUpdateFunctionSuccess() {
        FunctionRequest request = new FunctionRequest();
        request.setFunctionName("Updated");
        
        when(functionService.updateFunction(eq(1L), any(FunctionRequest.class))).thenReturn(testFunctionResponse);

        ResponseEntity<ApiResponse<FunctionResponse>> response = functionController.updateFunction(1L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
    }

    @Test
    void testFunctionControllerUpdateFunctionNotFound() {
        FunctionRequest request = new FunctionRequest();
        
        when(functionService.updateFunction(eq(999L), any(FunctionRequest.class)))
            .thenThrow(new RuntimeException("Not found"));

        ResponseEntity<ApiResponse<FunctionResponse>> response = functionController.updateFunction(999L, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testFunctionControllerDeleteFunctionSuccess() {
        doNothing().when(functionService).deleteFunction(1L);

        ResponseEntity<ApiResponse<Void>> response = functionController.deleteFunction(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
    }

    @Test
    void testFunctionControllerDeleteFunctionNotFound() {
        doThrow(new RuntimeException("Not found")).when(functionService).deleteFunction(999L);

        ResponseEntity<ApiResponse<Void>> response = functionController.deleteFunction(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testFunctionControllerCalculateFunctionSuccess() {
        CalculationRequest request = new CalculationRequest();
        TabulatedFunctionRequest funcRequest = new TabulatedFunctionRequest();
        funcRequest.setXValues(new double[]{2.0});
        request.setFunction(funcRequest);
        request.setOperation("calculate");
        
        FunctionEntity functionEntity = new FunctionEntity();
        functionEntity.setFunctionId(1L);
        functionEntity.setFunctionType("POLYNOMIAL");
        
        PointEntity point1 = new PointEntity();
        point1.setXValue(0.0);
        point1.setYValue(0.0);
        PointEntity point2 = new PointEntity();
        point2.setXValue(4.0);
        point2.setYValue(16.0);
        
        when(functionRepository.findById(1L)).thenReturn(Optional.of(functionEntity));
        when(pointRepository.findByFunctionIdOrderByXValue(1L)).thenReturn(Arrays.asList(point1, point2));

        ResponseEntity<ApiResponse<CalculationResponse>> response = functionController.calculateFunction(1L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
    }

    @Test
    void testFunctionControllerCalculateFunctionNotEnoughPoints() {
        CalculationRequest request = new CalculationRequest();
        TabulatedFunctionRequest funcRequest = new TabulatedFunctionRequest();
        funcRequest.setXValues(new double[]{2.0});
        request.setFunction(funcRequest);
        
        FunctionEntity functionEntity = new FunctionEntity();
        functionEntity.setFunctionId(1L);
        
        PointEntity point1 = new PointEntity();
        point1.setXValue(0.0);
        point1.setYValue(0.0);
        
        when(functionRepository.findById(1L)).thenReturn(Optional.of(functionEntity));
        when(pointRepository.findByFunctionIdOrderByXValue(1L)).thenReturn(Arrays.asList(point1));

        ResponseEntity<ApiResponse<CalculationResponse>> response = functionController.calculateFunction(1L, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testFunctionControllerCalculateFunctionWithXFrom() {
        CalculationRequest request = new CalculationRequest();
        TabulatedFunctionRequest funcRequest = new TabulatedFunctionRequest();
        funcRequest.setXFrom(2.0);
        request.setFunction(funcRequest);
        
        FunctionEntity functionEntity = new FunctionEntity();
        functionEntity.setFunctionId(1L);
        functionEntity.setFunctionType("POLYNOMIAL");
        
        PointEntity point1 = new PointEntity();
        point1.setXValue(0.0);
        point1.setYValue(0.0);
        PointEntity point2 = new PointEntity();
        point2.setXValue(4.0);
        point2.setYValue(16.0);
        
        when(functionRepository.findById(1L)).thenReturn(Optional.of(functionEntity));
        when(pointRepository.findByFunctionIdOrderByXValue(1L)).thenReturn(Arrays.asList(point1, point2));

        ResponseEntity<ApiResponse<CalculationResponse>> response = functionController.calculateFunction(1L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testFunctionControllerCalculateFunctionNoXValue() {
        CalculationRequest request = new CalculationRequest();
        TabulatedFunctionRequest funcRequest = new TabulatedFunctionRequest();
        request.setFunction(funcRequest);
        
        FunctionEntity functionEntity = new FunctionEntity();
        functionEntity.setFunctionId(1L);
        
        PointEntity point1 = new PointEntity();
        point1.setXValue(0.0);
        point1.setYValue(0.0);
        PointEntity point2 = new PointEntity();
        point2.setXValue(4.0);
        point2.setYValue(16.0);
        
        when(functionRepository.findById(1L)).thenReturn(Optional.of(functionEntity));
        when(pointRepository.findByFunctionIdOrderByXValue(1L)).thenReturn(Arrays.asList(point1, point2));

        ResponseEntity<ApiResponse<CalculationResponse>> response = functionController.calculateFunction(1L, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testPointControllerCreatePointSuccess() {
        PointRequest request = new PointRequest(1L, 3.0, 9.0);
        
        when(pointService.createPoint(eq(1L), any(PointRequest.class))).thenReturn(testPointResponse);

        ResponseEntity<ApiResponse<PointResponse>> response = pointController.createPoint(1L, request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
    }

    @Test
    void testPointControllerCreatePointError() {
        PointRequest request = new PointRequest();
        
        when(pointService.createPoint(eq(999L), any(PointRequest.class)))
            .thenThrow(new RuntimeException("Function not found"));

        ResponseEntity<ApiResponse<PointResponse>> response = pointController.createPoint(999L, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testPointControllerGetPointByIdSuccess() {
        when(pointService.getPointById(1L)).thenReturn(testPointResponse);

        ResponseEntity<ApiResponse<PointResponse>> response = pointController.getPointById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
    }

    @Test
    void testPointControllerGetPointByIdNotFound() {
        when(pointService.getPointById(999L)).thenThrow(new RuntimeException("Not found"));

        ResponseEntity<ApiResponse<PointResponse>> response = pointController.getPointById(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testPointControllerGetPointsByFunctionIdSuccess() {
        when(pointService.getPointsByFunctionId(1L)).thenReturn(Arrays.asList(testPointResponse));

        ResponseEntity<ApiResponse<List<PointResponse>>> response = pointController.getPointsByFunctionId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getData().size());
    }

    @Test
    void testPointControllerGetPointsByFunctionIdError() {
        when(pointService.getPointsByFunctionId(999L)).thenThrow(new RuntimeException("Not found"));

        ResponseEntity<ApiResponse<List<PointResponse>>> response = pointController.getPointsByFunctionId(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testPointControllerUpdatePointSuccess() {
        PointRequest request = new PointRequest(1L, 5.0, 25.0);
        
        when(pointService.updatePoint(eq(1L), any(PointRequest.class))).thenReturn(testPointResponse);

        ResponseEntity<ApiResponse<PointResponse>> response = pointController.updatePoint(1L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
    }

    @Test
    void testPointControllerUpdatePointNotFound() {
        PointRequest request = new PointRequest();
        
        when(pointService.updatePoint(eq(999L), any(PointRequest.class)))
            .thenThrow(new RuntimeException("Not found"));

        ResponseEntity<ApiResponse<PointResponse>> response = pointController.updatePoint(999L, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testPointControllerDeletePointSuccess() {
        doNothing().when(pointService).deletePoint(1L);

        ResponseEntity<ApiResponse<Void>> response = pointController.deletePoint(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
    }

    @Test
    void testPointControllerDeletePointNotFound() {
        doThrow(new RuntimeException("Not found")).when(pointService).deletePoint(999L);

        ResponseEntity<ApiResponse<Void>> response = pointController.deletePoint(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testUserControllerCreateUserSuccess() {
        UserRequest request = new UserRequest("newuser", "pass", "new@email.com");
        
        when(userService.createUser(any(UserRequest.class))).thenReturn(testUserResponse);

        ResponseEntity<ApiResponse<UserResponse>> response = userController.createUser(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
    }

    @Test
    void testUserControllerCreateUserError() {
        UserRequest request = new UserRequest();
        
        when(userService.createUser(any(UserRequest.class)))
            .thenThrow(new RuntimeException("Username exists"));

        ResponseEntity<ApiResponse<UserResponse>> response = userController.createUser(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testUserControllerGetUserByIdSuccess() {
        when(userService.getUserById(1L)).thenReturn(testUserResponse);

        ResponseEntity<ApiResponse<UserResponse>> response = userController.getUserById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
    }

    @Test
    void testUserControllerGetUserByIdNotFound() {
        when(userService.getUserById(999L)).thenThrow(new RuntimeException("Not found"));

        ResponseEntity<ApiResponse<UserResponse>> response = userController.getUserById(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testUserControllerGetAllUsersSuccess() {
        when(userService.getAllUsers()).thenReturn(Arrays.asList(testUserResponse));

        ResponseEntity<ApiResponse<List<UserResponse>>> response = userController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getData().size());
    }

    @Test
    void testUserControllerGetAllUsersError() {
        when(userService.getAllUsers()).thenThrow(new RuntimeException("Error"));

        ResponseEntity<ApiResponse<List<UserResponse>>> response = userController.getAllUsers();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testUserControllerUpdateUserSuccess() {
        UserRequest request = new UserRequest("updated", "pass", "updated@email.com");
        
        when(userService.updateUser(eq(1L), any(UserRequest.class))).thenReturn(testUserResponse);

        ResponseEntity<ApiResponse<UserResponse>> response = userController.updateUser(1L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
    }

    @Test
    void testUserControllerUpdateUserNotFound() {
        UserRequest request = new UserRequest();
        
        when(userService.updateUser(eq(999L), any(UserRequest.class)))
            .thenThrow(new RuntimeException("Not found"));

        ResponseEntity<ApiResponse<UserResponse>> response = userController.updateUser(999L, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testUserControllerDeleteUserSuccess() {
        doNothing().when(userService).deleteUser(1L);

        ResponseEntity<ApiResponse<Void>> response = userController.deleteUser(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
    }

    @Test
    void testUserControllerDeleteUserNotFound() {
        doThrow(new RuntimeException("Not found")).when(userService).deleteUser(999L);

        ResponseEntity<ApiResponse<Void>> response = userController.deleteUser(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testUserControllerGetFunctionsByUserIdSuccess() {
        when(functionService.getFunctionsByUserId(1L)).thenReturn(Arrays.asList(testFunctionResponse));

        ResponseEntity<ApiResponse<List<FunctionResponse>>> response = userController.getFunctionsByUserId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getData().size());
    }

    @Test
    void testUserControllerGetFunctionsByUserIdError() {
        when(functionService.getFunctionsByUserId(999L)).thenThrow(new RuntimeException("Not found"));

        ResponseEntity<ApiResponse<List<FunctionResponse>>> response = userController.getFunctionsByUserId(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testUserControllerGetCompositeFunctionsByUserIdSuccess() {
        when(compositeFunctionService.getCompositeFunctionsByUserId(1L))
            .thenReturn(Arrays.asList(testCompositeResponse));

        ResponseEntity<ApiResponse<List<CompositeFunctionResponse>>> response = 
            userController.getCompositeFunctionsByUserId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getData().size());
    }

    @Test
    void testUserControllerGetCompositeFunctionsByUserIdError() {
        when(compositeFunctionService.getCompositeFunctionsByUserId(999L))
            .thenThrow(new RuntimeException("Not found"));

        ResponseEntity<ApiResponse<List<CompositeFunctionResponse>>> response = 
            userController.getCompositeFunctionsByUserId(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testRoleControllerCreateRoleSuccess() {
        RoleRequest request = new RoleRequest();
        request.setRoleName("ADMIN");
        request.setDescription("Admin role");
        
        when(roleService.createRole(any(RoleRequest.class))).thenReturn(testRoleResponse);

        ResponseEntity<ApiResponse<RoleResponse>> response = roleController.createRole(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
    }

    @Test
    void testRoleControllerCreateRoleError() {
        RoleRequest request = new RoleRequest();
        
        when(roleService.createRole(any(RoleRequest.class)))
            .thenThrow(new RuntimeException("Role exists"));

        ResponseEntity<ApiResponse<RoleResponse>> response = roleController.createRole(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testRoleControllerGetRoleByIdSuccess() {
        when(roleService.getRoleById(1L)).thenReturn(testRoleResponse);

        ResponseEntity<ApiResponse<RoleResponse>> response = roleController.getRoleById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
    }

    @Test
    void testRoleControllerGetRoleByIdNotFound() {
        when(roleService.getRoleById(999L)).thenThrow(new RuntimeException("Not found"));

        ResponseEntity<ApiResponse<RoleResponse>> response = roleController.getRoleById(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testRoleControllerGetAllRolesSuccess() {
        when(roleService.getAllRoles()).thenReturn(Arrays.asList(testRoleResponse));

        ResponseEntity<ApiResponse<List<RoleResponse>>> response = roleController.getAllRoles();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getData().size());
    }

    @Test
    void testRoleControllerGetAllRolesError() {
        when(roleService.getAllRoles()).thenThrow(new RuntimeException("Error"));

        ResponseEntity<ApiResponse<List<RoleResponse>>> response = roleController.getAllRoles();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testRoleControllerUpdateRoleSuccess() {
        RoleRequest request = new RoleRequest();
        request.setRoleName("UPDATED");
        
        when(roleService.updateRole(eq(1L), any(RoleRequest.class))).thenReturn(testRoleResponse);

        ResponseEntity<ApiResponse<RoleResponse>> response = roleController.updateRole(1L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
    }

    @Test
    void testRoleControllerUpdateRoleNotFound() {
        RoleRequest request = new RoleRequest();
        
        when(roleService.updateRole(eq(999L), any(RoleRequest.class)))
            .thenThrow(new RuntimeException("Not found"));

        ResponseEntity<ApiResponse<RoleResponse>> response = roleController.updateRole(999L, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testRoleControllerDeleteRoleSuccess() {
        doNothing().when(roleService).deleteRole(1L);

        ResponseEntity<ApiResponse<Void>> response = roleController.deleteRole(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
    }

    @Test
    void testRoleControllerDeleteRoleNotFound() {
        doThrow(new RuntimeException("Not found")).when(roleService).deleteRole(999L);

        ResponseEntity<ApiResponse<Void>> response = roleController.deleteRole(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testAuthControllerRegisterSuccess() {
        UserRequest request = new UserRequest("newuser", "pass", "new@email.com");
        
        when(userService.createUser(any(UserRequest.class))).thenReturn(testUserResponse);

        ResponseEntity<ApiResponse<UserResponse>> response = authController.register(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        assertEquals("User registered successfully", response.getBody().getMessage());
    }

    @Test
    void testAuthControllerRegisterError() {
        UserRequest request = new UserRequest();
        
        when(userService.createUser(any(UserRequest.class)))
            .thenThrow(new RuntimeException("Username exists"));

        ResponseEntity<ApiResponse<UserResponse>> response = authController.register(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
    }

    @Test
    void testAuthControllerAssignRolesSuccess() {
        AssignRoleRequest request = new AssignRoleRequest();
        request.setUserId(1L);
        request.setRoleIds(Arrays.asList(1L, 2L));
        
        when(userService.assignRoles(any(AssignRoleRequest.class))).thenReturn(testUserResponse);

        ResponseEntity<ApiResponse<UserResponse>> response = authController.assignRoles(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Roles assigned successfully", response.getBody().getMessage());
    }

    @Test
    void testAuthControllerAssignRolesError() {
        AssignRoleRequest request = new AssignRoleRequest();
        
        when(userService.assignRoles(any(AssignRoleRequest.class)))
            .thenThrow(new RuntimeException("User not found"));

        ResponseEntity<ApiResponse<UserResponse>> response = authController.assignRoles(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
    }

    @Mock
    private CompositeFunctionService compositeService;

    @Test
    void testCompositeFunctionControllerBasicOperations() {
        CompositeFunctionController compositeController = new CompositeFunctionController();
        assertNotNull(compositeController);
    }

    @Test
    void testOperationControllerBasicOperations() {
        OperationController operationController = new OperationController();
        assertNotNull(operationController);
    }
}

