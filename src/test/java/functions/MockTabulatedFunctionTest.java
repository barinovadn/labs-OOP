package functions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MockTabulatedFunctionTest {

    @Test
    void testInterpolate() {
        MockTabulatedFunction mock = new MockTabulatedFunction();

        double result = mock.interpolate(2.0, 1.0, 3.0, 2.0, 6.0);
        assertEquals(4.0, result, 0.001);
    }

    @Test
    void testApplyInterpolation() {
        MockTabulatedFunction mock = new MockTabulatedFunction();

        double result = mock.apply(2.0);
        assertEquals(4.0, result, 0.001);
    }

    @Test
    void testApplyExactX() {
        MockTabulatedFunction mock = new MockTabulatedFunction();

        assertEquals(2.0, mock.apply(1.0));
        assertEquals(6.0, mock.apply(3.0));
    }

    @Test
    void testFloorIndexOfX() {
        MockTabulatedFunction mock = new MockTabulatedFunction();

        assertEquals(0, mock.floorIndexOfX(0.5));
        assertEquals(0, mock.floorIndexOfX(1.0));
        assertEquals(0, mock.floorIndexOfX(2.0));
        assertEquals(1, mock.floorIndexOfX(3.0));
        assertEquals(2, mock.floorIndexOfX(4.0));
    }

    @Test
    void testGetters() {
        MockTabulatedFunction mock = new MockTabulatedFunction();

        assertEquals(2, mock.getCount());
        assertEquals(1.0, mock.getX(0));
        assertEquals(3.0, mock.getX(1));
        assertEquals(2.0, mock.getY(0));
        assertEquals(6.0, mock.getY(1));
    }

    @Test
    void testIndexOfY() {
        MockTabulatedFunction mock = new MockTabulatedFunction();

        assertEquals(0, mock.indexOfY(2.0));
        assertEquals(1, mock.indexOfY(6.0));
        assertEquals(-1, mock.indexOfY(5.0));
    }

    @Test
    void testSetY() {
        MockTabulatedFunction mock = new MockTabulatedFunction();

        mock.setY(0, 10.0);
        assertEquals(2.0, mock.getY(0));
    }

    @Test
    void testApplyExtrapolation() {
        MockTabulatedFunction mock = new MockTabulatedFunction();

        double left = mock.apply(0.0);
        double right = mock.apply(4.0);

        assertTrue(left < 2.0);
        assertTrue(right > 6.0);
    }
}