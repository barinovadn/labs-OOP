package service;

import dto.*;
import entity.*;
import repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServiceTest {

    @Mock
    private FunctionRepository functionRepository;
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private PointRepository pointRepository;
    
    @Mock
    private RoleRepository roleRepository;
    
    @Mock
    private CompositeFunctionRepository compositeFunctionRepository;
    
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private FunctionService functionService;
    
    @InjectMocks
    private PointService pointService;
    
    @InjectMocks
    private UserService userService;
    
    @InjectMocks
    private RoleService roleService;
    
    @InjectMocks
    private CompositeFunctionService compositeFunctionService;
    
    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    private UserEntity testUser;
    private FunctionEntity testFunction;
    private PointEntity testPoint;
    private RoleEntity testRole;
    private CompositeFunctionEntity testComposite;

    @BeforeEach
    void setUp() {
        testUser = new UserEntity("testuser", "password123", "test@email.com");
        testUser.setUserId(1L);
        testUser.setCreatedAt(LocalDateTime.now());
        
        testFunction = new FunctionEntity(testUser, "TestFunc", "POLYNOMIAL", "x^2", 0.0, 10.0);
        testFunction.setFunctionId(1L);
        testFunction.setCreatedAt(LocalDateTime.now());
        
        testPoint = new PointEntity(testFunction, 2.0, 4.0);
        testPoint.setPointId(1L);
        testPoint.setComputedAt(LocalDateTime.now());
        
        testRole = new RoleEntity("USER", "Default user role");
        testRole.setRoleId(1L);
        
        testComposite = new CompositeFunctionEntity(testUser, "composite", testFunction, testFunction);
        testComposite.setCompositeId(1L);
        testComposite.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void testFunctionServiceCreateFunction() {
        FunctionRequest request = new FunctionRequest();
        request.setUserId(1L);
        request.setFunctionName("NewFunc");
        request.setFunctionType("LINEAR");
        request.setFunctionExpression("2x+1");
        request.setXFrom(0.0);
        request.setXTo(5.0);

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(functionRepository.save(any(FunctionEntity.class))).thenReturn(testFunction);

        FunctionResponse response = functionService.createFunction(request);

        assertNotNull(response);
        assertEquals(testFunction.getFunctionId(), response.getFunctionId());
        verify(functionRepository).save(any(FunctionEntity.class));
    }

    @Test
    void testFunctionServiceCreateFunctionUserNotFound() {
        FunctionRequest request = new FunctionRequest();
        request.setUserId(999L);

        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> functionService.createFunction(request));
    }

    @Test
    void testFunctionServiceGetFunctionById() {
        when(functionRepository.findById(1L)).thenReturn(Optional.of(testFunction));

        FunctionResponse response = functionService.getFunctionById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getFunctionId());
    }

    @Test
    void testFunctionServiceGetFunctionByIdNotFound() {
        when(functionRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> functionService.getFunctionById(999L));
    }

    @Test
    void testFunctionServiceGetFunctionsByUserId() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(functionRepository.findByUser(testUser)).thenReturn(Arrays.asList(testFunction));

        List<FunctionResponse> responses = functionService.getFunctionsByUserId(1L);

        assertNotNull(responses);
        assertEquals(1, responses.size());
    }

    @Test
    void testFunctionServiceGetAllFunctionsWithSorting() {
        FunctionEntity func2 = new FunctionEntity(testUser, "AFunc", "LINEAR", "x", 0.0, 1.0);
        func2.setFunctionId(2L);
        func2.setCreatedAt(LocalDateTime.now().plusDays(1));
        
        when(functionRepository.findAll()).thenReturn(Arrays.asList(testFunction, func2));

        // by name
        List<FunctionResponse> byName = functionService.getAllFunctions("name");
        assertNotNull(byName);
        
        // by type
        List<FunctionResponse> byType = functionService.getAllFunctions("type");
        assertNotNull(byType);
        
        // by created
        List<FunctionResponse> byCreated = functionService.getAllFunctions("created");
        assertNotNull(byCreated);
        
        // no sort
        List<FunctionResponse> noSort = functionService.getAllFunctions(null);
        assertNotNull(noSort);
        
        // unknown
        List<FunctionResponse> unknownSort = functionService.getAllFunctions("unknown");
        assertNotNull(unknownSort);
    }

    @Test
    void testFunctionServiceUpdateFunction() {
        FunctionRequest request = new FunctionRequest();
        request.setFunctionName("UpdatedFunc");
        request.setFunctionType("EXPONENTIAL");
        request.setFunctionExpression("e^x");
        request.setXFrom(-5.0);
        request.setXTo(5.0);

        when(functionRepository.findById(1L)).thenReturn(Optional.of(testFunction));
        when(functionRepository.save(any(FunctionEntity.class))).thenReturn(testFunction);

        FunctionResponse response = functionService.updateFunction(1L, request);

        assertNotNull(response);
        verify(functionRepository).save(any(FunctionEntity.class));
    }

    @Test
    void testFunctionServiceDeleteFunction() {
        when(functionRepository.existsById(1L)).thenReturn(true);
        doNothing().when(functionRepository).deleteById(1L);

        functionService.deleteFunction(1L);

        verify(functionRepository).deleteById(1L);
    }

    @Test
    void testFunctionServiceDeleteFunctionNotFound() {
        when(functionRepository.existsById(999L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> functionService.deleteFunction(999L));
    }

    @Test
    void testPointServiceCreatePoint() {
        PointRequest request = new PointRequest(1L, 3.0, 9.0);

        when(functionRepository.findById(1L)).thenReturn(Optional.of(testFunction));
        when(pointRepository.save(any(PointEntity.class))).thenReturn(testPoint);

        PointResponse response = pointService.createPoint(1L, request);

        assertNotNull(response);
        verify(pointRepository).save(any(PointEntity.class));
    }

    @Test
    void testPointServiceCreatePointFunctionNotFound() {
        PointRequest request = new PointRequest(999L, 1.0, 1.0);

        when(functionRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> pointService.createPoint(999L, request));
    }

    @Test
    void testPointServiceGetPointById() {
        when(pointRepository.findById(1L)).thenReturn(Optional.of(testPoint));

        PointResponse response = pointService.getPointById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getPointId());
    }

    @Test
    void testPointServiceGetPointByIdNotFound() {
        when(pointRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> pointService.getPointById(999L));
    }

    @Test
    void testPointServiceGetPointsByFunctionId() {
        when(functionRepository.findById(1L)).thenReturn(Optional.of(testFunction));
        when(pointRepository.findByFunction(testFunction)).thenReturn(Arrays.asList(testPoint));

        List<PointResponse> responses = pointService.getPointsByFunctionId(1L);

        assertNotNull(responses);
        assertEquals(1, responses.size());
    }

    @Test
    void testPointServiceUpdatePoint() {
        PointRequest request = new PointRequest(1L, 5.0, 25.0);

        when(pointRepository.findById(1L)).thenReturn(Optional.of(testPoint));
        when(pointRepository.save(any(PointEntity.class))).thenReturn(testPoint);

        PointResponse response = pointService.updatePoint(1L, request);

        assertNotNull(response);
        verify(pointRepository).save(any(PointEntity.class));
    }

    @Test
    void testPointServiceDeletePoint() {
        when(pointRepository.existsById(1L)).thenReturn(true);
        doNothing().when(pointRepository).deleteById(1L);

        pointService.deletePoint(1L);

        verify(pointRepository).deleteById(1L);
    }

    @Test
    void testPointServiceDeletePointNotFound() {
        when(pointRepository.existsById(999L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> pointService.deletePoint(999L));
    }

    @Test
    void testUserServiceCreateUser() {
        UserRequest request = new UserRequest("newuser", "pass123", "new@email.com");

        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("new@email.com")).thenReturn(false);
        when(passwordEncoder.encode("pass123")).thenReturn("encodedPass");
        when(roleRepository.findByRoleName("USER")).thenReturn(Optional.of(testRole));
        when(userRepository.save(any(UserEntity.class))).thenReturn(testUser);

        UserResponse response = userService.createUser(request);

        assertNotNull(response);
        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    void testUserServiceCreateUserWithNewRole() {
        UserRequest request = new UserRequest("newuser", "pass123", "new@email.com");

        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("new@email.com")).thenReturn(false);
        when(passwordEncoder.encode("pass123")).thenReturn("encodedPass");
        when(roleRepository.findByRoleName("USER")).thenReturn(Optional.empty());
        when(roleRepository.save(any(RoleEntity.class))).thenReturn(testRole);
        when(userRepository.save(any(UserEntity.class))).thenReturn(testUser);

        UserResponse response = userService.createUser(request);

        assertNotNull(response);
    }

    @Test
    void testUserServiceCreateUserDuplicateUsername() {
        UserRequest request = new UserRequest("existinguser", "pass", "email@test.com");

        when(userRepository.existsByUsername("existinguser")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> userService.createUser(request));
    }

    @Test
    void testUserServiceCreateUserDuplicateEmail() {
        UserRequest request = new UserRequest("newuser", "pass", "existing@email.com");

        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("existing@email.com")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> userService.createUser(request));
    }

    @Test
    void testUserServiceGetUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        UserResponse response = userService.getUserById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getUserId());
    }

    @Test
    void testUserServiceGetAllUsers() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(testUser));

        List<UserResponse> responses = userService.getAllUsers();

        assertNotNull(responses);
        assertEquals(1, responses.size());
    }

    @Test
    void testUserServiceUpdateUser() {
        UserRequest request = new UserRequest("updateduser", "newpass", "updated@email.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.encode("newpass")).thenReturn("encodedNewPass");
        when(userRepository.save(any(UserEntity.class))).thenReturn(testUser);

        UserResponse response = userService.updateUser(1L, request);

        assertNotNull(response);
    }

    @Test
    void testUserServiceUpdateUserNoPasswordChange() {
        UserRequest request = new UserRequest("updateduser", "", "updated@email.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(UserEntity.class))).thenReturn(testUser);

        UserResponse response = userService.updateUser(1L, request);

        assertNotNull(response);
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void testUserServiceAssignRoles() {
        AssignRoleRequest request = new AssignRoleRequest();
        request.setUserId(1L);
        request.setRoleIds(Arrays.asList(1L));

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(roleRepository.findById(1L)).thenReturn(Optional.of(testRole));
        when(userRepository.save(any(UserEntity.class))).thenReturn(testUser);

        UserResponse response = userService.assignRoles(request);

        assertNotNull(response);
    }

    @Test
    void testUserServiceDeleteUser() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    void testRoleServiceCreateRole() {
        RoleRequest request = new RoleRequest();
        request.setRoleName("ADMIN");
        request.setDescription("Admin role");

        when(roleRepository.existsByRoleName("ADMIN")).thenReturn(false);
        when(roleRepository.save(any(RoleEntity.class))).thenReturn(testRole);

        RoleResponse response = roleService.createRole(request);

        assertNotNull(response);
    }

    @Test
    void testRoleServiceCreateRoleDuplicate() {
        RoleRequest request = new RoleRequest();
        request.setRoleName("EXISTING");

        when(roleRepository.existsByRoleName("EXISTING")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> roleService.createRole(request));
    }

    @Test
    void testRoleServiceGetRoleById() {
        when(roleRepository.findById(1L)).thenReturn(Optional.of(testRole));

        RoleResponse response = roleService.getRoleById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getRoleId());
    }

    @Test
    void testRoleServiceGetRoleByName() {
        when(roleRepository.findByRoleName("USER")).thenReturn(Optional.of(testRole));

        RoleResponse response = roleService.getRoleByName("USER");

        assertNotNull(response);
        assertEquals("USER", response.getRoleName());
    }

    @Test
    void testRoleServiceGetAllRoles() {
        when(roleRepository.findAll()).thenReturn(Arrays.asList(testRole));

        List<RoleResponse> responses = roleService.getAllRoles();

        assertNotNull(responses);
        assertEquals(1, responses.size());
    }

    @Test
    void testRoleServiceUpdateRole() {
        RoleRequest request = new RoleRequest();
        request.setRoleName("UPDATED");
        request.setDescription("Updated description");

        when(roleRepository.findById(1L)).thenReturn(Optional.of(testRole));
        when(roleRepository.save(any(RoleEntity.class))).thenReturn(testRole);

        RoleResponse response = roleService.updateRole(1L, request);

        assertNotNull(response);
    }

    @Test
    void testRoleServiceDeleteRole() {
        when(roleRepository.existsById(1L)).thenReturn(true);
        doNothing().when(roleRepository).deleteById(1L);

        roleService.deleteRole(1L);

        verify(roleRepository).deleteById(1L);
    }

    @Test
    void testCompositeFunctionServiceCreate() {
        CompositeFunctionRequest request = new CompositeFunctionRequest();
        request.setUserId(1L);
        request.setCompositeName("f(g(x))");
        request.setFirstFunctionId(1L);
        request.setSecondFunctionId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(functionRepository.findById(1L)).thenReturn(Optional.of(testFunction));
        when(compositeFunctionRepository.save(any(CompositeFunctionEntity.class))).thenReturn(testComposite);

        CompositeFunctionResponse response = compositeFunctionService.createCompositeFunction(request);

        assertNotNull(response);
    }

    @Test
    void testCompositeFunctionServiceGetById() {
        when(compositeFunctionRepository.findById(1L)).thenReturn(Optional.of(testComposite));

        CompositeFunctionResponse response = compositeFunctionService.getCompositeFunctionById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getCompositeId());
    }

    @Test
    void testCompositeFunctionServiceGetByUserId() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(compositeFunctionRepository.findByUser(testUser)).thenReturn(Arrays.asList(testComposite));

        List<CompositeFunctionResponse> responses = compositeFunctionService.getCompositeFunctionsByUserId(1L);

        assertNotNull(responses);
        assertEquals(1, responses.size());
    }

    @Test
    void testCompositeFunctionServiceUpdate() {
        CompositeFunctionRequest request = new CompositeFunctionRequest();
        request.setCompositeName("updated");
        request.setFirstFunctionId(1L);
        request.setSecondFunctionId(1L);

        when(compositeFunctionRepository.findById(1L)).thenReturn(Optional.of(testComposite));
        when(functionRepository.findById(1L)).thenReturn(Optional.of(testFunction));
        when(compositeFunctionRepository.save(any(CompositeFunctionEntity.class))).thenReturn(testComposite);

        CompositeFunctionResponse response = compositeFunctionService.updateCompositeFunction(1L, request);

        assertNotNull(response);
    }

    @Test
    void testCompositeFunctionServiceDelete() {
        when(compositeFunctionRepository.existsById(1L)).thenReturn(true);
        doNothing().when(compositeFunctionRepository).deleteById(1L);

        compositeFunctionService.deleteCompositeFunction(1L);

        verify(compositeFunctionRepository).deleteById(1L);
    }

    @Test
    void testCustomUserDetailsServiceLoadUserByUsername() {
        testUser.setRoles(Arrays.asList(testRole));
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername("testuser");

        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
        assertTrue(userDetails.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
        
        assertTrue(userDetails instanceof security.CustomUserDetails);
        security.CustomUserDetails customUserDetails = (security.CustomUserDetails) userDetails;
        assertEquals(testUser.getUserId(), customUserDetails.getUserId());
        assertEquals(testUser.getEmail(), customUserDetails.getEmail());
        assertSame(testUser, customUserDetails.getUserEntity());
    }

    @Test
    void testCustomUserDetailsServiceLoadUserByUsernameNoRoles() {
        testUser.setRoles(Collections.emptyList());
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername("testuser");

        assertNotNull(userDetails);
        assertTrue(userDetails.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
        
        assertTrue(userDetails instanceof security.CustomUserDetails);
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.isEnabled());
    }

    @Test
    void testCustomUserDetailsServiceLoadUserByUsernameNotFound() {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, 
            () -> customUserDetailsService.loadUserByUsername("unknown"));
    }

    @Test
    void testUserServiceUpdateUserWithNullPassword() {
        UserRequest request = new UserRequest("updateduser", null, "updated@email.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(UserEntity.class))).thenReturn(testUser);

        UserResponse response = userService.updateUser(1L, request);

        assertNotNull(response);
        verify(passwordEncoder, never()).encode(any());
    }

    @Test
    void testUserServiceAssignRolesMultiple() {
        RoleEntity adminRole = new RoleEntity("ADMIN", "Admin role");
        adminRole.setRoleId(2L);
        
        AssignRoleRequest request = new AssignRoleRequest();
        request.setUserId(1L);
        request.setRoleIds(Arrays.asList(1L, 2L));

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(roleRepository.findById(1L)).thenReturn(Optional.of(testRole));
        when(roleRepository.findById(2L)).thenReturn(Optional.of(adminRole));
        when(userRepository.save(any(UserEntity.class))).thenReturn(testUser);

        UserResponse response = userService.assignRoles(request);

        assertNotNull(response);
        verify(roleRepository, times(2)).findById(anyLong());
    }

    @Test
    void testUserServiceAssignRolesRoleNotFound() {
        AssignRoleRequest request = new AssignRoleRequest();
        request.setUserId(1L);
        request.setRoleIds(Arrays.asList(999L));

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(roleRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.assignRoles(request));
    }

    @Test
    void testFunctionServiceGetFunctionsByUserIdUserNotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> functionService.getFunctionsByUserId(999L));
    }

    @Test
    void testFunctionServiceUpdateFunctionNotFound() {
        FunctionRequest request = new FunctionRequest();
        request.setFunctionName("Test");

        when(functionRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> functionService.updateFunction(999L, request));
    }

    @Test
    void testPointServiceGetPointsByFunctionIdNotFound() {
        when(functionRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> pointService.getPointsByFunctionId(999L));
    }

    @Test
    void testPointServiceUpdatePointNotFound() {
        PointRequest request = new PointRequest(1L, 1.0, 1.0);

        when(pointRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> pointService.updatePoint(999L, request));
    }

    @Test
    void testRoleServiceGetRoleByIdNotFound() {
        when(roleRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> roleService.getRoleById(999L));
    }

    @Test
    void testRoleServiceGetRoleByNameNotFound() {
        when(roleRepository.findByRoleName("UNKNOWN")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> roleService.getRoleByName("UNKNOWN"));
    }

    @Test
    void testRoleServiceDeleteRoleNotFound() {
        when(roleRepository.existsById(999L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> roleService.deleteRole(999L));
    }

    @Test
    void testRoleServiceUpdateRoleNotFound() {
        RoleRequest request = new RoleRequest();
        request.setRoleName("Test");

        when(roleRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> roleService.updateRole(999L, request));
    }

    @Test
    void testCompositeFunctionServiceCreateUserNotFound() {
        CompositeFunctionRequest request = new CompositeFunctionRequest();
        request.setUserId(999L);
        request.setFirstFunctionId(1L);
        request.setSecondFunctionId(1L);

        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, 
            () -> compositeFunctionService.createCompositeFunction(request));
    }

    @Test
    void testCompositeFunctionServiceCreateFirstFunctionNotFound() {
        CompositeFunctionRequest request = new CompositeFunctionRequest();
        request.setUserId(1L);
        request.setFirstFunctionId(999L);
        request.setSecondFunctionId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(functionRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, 
            () -> compositeFunctionService.createCompositeFunction(request));
    }

    @Test
    void testCompositeFunctionServiceCreateSecondFunctionNotFound() {
        CompositeFunctionRequest request = new CompositeFunctionRequest();
        request.setUserId(1L);
        request.setFirstFunctionId(1L);
        request.setSecondFunctionId(999L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(functionRepository.findById(1L)).thenReturn(Optional.of(testFunction));
        when(functionRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, 
            () -> compositeFunctionService.createCompositeFunction(request));
    }

    @Test
    void testCompositeFunctionServiceGetByIdNotFound() {
        when(compositeFunctionRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, 
            () -> compositeFunctionService.getCompositeFunctionById(999L));
    }

    @Test
    void testCompositeFunctionServiceGetByUserIdUserNotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, 
            () -> compositeFunctionService.getCompositeFunctionsByUserId(999L));
    }

    @Test
    void testCompositeFunctionServiceUpdateNotFound() {
        CompositeFunctionRequest request = new CompositeFunctionRequest();
        request.setFirstFunctionId(1L);
        request.setSecondFunctionId(1L);

        when(compositeFunctionRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, 
            () -> compositeFunctionService.updateCompositeFunction(999L, request));
    }

    @Test
    void testCompositeFunctionServiceDeleteNotFound() {
        when(compositeFunctionRepository.existsById(999L)).thenReturn(false);

        assertThrows(RuntimeException.class, 
            () -> compositeFunctionService.deleteCompositeFunction(999L));
    }

    @Test
    void testUserServiceDeleteUserNotFound() {
        when(userRepository.existsById(999L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> userService.deleteUser(999L));
    }

    @Test
    void testUserServiceGetUserByIdNotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.getUserById(999L));
    }

    @Test
    void testCustomUserDetailsServiceMultipleRoles() {
        RoleEntity adminRole = new RoleEntity("ADMIN", "Admin");
        adminRole.setRoleId(2L);
        testUser.setRoles(Arrays.asList(testRole, adminRole));
        
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername("testuser");

        assertNotNull(userDetails);
        assertEquals(2, userDetails.getAuthorities().size());
    }

    @Test
    void testFunctionServiceGetAllFunctionsEmpty() {
        when(functionRepository.findAll()).thenReturn(Collections.emptyList());

        List<FunctionResponse> responses = functionService.getAllFunctions("name");

        assertNotNull(responses);
        assertTrue(responses.isEmpty());
    }
}

