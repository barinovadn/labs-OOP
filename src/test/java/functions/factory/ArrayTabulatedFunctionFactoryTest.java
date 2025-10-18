package functions.factory;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import functions.ArrayTabulatedFunction;
import functions.TabulatedFunction;
import functions.StrictTabulatedFunction;
import functions.UnmodifiableTabulatedFunction;

public class ArrayTabulatedFunctionFactoryTest {

    private final TabulatedFunctionFactory factory = new ArrayTabulatedFunctionFactory();
    private final double[] xValues = {1.0, 2.0, 3.0};
    private final double[] yValues = {10.0, 20.0, 30.0};

    @Test
    void testCreate() {
        TabulatedFunction function = factory.create(xValues, yValues);

        assertTrue(function instanceof ArrayTabulatedFunction);
        assertEquals(3, function.getCount());
        assertArrayEquals(new double[]{1.0, 2.0, 3.0},
                new double[]{function.getX(0), function.getX(1), function.getX(2)}, 1e-9);
        assertArrayEquals(new double[]{10.0, 20.0, 30.0},
                new double[]{function.getY(0), function.getY(1), function.getY(2)}, 1e-9);
    }

    @Test
    void testCreateStrict() {
        TabulatedFunction function = factory.createStrict(xValues, yValues);

        assertTrue(function instanceof StrictTabulatedFunction);
        assertEquals(3, function.getCount());
        assertEquals(20.0, function.apply(2.0), 1e-9);
        assertThrows(UnsupportedOperationException.class, () -> function.apply(1.5));
    }

    @Test
    void testCreateUnmodifiable() {
        TabulatedFunction function = factory.createUnmodifiable(xValues, yValues);

        assertTrue(function instanceof UnmodifiableTabulatedFunction);
        assertEquals(3, function.getCount());
        assertEquals(20.0, function.apply(2.0), 1e-9);
        assertThrows(UnsupportedOperationException.class, () -> function.setY(0, 15.0));
    }

    @Test
    void testCreateStrictUnmodifiable() {
        TabulatedFunction function = factory.createStrictUnmodifiable(xValues, yValues);

        assertEquals(3, function.getCount());
        assertEquals(20.0, function.apply(2.0), 1e-9);

        assertThrows(UnsupportedOperationException.class, () -> function.apply(1.5));
        assertThrows(UnsupportedOperationException.class, () -> function.setY(0, 15.0));
    }
}