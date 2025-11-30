package manual.mapper;

import manual.dto.*;
import manual.entity.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class MapperFullTest {

    @Test
    void testUserMapperAllCases() {
        CreateUserRequest request = new CreateUserRequest("testuser", "password123", "test@test.com");
        UserEntity entity = UserMapper.toEntity(request);
        assertEquals("testuser", entity.getUsername());
        assertEquals("password123", entity.getPassword());
        assertEquals("test@test.com", entity.getEmail());
        assertNotNull(entity.getCreatedAt());

        CreateUserRequest emptyReq = new CreateUserRequest("", "", "");
        UserEntity emptyEntity = UserMapper.toEntity(emptyReq);
        assertEquals("", emptyEntity.getUsername());
        assertEquals("", emptyEntity.getPassword());
        assertEquals("", emptyEntity.getEmail());

        UserEntity respEntity = new UserEntity("user", "pass", "user@test.com");
        respEntity.setUserId(1L);
        LocalDateTime now = LocalDateTime.now();
        respEntity.setCreatedAt(now);
        UserResponse response = UserMapper.toResponse(respEntity);
        assertEquals(1L, response.getUserId());
        assertEquals("user", response.getUsername());
        assertEquals("user@test.com", response.getEmail());
        assertEquals(now, response.getCreatedAt());
    }

    @Test
    void testFunctionMapperAllCases() {
        UserEntity user = new UserEntity("user", "pass", "user@test.com");
        user.setUserId(1L);

        CreateFunctionRequest request = new CreateFunctionRequest(1L, "MyFunc", "LINEAR", "2*x+1", 0.0, 10.0);
        FunctionEntity entity = FunctionMapper.toEntity(request, user);
        assertEquals(user, entity.getUser());
        assertEquals("MyFunc", entity.getFunctionName());
        assertEquals("LINEAR", entity.getFunctionType());
        assertEquals("2*x+1", entity.getFunctionExpression());
        assertEquals(0.0, entity.getXFrom());
        assertEquals(10.0, entity.getXTo());
        assertNotNull(entity.getCreatedAt());

        CreateFunctionRequest nullReq = new CreateFunctionRequest();
        FunctionEntity nullEntity = FunctionMapper.toEntity(nullReq, user);
        assertEquals(user, nullEntity.getUser());
        assertNull(nullEntity.getFunctionName());
        assertNull(nullEntity.getFunctionType());

        FunctionEntity respEntity = new FunctionEntity(user, "Func", "POLY", "x*x", -5.0, 5.0);
        respEntity.setFunctionId(10L);
        LocalDateTime now = LocalDateTime.now();
        respEntity.setCreatedAt(now);
        FunctionResponse response = FunctionMapper.toResponse(respEntity);
        assertEquals(10L, response.getFunctionId());
        assertEquals(1L, response.getUserId());
        assertEquals("Func", response.getFunctionName());
        assertEquals("POLY", response.getFunctionType());
        assertEquals("x*x", response.getFunctionExpression());
        assertEquals(-5.0, response.getXFrom());
        assertEquals(5.0, response.getXTo());
        assertEquals(now, response.getCreatedAt());
    }

    @Test
    void testPointMapperAllCases() {
        UserEntity user = new UserEntity("user", "pass", "user@test.com");
        user.setUserId(1L);
        FunctionEntity function = new FunctionEntity(user, "Func", "LINEAR", "x", 0.0, 10.0);
        function.setFunctionId(1L);

        CreatePointRequest request = new CreatePointRequest(1L, 5.0, 25.0);
        PointEntity entity = PointMapper.toEntity(request, function);
        assertEquals(function, entity.getFunction());
        assertEquals(5.0, entity.getXValue());
        assertEquals(25.0, entity.getYValue());
        assertNotNull(entity.getComputedAt());

        CreatePointRequest zeroReq = new CreatePointRequest(1L, 0.0, 0.0);
        PointEntity zeroEntity = PointMapper.toEntity(zeroReq, function);
        assertEquals(0.0, zeroEntity.getXValue());
        assertEquals(0.0, zeroEntity.getYValue());

        CreatePointRequest negReq = new CreatePointRequest(1L, -3.5, -12.25);
        PointEntity negEntity = PointMapper.toEntity(negReq, function);
        assertEquals(-3.5, negEntity.getXValue());
        assertEquals(-12.25, negEntity.getYValue());

        PointEntity respEntity = new PointEntity(function, 2.5, 6.25);
        respEntity.setPointId(5L);
        LocalDateTime now = LocalDateTime.now();
        respEntity.setComputedAt(now);
        PointResponse response = PointMapper.toResponse(respEntity);
        assertEquals(5L, response.getPointId());
        assertEquals(1L, response.getFunctionId());
        assertEquals(2.5, response.getXValue());
        assertEquals(6.25, response.getYValue());
        assertEquals(now, response.getComputedAt());
    }

    @Test
    void testCompositeFunctionMapperAllCases() {
        UserEntity user = new UserEntity("user", "pass", "user@test.com");
        user.setUserId(1L);
        FunctionEntity first = new FunctionEntity(user, "First", "LINEAR", "x", 0.0, 10.0);
        first.setFunctionId(10L);
        FunctionEntity second = new FunctionEntity(user, "Second", "LINEAR", "2*x", 0.0, 10.0);
        second.setFunctionId(20L);

        CreateCompositeFunctionRequest request = new CreateCompositeFunctionRequest(1L, "Composite", 10L, 20L);
        CompositeFunctionEntity entity = CompositeFunctionMapper.toEntity(request, user, first, second);
        assertEquals(user, entity.getUser());
        assertEquals("Composite", entity.getCompositeName());
        assertEquals(first, entity.getFirstFunction());
        assertEquals(second, entity.getSecondFunction());
        assertNotNull(entity.getCreatedAt());

        CreateCompositeFunctionRequest emptyReq = new CreateCompositeFunctionRequest(1L, "", 1L, 2L);
        CompositeFunctionEntity emptyEntity = CompositeFunctionMapper.toEntity(emptyReq, user, first, second);
        assertEquals("", emptyEntity.getCompositeName());

        CompositeFunctionEntity respEntity = new CompositeFunctionEntity(user, "MyComposite", first, second);
        respEntity.setCompositeId(5L);
        LocalDateTime now = LocalDateTime.now();
        respEntity.setCreatedAt(now);
        CompositeFunctionResponse response = CompositeFunctionMapper.toResponse(respEntity);
        assertEquals(5L, response.getCompositeId());
        assertEquals(1L, response.getUserId());
        assertEquals("MyComposite", response.getCompositeName());
        assertEquals(10L, response.getFirstFunctionId());
        assertEquals(20L, response.getSecondFunctionId());
        assertEquals(now, response.getCreatedAt());
    }
}

