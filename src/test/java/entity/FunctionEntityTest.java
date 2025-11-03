package entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FunctionEntityTest {

    @Test
    void testAllGettersAndSetters() {
        FunctionEntity function = new FunctionEntity();
        UserEntity user = new UserEntity();

        function.setFunctionId(1L);
        function.setUser(user);
        function.setFunctionName("test function");
        function.setFunctionType("LINEAR");
        function.setFunctionExpression("2*x");
        function.setXFrom(0.0);
        function.setXTo(10.0);
        LocalDateTime now = LocalDateTime.now();
        function.setCreatedAt(now);

        List<PointEntity> points = new ArrayList<>();
        function.setComputedPoints(points);

        assertEquals(1L, function.getFunctionId());
        assertSame(user, function.getUser());
        assertEquals("test function", function.getFunctionName());
        assertEquals("LINEAR", function.getFunctionType());
        assertEquals("2*x", function.getFunctionExpression());
        assertEquals(0.0, function.getXFrom());
        assertEquals(10.0, function.getXTo());
        assertEquals(now, function.getCreatedAt());
        assertEquals(points, function.getComputedPoints());
    }

    @Test
    void testConstructor() {
        UserEntity user = new UserEntity("user", "pass", "email@test.com");
        FunctionEntity function = new FunctionEntity(user, "x^2", "SQR", "x*x", 0.0, 10.0);

        assertSame(user, function.getUser());
        assertEquals("x^2", function.getFunctionName());
        assertEquals("SQR", function.getFunctionType());
        assertEquals("x*x", function.getFunctionExpression());
        assertEquals(0.0, function.getXFrom());
        assertEquals(10.0, function.getXTo());
        assertNotNull(function.getCreatedAt());
    }
}