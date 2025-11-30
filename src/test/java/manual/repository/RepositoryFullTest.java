package manual.repository;

import manual.DatabaseConnection;
import manual.dto.*;
import manual.entity.*;
import manual.security.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RepositoryFullTest {

    private Connection connection;
    private UserRepository userRepo;
    private FunctionRepository functionRepo;
    private PointRepository pointRepo;
    private CompositeFunctionRepository compositeRepo;
    private RoleRepository roleRepo;

    @BeforeEach
    void setUp() throws SQLException {
        connection = DatabaseConnection.getConnection();
        userRepo = new UserRepository(connection);
        functionRepo = new FunctionRepository(connection);
        pointRepo = new PointRepository(connection);
        compositeRepo = new CompositeFunctionRepository(connection);
        roleRepo = new RoleRepository(connection);
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    void testUserRepositoryFullCoverage() throws SQLException {
        assertNull(userRepo.findById(-1L));
        assertNull(userRepo.findById(0L));
        assertNull(userRepo.findEntityByUsername("nonexistentuser12345678"));
        assertFalse(userRepo.delete(-1L));
        List<UserResponse> all = userRepo.findAll();
        assertNotNull(all);

        CreateUserRequest createReq = new CreateUserRequest("testuser_repo", "pass", "repo@test.com");
        UserResponse created = userRepo.create(createReq);
        assertNotNull(created);
        assertTrue(created.getUserId() > 0);

        UserResponse found = userRepo.findById(created.getUserId());
        assertNotNull(found);
        assertEquals("testuser_repo", found.getUsername());

        UserEntity entity = userRepo.findEntityByUsername("testuser_repo");
        assertNotNull(entity);

        CreateUserRequest updateReq = new CreateUserRequest("testuser_repo", "newpass", "updated@test.com");
        UserResponse updated = userRepo.update(created.getUserId(), updateReq);
        assertNotNull(updated);

        assertTrue(userRepo.delete(created.getUserId()));
        assertNull(userRepo.findById(created.getUserId()));
    }

    @Test
    void testFunctionRepositoryFullCoverage() throws SQLException {
        assertNull(functionRepo.findById(-1L));
        assertNull(functionRepo.findById(0L));
        assertTrue(functionRepo.findByUserId(-1L).isEmpty());
        assertFalse(functionRepo.delete(-1L));
        List<FunctionResponse> all = functionRepo.findAll();
        assertNotNull(all);

        CreateUserRequest userReq = new CreateUserRequest("functest_user2", "pass", "functest2@test.com");
        UserResponse user = userRepo.create(userReq);
        UserEntity userEntity = userRepo.findEntityByUsername("functest_user2");

        CreateFunctionRequest funcReq = new CreateFunctionRequest(user.getUserId(), "TestFunc2", "LINEAR", "x+1", 0.0, 10.0);
        FunctionResponse created = functionRepo.create(funcReq, userEntity);
        assertNotNull(created);
        assertTrue(created.getFunctionId() > 0);

        FunctionResponse found = functionRepo.findById(created.getFunctionId());
        assertNotNull(found);
        assertEquals("TestFunc2", found.getFunctionName());

        List<FunctionResponse> byUser = functionRepo.findByUserId(user.getUserId());
        assertFalse(byUser.isEmpty());

        CreateFunctionRequest updateReq = new CreateFunctionRequest(user.getUserId(), "Updated", "POLY", "x*x", -5.0, 5.0);
        FunctionResponse updated = functionRepo.update(created.getFunctionId(), updateReq);
        assertNotNull(updated);

        assertTrue(functionRepo.delete(created.getFunctionId()));
        userRepo.delete(user.getUserId());
    }

    @Test
    void testPointRepositoryFullCoverage() throws SQLException {
        assertNull(pointRepo.findById(-1L));
        assertNull(pointRepo.findById(0L));
        assertTrue(pointRepo.findByFunctionId(-1L).isEmpty());
        assertFalse(pointRepo.delete(-1L));

        CreateUserRequest userReq = new CreateUserRequest("pointtest_user2", "pass", "pointtest2@test.com");
        UserResponse user = userRepo.create(userReq);
        UserEntity userEntity = userRepo.findEntityByUsername("pointtest_user2");

        CreateFunctionRequest funcReq = new CreateFunctionRequest(user.getUserId(), "PointFunc2", "LINEAR", "x", 0.0, 10.0);
        FunctionResponse func = functionRepo.create(funcReq, userEntity);
        FunctionEntity funcEntity = new FunctionEntity(userEntity, "PointFunc2", "LINEAR", "x", 0.0, 10.0);
        funcEntity.setFunctionId(func.getFunctionId());

        CreatePointRequest pointReq = new CreatePointRequest(func.getFunctionId(), 5.0, 5.0);
        PointResponse created = pointRepo.create(pointReq, funcEntity);
        assertNotNull(created);
        assertTrue(created.getPointId() > 0);

        PointResponse found = pointRepo.findById(created.getPointId());
        assertNotNull(found);
        assertEquals(5.0, found.getXValue());

        List<PointResponse> byFunc = pointRepo.findByFunctionId(func.getFunctionId());
        assertFalse(byFunc.isEmpty());

        PointResponse byX = pointRepo.findByFunctionIdAndX(func.getFunctionId(), 5.0);
        assertNotNull(byX);

        CreatePointRequest updateReq = new CreatePointRequest(func.getFunctionId(), 5.0, 10.0);
        PointResponse updated = pointRepo.update(created.getPointId(), updateReq);
        assertNotNull(updated);

        assertTrue(pointRepo.delete(created.getPointId()));
        functionRepo.delete(func.getFunctionId());
        userRepo.delete(user.getUserId());
    }

    @Test
    void testCompositeFunctionRepositoryFullCoverage() throws SQLException {
        assertNull(compositeRepo.findById(-1L));
        assertNull(compositeRepo.findById(0L));
        assertTrue(compositeRepo.findByUserId(-1L).isEmpty());
        assertFalse(compositeRepo.delete(-1L));

        CreateUserRequest userReq = new CreateUserRequest("comptest_user2", "pass", "comptest2@test.com");
        UserResponse user = userRepo.create(userReq);
        UserEntity userEntity = userRepo.findEntityByUsername("comptest_user2");

        CreateFunctionRequest func1Req = new CreateFunctionRequest(user.getUserId(), "Func1_2", "LINEAR", "x", 0.0, 10.0);
        FunctionResponse func1 = functionRepo.create(func1Req, userEntity);
        FunctionEntity func1Entity = new FunctionEntity(userEntity, "Func1_2", "LINEAR", "x", 0.0, 10.0);
        func1Entity.setFunctionId(func1.getFunctionId());

        CreateFunctionRequest func2Req = new CreateFunctionRequest(user.getUserId(), "Func2_2", "LINEAR", "2*x", 0.0, 10.0);
        FunctionResponse func2 = functionRepo.create(func2Req, userEntity);
        FunctionEntity func2Entity = new FunctionEntity(userEntity, "Func2_2", "LINEAR", "2*x", 0.0, 10.0);
        func2Entity.setFunctionId(func2.getFunctionId());

        CreateCompositeFunctionRequest compReq = new CreateCompositeFunctionRequest(user.getUserId(), "Composite2", func1.getFunctionId(), func2.getFunctionId());
        CompositeFunctionResponse created = compositeRepo.create(compReq, userEntity, func1Entity, func2Entity);
        assertNotNull(created);
        assertTrue(created.getCompositeId() > 0);

        CompositeFunctionResponse found = compositeRepo.findById(created.getCompositeId());
        assertNotNull(found);
        assertEquals("Composite2", found.getCompositeName());

        List<CompositeFunctionResponse> byUser = compositeRepo.findByUserId(user.getUserId());
        assertFalse(byUser.isEmpty());

        CreateCompositeFunctionRequest updateReq = new CreateCompositeFunctionRequest(user.getUserId(), "Updated2", func1.getFunctionId(), func2.getFunctionId());
        CompositeFunctionResponse updated = compositeRepo.update(created.getCompositeId(), updateReq);
        assertNotNull(updated);

        assertTrue(compositeRepo.delete(created.getCompositeId()));
        functionRepo.delete(func1.getFunctionId());
        functionRepo.delete(func2.getFunctionId());
        userRepo.delete(user.getUserId());
    }

    @Test
    void testRoleRepositoryFullCoverage() throws SQLException {
        List<String> allRoles = roleRepo.getAllRoleNames();
        assertNotNull(allRoles);

        Long invalidRoleId = roleRepo.findRoleIdByName("INVALID_ROLE");
        assertNull(invalidRoleId);

        CreateUserRequest userReq = new CreateUserRequest("roletest_user2", "pass", "roletest2@test.com");
        UserResponse user = userRepo.create(userReq);

        List<Role> initialRoles = roleRepo.findRolesByUserId(user.getUserId());
        assertNotNull(initialRoles);

        roleRepo.assignRoleToUser(user.getUserId(), "USER");
        roleRepo.assignRoleToUser(user.getUserId(), "USER");

        boolean assignInvalid = roleRepo.assignRoleToUser(user.getUserId(), "INVALID_ROLE");
        assertFalse(assignInvalid);

        List<Role> roles = roleRepo.findRolesByUserId(user.getUserId());
        assertNotNull(roles);

        roleRepo.removeRoleFromUser(user.getUserId(), "USER");

        boolean removeInvalid = roleRepo.removeRoleFromUser(user.getUserId(), "INVALID_ROLE");
        assertFalse(removeInvalid);

        userRepo.delete(user.getUserId());
    }

    @Test
    void testRepositoryUpdateNonExistent() throws SQLException {
        CreateUserRequest userReq = new CreateUserRequest("u", "p", "e@test.com");
        UserResponse updated = userRepo.update(-1L, userReq);
        assertNull(updated);

        CreateFunctionRequest funcReq = new CreateFunctionRequest(1L, "F", "T", "x", 0.0, 1.0);
        FunctionResponse funcUpdated = functionRepo.update(-1L, funcReq);
        assertNull(funcUpdated);

        CreatePointRequest pointReq = new CreatePointRequest(1L, 1.0, 1.0);
        PointResponse pointUpdated = pointRepo.update(-1L, pointReq);
        assertNull(pointUpdated);

        CreateCompositeFunctionRequest compReq = new CreateCompositeFunctionRequest(1L, "C", 1L, 2L);
        CompositeFunctionResponse compUpdated = compositeRepo.update(-1L, compReq);
        assertNull(compUpdated);
    }
}
