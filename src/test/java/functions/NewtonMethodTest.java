package functions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NewtonMethodTest {

    @Test
    void testSquareRoot() {
        double result = NewtonMethod.apply(
                x -> x * x - 2,
                x -> 2 * x,
                1.0, 1e-10, 100
        );
        assertEquals(1.414213562, result, 1e-6);
    }

    @Test
    void testLinearEquation() {
        double result = NewtonMethod.apply(
                x -> 2 * x - 4,
                x -> 2.0,
                0.0, 1e-10, 100
        );
        assertEquals(2.0, result, 1e-9);
    }

    @Test
    void testCosine() {
        double result = NewtonMethod.apply(
                Math::cos,
                x -> -Math.sin(x),
                1.0, 1e-10, 100
        );
        assertEquals(Math.PI/2, result, 0.001);
    }

    @Test
    void testCubicEquation() {
        double result = NewtonMethod.apply(
                x -> x * x * x - 8,
                x -> 3 * x * x,
                3.0, 1e-10, 100
        );
        assertEquals(2.0, result, 1e-6);
    }

}