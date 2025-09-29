package functions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BSplineTest {

    @Test
    void testBSplineDegreeA() {
        double[] knots = {0, 1, 2, 3};
        assertEquals(1.0, BSpline.apply(0, 0, 0.5, knots));
        assertEquals(0.0, BSpline.apply(0, 0, 1.0, knots));
    }

    @Test
    void testBSplineDegreeB() {
        double[] knots = {0, 0, 1, 2, 2};
        assertEquals(0.5, BSpline.apply(1, 1, 0.5, knots), 0.001);
        assertEquals(0.0, BSpline.apply(0, 1, 1.5, knots));
    }

    @Test
    void testBSplineDegreeC() {
        double[] knots = {0, 0, 0, 1, 2, 3, 3, 3};
        double result = BSpline.apply(2, 2, 1.5, knots);
        assertTrue(result > 0);
    }

    @Test
    void testBSplineWithZeroDenominator() {
        double[] knots = {0, 1, 1, 2};
        double result = BSpline.apply(0, 1, 0.5, knots);
        assertTrue(result >= 0);
    }
}