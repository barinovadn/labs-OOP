package manual.dto;

import manual.entity.UserEntity;
import manual.entity.FunctionEntity;
import manual.entity.PointEntity;
import manual.entity.CompositeFunctionEntity;
import manual.mapper.UserMapper;
import manual.mapper.FunctionMapper;
import manual.mapper.PointMapper;
import manual.mapper.CompositeFunctionMapper;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class DtoMapperTest {

    @Test
    void testUserMapping() {
        CreateUserRequest request = new CreateUserRequest("testuser", "password", "test@email.com");
        UserEntity entity = UserMapper.toEntity(request);

        assertEquals("testuser", entity.getUsername());
        assertEquals("password", entity.getPassword());
        assertEquals("test@email.com", entity.getEmail());
        assertNotNull(entity.getCreatedAt());

        entity.setUserId(1L);
        entity.setCreatedAt(LocalDateTime.now());

        UserResponse response = UserMapper.toResponse(entity);
        assertEquals(1L, response.getUserId());
        assertEquals("testuser", response.getUsername());
        assertEquals("test@email.com", response.getEmail());
        assertNotNull(response.getCreatedAt());
    }

    @Test
    void testFunctionMapping() {
        UserEntity user = new UserEntity("user", "pass", "email@test.com");
        user.setUserId(1L);

        CreateFunctionRequest request = new CreateFunctionRequest(1L, "func", "SQR", "x*x", 0.0, 10.0);
        FunctionEntity entity = FunctionMapper.toEntity(request, user);

        assertEquals(user, entity.getUser());
        assertEquals("func", entity.getFunctionName());
        assertEquals("SQR", entity.getFunctionType());
        assertEquals("x*x", entity.getFunctionExpression());
        assertEquals(0.0, entity.getXFrom());
        assertEquals(10.0, entity.getXTo());

        entity.setFunctionId(1L);
        entity.setCreatedAt(LocalDateTime.now());

        FunctionResponse response = FunctionMapper.toResponse(entity);
        assertEquals(1L, response.getFunctionId());
        assertEquals(1L, response.getUserId());
        assertEquals("func", response.getFunctionName());
        assertEquals("SQR", response.getFunctionType());
    }

    @Test
    void testPointMapping() {
        UserEntity user = new UserEntity("user", "pass", "email@test.com");
        user.setUserId(1L);

        FunctionEntity function = new FunctionEntity(user, "func", "TYPE", "x", 0.0, 5.0);
        function.setFunctionId(1L);

        CreatePointRequest request = new CreatePointRequest(1L, 2.0, 4.0);
        PointEntity entity = PointMapper.toEntity(request, function);

        assertEquals(function, entity.getFunction());
        assertEquals(2.0, entity.getXValue());
        assertEquals(4.0, entity.getYValue());

        entity.setPointId(1L);
        entity.setComputedAt(LocalDateTime.now());

        PointResponse response = PointMapper.toResponse(entity);
        assertEquals(1L, response.getPointId());
        assertEquals(1L, response.getFunctionId());
        assertEquals(2.0, response.getXValue());
        assertEquals(4.0, response.getYValue());
    }

    @Test
    void testCompositeFunctionMapping() {
        UserEntity user = new UserEntity("user", "pass", "email@test.com");
        user.setUserId(1L);

        FunctionEntity firstFunc = new FunctionEntity(user, "f", "TYPE1", "x", 0.0, 1.0);
        firstFunc.setFunctionId(1L);

        FunctionEntity secondFunc = new FunctionEntity(user, "g", "TYPE2", "x+1", 0.0, 1.0);
        secondFunc.setFunctionId(2L);

        CreateCompositeFunctionRequest request = new CreateCompositeFunctionRequest(1L, "composite", 1L, 2L);
        CompositeFunctionEntity entity = CompositeFunctionMapper.toEntity(request, user, firstFunc, secondFunc);

        assertEquals(user, entity.getUser());
        assertEquals("composite", entity.getCompositeName());
        assertEquals(firstFunc, entity.getFirstFunction());
        assertEquals(secondFunc, entity.getSecondFunction());

        entity.setCompositeId(1L);
        entity.setCreatedAt(LocalDateTime.now());

        CompositeFunctionResponse response = CompositeFunctionMapper.toResponse(entity);
        assertEquals(1L, response.getCompositeId());
        assertEquals(1L, response.getUserId());
        assertEquals("composite", response.getCompositeName());
        assertEquals(1L, response.getFirstFunctionId());
        assertEquals(2L, response.getSecondFunctionId());
    }
}