package manual.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EntityFullTest {

    @Test
    void testUserEntityAllCases() {
        UserEntity user = new UserEntity();
        LocalDateTime now = LocalDateTime.now();
        user.setUserId(1L);
        user.setUsername("testuser");
        user.setPassword("password");
        user.setEmail("test@test.com");
        user.setCreatedAt(now);
        assertEquals(1L, user.getUserId());
        assertEquals("testuser", user.getUsername());
        assertEquals("password", user.getPassword());
        assertEquals("test@test.com", user.getEmail());
        assertEquals(now, user.getCreatedAt());

        UserEntity user2 = new UserEntity("user", "pass", "email@test.com");
        assertEquals("user", user2.getUsername());
        assertEquals("pass", user2.getPassword());
        assertEquals("email@test.com", user2.getEmail());
        assertNotNull(user2.getCreatedAt());

        UserEntity nullUser = new UserEntity();
        nullUser.setUserId(null);
        nullUser.setUsername(null);
        nullUser.setPassword(null);
        nullUser.setEmail(null);
        nullUser.setCreatedAt(null);
        assertNull(nullUser.getUserId());
        assertNull(nullUser.getUsername());
        assertNull(nullUser.getPassword());
        assertNull(nullUser.getEmail());
        assertNull(nullUser.getCreatedAt());
    }

    @Test
    void testFunctionEntityAllCases() {
        UserEntity user = new UserEntity("user", "pass", "email@test.com");
        user.setUserId(1L);
        FunctionEntity func = new FunctionEntity();
        LocalDateTime now = LocalDateTime.now();
        func.setFunctionId(1L);
        func.setUser(user);
        func.setFunctionName("Test Function");
        func.setFunctionType("LINEAR");
        func.setFunctionExpression("x");
        func.setXFrom(0.0);
        func.setXTo(10.0);
        func.setCreatedAt(now);
        assertEquals(1L, func.getFunctionId());
        assertEquals(user, func.getUser());
        assertEquals("Test Function", func.getFunctionName());
        assertEquals("LINEAR", func.getFunctionType());
        assertEquals("x", func.getFunctionExpression());
        assertEquals(0.0, func.getXFrom());
        assertEquals(10.0, func.getXTo());
        assertEquals(now, func.getCreatedAt());

        FunctionEntity func2 = new FunctionEntity(user, "MyFunc", "POLYNOMIAL", "x*x", -5.0, 5.0);
        assertEquals(user, func2.getUser());
        assertEquals("MyFunc", func2.getFunctionName());
        assertNotNull(func2.getCreatedAt());

        FunctionEntity nullFunc = new FunctionEntity();
        nullFunc.setFunctionId(null);
        nullFunc.setUser(null);
        nullFunc.setFunctionName(null);
        nullFunc.setFunctionType(null);
        nullFunc.setFunctionExpression(null);
        nullFunc.setXFrom(null);
        nullFunc.setXTo(null);
        nullFunc.setCreatedAt(null);
        assertNull(nullFunc.getFunctionId());
        assertNull(nullFunc.getUser());
    }

    @Test
    void testPointEntityAllCases() {
        UserEntity user = new UserEntity("user", "pass", "email@test.com");
        user.setUserId(1L);
        FunctionEntity func = new FunctionEntity(user, "Func", "LINEAR", "x", 0.0, 10.0);
        func.setFunctionId(1L);
        PointEntity point = new PointEntity();
        LocalDateTime now = LocalDateTime.now();
        point.setPointId(1L);
        point.setFunction(func);
        point.setXValue(5.0);
        point.setYValue(5.0);
        point.setComputedAt(now);
        assertEquals(1L, point.getPointId());
        assertEquals(func, point.getFunction());
        assertEquals(5.0, point.getXValue());
        assertEquals(5.0, point.getYValue());
        assertEquals(now, point.getComputedAt());

        PointEntity point2 = new PointEntity(func, 3.0, 9.0);
        assertEquals(func, point2.getFunction());
        assertEquals(3.0, point2.getXValue());
        assertEquals(9.0, point2.getYValue());
        assertNotNull(point2.getComputedAt());

        PointEntity negPoint = new PointEntity(func, -3.0, -9.0);
        assertEquals(-3.0, negPoint.getXValue());
        assertEquals(-9.0, negPoint.getYValue());
    }

    @Test
    void testCompositeFunctionEntityAllCases() {
        UserEntity user = new UserEntity("user", "pass", "email@test.com");
        user.setUserId(1L);
        FunctionEntity first = new FunctionEntity(user, "First", "LINEAR", "x", 0.0, 10.0);
        first.setFunctionId(1L);
        FunctionEntity second = new FunctionEntity(user, "Second", "LINEAR", "2*x", 0.0, 10.0);
        second.setFunctionId(2L);
        CompositeFunctionEntity comp = new CompositeFunctionEntity();
        LocalDateTime now = LocalDateTime.now();
        comp.setCompositeId(1L);
        comp.setUser(user);
        comp.setCompositeName("MyComposite");
        comp.setFirstFunction(first);
        comp.setSecondFunction(second);
        comp.setCreatedAt(now);
        assertEquals(1L, comp.getCompositeId());
        assertEquals(user, comp.getUser());
        assertEquals("MyComposite", comp.getCompositeName());
        assertEquals(first, comp.getFirstFunction());
        assertEquals(second, comp.getSecondFunction());
        assertEquals(now, comp.getCreatedAt());

        CompositeFunctionEntity comp2 = new CompositeFunctionEntity(user, "Composite", first, second);
        assertEquals(user, comp2.getUser());
        assertEquals("Composite", comp2.getCompositeName());
        assertNotNull(comp2.getCreatedAt());

        CompositeFunctionEntity nullComp = new CompositeFunctionEntity();
        nullComp.setCompositeId(null);
        nullComp.setUser(null);
        nullComp.setCompositeName(null);
        nullComp.setFirstFunction(null);
        nullComp.setSecondFunction(null);
        nullComp.setCreatedAt(null);
        assertNull(nullComp.getCompositeId());
        assertNull(nullComp.getUser());
    }
}

