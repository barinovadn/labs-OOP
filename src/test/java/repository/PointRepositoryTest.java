package repository;

import entity.FunctionEntity;
import entity.PointEntity;
import entity.UserEntity;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class PointRepositoryTest {

    @Test
    void testPointCreation() {
        UserEntity user = new UserEntity("user", "pass", "email@test.com");
        FunctionEntity func = new FunctionEntity(user, "func", "TYPE", "x", 0.0, 10.0);
        PointEntity point = new PointEntity(func, 2.0, 4.0);

        assertNotNull(point);
        assertEquals(2.0, point.getXValue());
        assertEquals(4.0, point.getYValue());
        assertSame(func, point.getFunction());
        assertNotNull(point.getComputedAt());
    }

    @Test
    void testSetters() {
        PointEntity point = new PointEntity();
        FunctionEntity func = new FunctionEntity();

        point.setPointId(1L);
        point.setFunction(func);
        point.setXValue(3.0);
        point.setYValue(9.0);
        LocalDateTime now = LocalDateTime.now();
        point.setComputedAt(now);

        assertEquals(1L, point.getPointId());
        assertSame(func, point.getFunction());
        assertEquals(3.0, point.getXValue());
        assertEquals(9.0, point.getYValue());
        assertEquals(now, point.getComputedAt());
    }
}