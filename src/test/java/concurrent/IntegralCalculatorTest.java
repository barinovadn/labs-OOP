package concurrent;

import functions.ArrayTabulatedFunction;
import functions.TabulatedFunction;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class IntegralCalculatorTest {

    @Test
    void testIntegralLinearFunction() throws Exception {
        double[] xValues = {0.0, 1.0, 2.0, 3.0};
        double[] yValues = {0.0, 1.0, 2.0, 3.0};
        TabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        double result = IntegralCalculator.calculate(function, 2);
        assertEquals(4.5, result, 0.1);
    }

    @Test
    void testIntegralConstantFunction() throws Exception {
        double[] xValues = {0.0, 1.0, 2.0, 3.0};
        double[] yValues = {2.0, 2.0, 2.0, 2.0};
        TabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        double result = IntegralCalculator.calculate(function, 4);
        assertEquals(6.0, result, 0.1);
    }

    @Test
    void testIntegralSingleThread() throws Exception {
        double[] xValues = {0.0, 1.0};
        double[] yValues = {1.0, 1.0};
        TabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        double result = IntegralCalculator.calculate(function, 1);
        assertEquals(1.0, result, 0.1);
    }

    @Test
    void testThreadDistribution() throws Exception {
        double[] xValues = {0.0, 1.0, 2.0, 3.0, 4.0, 5.0};
        double[] yValues = {0.0, 1.0, 2.0, 3.0, 4.0, 5.0};
        TabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        double result = IntegralCalculator.calculate(function, 3);
        assertEquals(12.5, result, 0.1);
    }

    @Test
    void testUnevenThreadDistribution() throws Exception {
        double[] xValues = {0.0, 1.0, 2.0, 3.0, 4.0};
        double[] yValues = {1.0, 1.0, 1.0, 1.0, 1.0};
        TabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        double result = IntegralCalculator.calculate(function, 3);
        assertEquals(4.0, result, 0.1);
    }

    @Test
    void testClassInstantiation() {
        IntegralCalculator calculator = new IntegralCalculator();
        assertNotNull(calculator);
    }
}