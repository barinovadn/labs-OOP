package functions.factory;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import functions.ArrayTabulatedFunction;
import functions.TabulatedFunction;

public class ArrayTabulatedFunctionFactoryTest {

    @Test
    void testCreateArrayTabulatedFunction() {
        TabulatedFunctionFactory factory = new ArrayTabulatedFunctionFactory();
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};

        TabulatedFunction function = factory.create(xValues, yValues);

        assertTrue(function instanceof ArrayTabulatedFunction);
        assertEquals(3, function.getCount());
        assertEquals(1.0, function.getX(0), 1e-9);
        assertEquals(10.0, function.getY(0), 1e-9);
        assertEquals(2.0, function.getX(1), 1e-9);
        assertEquals(20.0, function.getY(1), 1e-9);
        assertEquals(3.0, function.getX(2), 1e-9);
        assertEquals(30.0, function.getY(2), 1e-9);
    }
}