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
    void testApplyExtrapolation() {
        MockTabulatedFunction mock = new MockTabulatedFunction();

        double left = mock.apply(0.0);
        double right = mock.apply(4.0);

        assertTrue(left < 2.0);
        assertTrue(right > 6.0);
    }
}