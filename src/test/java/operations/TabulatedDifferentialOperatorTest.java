package operations;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import functions.ArrayTabulatedFunction;
import functions.LinkedListTabulatedFunction;
import functions.TabulatedFunction;
import functions.factory.ArrayTabulatedFunctionFactory;
import functions.factory.LinkedListTabulatedFunctionFactory;
import functions.factory.TabulatedFunctionFactory;

public class TabulatedDifferentialOperatorTest {

    @Test
    void testDeriveWithArrayFactory() {
        TabulatedFunctionFactory factory = new ArrayTabulatedFunctionFactory();
        TabulatedDifferentialOperator operator = new TabulatedDifferentialOperator(factory);

        double[] xValues = {1.0, 2.0, 3.0, 4.0};
        double[] yValues = {1.0, 4.0, 9.0, 16.0}; // f(x) = x^2

        TabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);
        TabulatedFunction derivative = operator.derive(function);

        assertEquals(4, derivative.getCount());
        assertEquals(1.0, derivative.getX(0), 1e-9);
        assertEquals(2.0, derivative.getX(1), 1e-9);
        assertEquals(3.0, derivative.getX(2), 1e-9);
        assertEquals(4.0, derivative.getX(3), 1e-9);

        assertEquals(3.0, derivative.getY(0), 1e-9); // (4-1)/(2-1) = 3
        assertEquals(5.0, derivative.getY(1), 1e-9); // (9-4)/(3-2) = 5
        assertEquals(7.0, derivative.getY(2), 1e-9); // (16-9)/(4-3) = 7
        assertEquals(7.0, derivative.getY(3), 1e-9); // last point same as previous
    }

    @Test
    void testDeriveWithLinkedListFactory() {
        TabulatedFunctionFactory factory = new LinkedListTabulatedFunctionFactory();
        TabulatedDifferentialOperator operator = new TabulatedDifferentialOperator(factory);

        double[] xValues = {0.0, 1.0, 2.0, 3.0};
        double[] yValues = {0.0, 1.0, 2.0, 3.0}; // f(x) = x

        TabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);
        TabulatedFunction derivative = operator.derive(function);

        assertEquals(4, derivative.getCount());
        assertEquals(0.0, derivative.getX(0), 1e-9);
        assertEquals(1.0, derivative.getX(1), 1e-9);
        assertEquals(2.0, derivative.getX(2), 1e-9);
        assertEquals(3.0, derivative.getX(3), 1e-9);

        assertEquals(1.0, derivative.getY(0), 1e-9); // (1-0)/(1-0) = 1
        assertEquals(1.0, derivative.getY(1), 1e-9); // (2-1)/(2-1) = 1
        assertEquals(1.0, derivative.getY(2), 1e-9); // (3-2)/(3-2) = 1
        assertEquals(1.0, derivative.getY(3), 1e-9); // last point same as previous
    }

    @Test
    void testDeriveWithDefaultConstructor() {
        TabulatedDifferentialOperator operator = new TabulatedDifferentialOperator();

        double[] xValues = {1.0, 2.0};
        double[] yValues = {2.0, 4.0}; // f(x) = 2x

        TabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);
        TabulatedFunction derivative = operator.derive(function);

        assertEquals(2, derivative.getCount());
        assertEquals(1.0, derivative.getX(0), 1e-9);
        assertEquals(2.0, derivative.getX(1), 1e-9);

        assertEquals(2.0, derivative.getY(0), 1e-9); // (4-2)/(2-1) = 2
        assertEquals(2.0, derivative.getY(1), 1e-9); // last point same as previous
    }

    @Test
    void testGetSetFactory() {
        TabulatedDifferentialOperator operator = new TabulatedDifferentialOperator();

        assertTrue(operator.getFactory() instanceof ArrayTabulatedFunctionFactory);

        TabulatedFunctionFactory newFactory = new LinkedListTabulatedFunctionFactory();
        operator.setFactory(newFactory);

        assertTrue(operator.getFactory() instanceof LinkedListTabulatedFunctionFactory);
    }
}   