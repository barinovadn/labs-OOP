package manual.dto;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class CoverageTest {

    @Test
    void testPointDtoFullCoverage() {
        PointDto point = new PointDto();

        point.setPointId(1L);
        point.setFunctionId(2L);
        point.setXValue(3.0);
        point.setYValue(4.0);
        LocalDateTime now = LocalDateTime.now();
        point.setComputedAt(now);

        assertEquals(1L, point.getPointId());
        assertEquals(2L, point.getFunctionId());
        assertEquals(3.0, point.getXValue());
        assertEquals(4.0, point.getYValue());
        assertEquals(now, point.getComputedAt());

        PointDto point2 = new PointDto(5L, 6L, 7.0, 8.0, now);
        assertEquals(5L, point2.getPointId());
        assertEquals(6L, point2.getFunctionId());
        assertEquals(7.0, point2.getXValue());
        assertEquals(8.0, point2.getYValue());
        assertEquals(now, point2.getComputedAt());
    }

    @Test
    void testFunctionDtoFullCoverage() {
        FunctionDto function = new FunctionDto();

        function.setFunctionId(1L);
        function.setUserId(2L);
        function.setFunctionName("test");
        function.setFunctionType("TYPE");
        function.setFunctionExpression("x*x");
        function.setXFrom(0.0);
        function.setXTo(10.0);
        LocalDateTime now = LocalDateTime.now();
        function.setCreatedAt(now);

        assertEquals(1L, function.getFunctionId());
        assertEquals(2L, function.getUserId());
        assertEquals("test", function.getFunctionName());
        assertEquals("TYPE", function.getFunctionType());
        assertEquals("x*x", function.getFunctionExpression());
        assertEquals(0.0, function.getXFrom());
        assertEquals(10.0, function.getXTo());
        assertEquals(now, function.getCreatedAt());
    }

    @Test
    void testCompositeFunctionDtoFullCoverage() {
        CompositeFunctionDto composite = new CompositeFunctionDto();

        composite.setCompositeId(1L);
        composite.setUserId(2L);
        composite.setCompositeName("composite");
        composite.setFirstFunctionId(3L);
        composite.setSecondFunctionId(4L);
        LocalDateTime now = LocalDateTime.now();
        composite.setCreatedAt(now);

        assertEquals(1L, composite.getCompositeId());
        assertEquals(2L, composite.getUserId());
        assertEquals("composite", composite.getCompositeName());
        assertEquals(3L, composite.getFirstFunctionId());
        assertEquals(4L, composite.getSecondFunctionId());
        assertEquals(now, composite.getCreatedAt());
    }

    @Test
    void testUserDtoFullCoverage() {
        UserDto user = new UserDto();

        user.setUserId(1L);
        user.setUsername("user");
        user.setPassword("pass");
        user.setEmail("email@test.com");
        LocalDateTime now = LocalDateTime.now();
        user.setCreatedAt(now);

        assertEquals(1L, user.getUserId());
        assertEquals("user", user.getUsername());
        assertEquals("pass", user.getPassword());
        assertEquals("email@test.com", user.getEmail());
        assertEquals(now, user.getCreatedAt());
    }
}