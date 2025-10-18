package functions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ConstantFunctionTest {

    @Test
    void testConstantFunction() {
        ConstantFunction func = new ConstantFunction(5.0);
        assertEquals(5.0, func.apply(0.0));
        assertEquals(5.0, func.apply(10.0));
        assertEquals(5.0, func.apply(-5.0));
        assertEquals(5.0, func.getConstant());
    }

    @Test
    void testZeroFunction() {
        ZeroFunction zero = new ZeroFunction();
        assertEquals(0.0, zero.apply(0.0));
        assertEquals(0.0, zero.apply(100.0));
        assertEquals(0.0, zero.apply(-50.0));
        assertEquals(0.0, zero.getConstant());
    }

    @Test
    void testUnitFunction() {
        UnitFunction unit = new UnitFunction();
        assertEquals(1.0, unit.apply(0.0));
        assertEquals(1.0, unit.apply(100.0));
        assertEquals(1.0, unit.apply(-50.0));
        assertEquals(1.0, unit.getConstant());
    }

    @Test
    void testConstantFunctionWithNegative() {
        ConstantFunction func = new ConstantFunction(-3.5);
        assertEquals(-3.5, func.apply(0.0));
        assertEquals(-3.5, func.apply(10.0));
        assertEquals(-3.5, func.apply(-5.0));
        assertEquals(-3.5, func.getConstant());
    }

    @Test
    void testConstantFunctionWithZero() {
        ConstantFunction func = new ConstantFunction(0.0);
        assertEquals(0.0, func.apply(0.0));
        assertEquals(0.0, func.apply(10.0));
        assertEquals(0.0, func.apply(-5.0));
        assertEquals(0.0, func.getConstant());
    }
}