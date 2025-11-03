package entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;

public class PointEntityTest {

    @Test
    void testAllGettersAndSetters() {
        PointEntity point = new PointEntity();
        FunctionEntity function = new FunctionEntity();

        point.setPointId(1L);
        point.setFunction(function);
        point.setXValue(3.0);
        point.setYValue(9.0);
        LocalDateTime now = LocalDateTime.now();
        point.setComputedAt(now);

        assertEquals(1L, point.getPointId());
        assertSame(function, point.getFunction());
        assertEquals(3.0, point.getXValue());
        assertEquals(9.0, point.getYValue());
        assertEquals(now, point.getComputedAt());
    }

    @Test
    void testConstructor() {
        UserEntity user = new UserEntity("user", "pass", "email@test.com");
        FunctionEntity function = new FunctionEntity(user, "x^2", "SQR", "x*x", 0.0, 10.0);
        PointEntity point = new PointEntity(function, 2.0, 4.0);

        assertSame(function, point.getFunction());
        assertEquals(2.0, point.getXValue());
        assertEquals(4.0, point.getYValue());
        assertNotNull(point.getComputedAt());
    }
}