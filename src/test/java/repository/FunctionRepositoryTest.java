package repository;

import entity.FunctionEntity;
import entity.UserEntity;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class FunctionRepositoryTest {

    @Test
    void testFunctionCreation() {
        UserEntity user = new UserEntity("user", "pass", "email@test.com");
        FunctionEntity func = new FunctionEntity(user, "x^2", "SQR", "x*x", 0.0, 10.0);

        assertNotNull(func);
        assertEquals("x^2", func.getFunctionName());
        assertEquals("SQR", func.getFunctionType());
        assertEquals("x*x", func.getFunctionExpression());
        assertEquals(0.0, func.getXFrom());
        assertEquals(10.0, func.getXTo());
        assertNotNull(func.getCreatedAt());
        assertSame(user, func.getUser());
    }

    @Test
    void testSetters() {
        FunctionEntity func = new FunctionEntity();
        UserEntity user = new UserEntity();

        func.setFunctionId(1L);
        func.setUser(user);
        func.setFunctionName("test");
        func.setFunctionType("TYPE");
        func.setFunctionExpression("expr");
        func.setXFrom(0.0);
        func.setXTo(5.0);
        LocalDateTime now = LocalDateTime.now();
        func.setCreatedAt(now);
        func.setComputedPoints(new ArrayList<>());

        assertEquals(1L, func.getFunctionId());
        assertSame(user, func.getUser());
        assertEquals("test", func.getFunctionName());
        assertEquals("TYPE", func.getFunctionType());
        assertEquals("expr", func.getFunctionExpression());
        assertEquals(0.0, func.getXFrom());
        assertEquals(5.0, func.getXTo());
        assertEquals(now, func.getCreatedAt());
        assertNotNull(func.getComputedPoints());
    }
}