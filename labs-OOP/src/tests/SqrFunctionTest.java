package tests;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import functions.MathFunction;
import functions.SqrFunction;

class SqrFunctionTest {
    @Test
    void testApply() {
        MathFunction sqr = new SqrFunction();
        assertEquals(0.0, sqr.apply(0.0));
        assertEquals(25.0, sqr.apply(5.0));
        assertEquals(12.25, sqr.apply(-3.5));
    }
}