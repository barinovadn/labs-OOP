package operations;

import functions.ArrayTabulatedFunction;
import functions.LinkedListTabulatedFunction;
import functions.TabulatedFunction;
import functions.factory.ArrayTabulatedFunctionFactory;
import functions.factory.LinkedListTabulatedFunctionFactory;
import concurrent.SynchronizedTabulatedFunction;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TabulatedDifferentialOperatorTest {

    @Test
    void testDeriveSynchronouslyWithRegularFunction() {
        double[] xValues = {1.0, 2.0, 3.0, 4.0};
        double[] yValues = {1.0, 4.0, 9.0, 16.0};
        TabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);
        TabulatedDifferentialOperator operator = new TabulatedDifferentialOperator();

        TabulatedFunction derivative = operator.deriveSynchronously(function);

        assertEquals(4, derivative.getCount());
        assertEquals(3.0, derivative.getY(0), 1e-9);
        assertEquals(5.0, derivative.getY(1), 1e-9);
        assertEquals(7.0, derivative.getY(2), 1e-9);
        assertEquals(7.0, derivative.getY(3), 1e-9);
    }

    @Test
    void testDeriveSynchronouslyWithSyncFunction() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {2.0, 4.0, 6.0};
        TabulatedFunction baseFunction = new LinkedListTabulatedFunction(xValues, yValues);
        SynchronizedTabulatedFunction syncFunction = new SynchronizedTabulatedFunction(baseFunction);
        TabulatedDifferentialOperator operator = new TabulatedDifferentialOperator();

        TabulatedFunction derivative = operator.deriveSynchronously(syncFunction);

        assertEquals(3, derivative.getCount());
        assertEquals(2.0, derivative.getY(0), 1e-9);
        assertEquals(2.0, derivative.getY(1), 1e-9);
        assertEquals(2.0, derivative.getY(2), 1e-9);
    }

    @Test
    void testDeriveSynchronouslySameResultAsDerive() {
        double[] xValues = {0.0, 1.0, 2.0};
        double[] yValues = {0.0, 1.0, 4.0};
        TabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);
        TabulatedDifferentialOperator operator = new TabulatedDifferentialOperator();

        TabulatedFunction derivative1 = operator.derive(function);
        TabulatedFunction derivative2 = operator.deriveSynchronously(function);

        assertEquals(derivative1.getCount(), derivative2.getCount());
        for (int i = 0; i < derivative1.getCount(); i++) {
            assertEquals(derivative1.getX(i), derivative2.getX(i), 1e-9);
            assertEquals(derivative1.getY(i), derivative2.getY(i), 1e-9);
        }
    }

    @Test
    void testDeriveSynchronouslyWithFactoryMethods() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {1.0, 2.0, 3.0};
        TabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        ArrayTabulatedFunctionFactory arrayFactory = new ArrayTabulatedFunctionFactory();
        TabulatedDifferentialOperator operator = new TabulatedDifferentialOperator(arrayFactory);

        assertEquals(arrayFactory, operator.getFactory());

        LinkedListTabulatedFunctionFactory listFactory = new LinkedListTabulatedFunctionFactory();
        operator.setFactory(listFactory);
        assertEquals(listFactory, operator.getFactory());

        TabulatedFunction derivative = operator.deriveSynchronously(function);
        assertNotNull(derivative);
        assertEquals(3, derivative.getCount());
    }
}