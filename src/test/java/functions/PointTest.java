package functions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PointTest {

    @Test
    void testPointCreation() {
        Point point = new Point(2.5, 3.7);

        assertEquals(2.5, point.x, 0.001);
        assertEquals(3.7, point.y, 0.001);
    }

}