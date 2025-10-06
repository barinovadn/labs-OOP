package operations;

import functions.Point;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import functions.ArrayTabulatedFunction;
import functions.LinkedListTabulatedFunction;
import functions.TabulatedFunction;
import functions.factory.ArrayTabulatedFunctionFactory;
import functions.factory.LinkedListTabulatedFunctionFactory;
import exceptions.InconsistentFunctionsException;

public class TabulatedFunctionOperationServiceTest {

    @Test
    void testAsPointsWithArrayTabulatedFunction() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};

        TabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);
        Point[] points = TabulatedFunctionOperationService.asPoints(function);

        assertEquals(3, points.length);
        assertEquals(1.0, points[0].x, 1e-9);
        assertEquals(10.0, points[0].y, 1e-9);
        assertEquals(2.0, points[1].x, 1e-9);
        assertEquals(20.0, points[1].y, 1e-9);
        assertEquals(3.0, points[2].x, 1e-9);
        assertEquals(30.0, points[2].y, 1e-9);
    }

    @Test
    void testMultiplyFunctions() {
        TabulatedFunctionOperationService service = new TabulatedFunctionOperationService();

        double[] xValues1 = {1.0, 2.0, 3.0};
        double[] yValues1 = {2.0, 3.0, 4.0};
        double[] xValues2 = {1.0, 2.0, 3.0};
        double[] yValues2 = {5.0, 6.0, 7.0};

        TabulatedFunction func1 = new ArrayTabulatedFunction(xValues1, yValues1);
        TabulatedFunction func2 = new LinkedListTabulatedFunction(xValues2, yValues2);

        TabulatedFunction result = service.multiply(func1, func2);

        assertEquals(3, result.getCount());
        assertEquals(1.0, result.getX(0), 1e-9);
        assertEquals(10.0, result.getY(0), 1e-9);
        assertEquals(2.0, result.getX(1), 1e-9);
        assertEquals(18.0, result.getY(1), 1e-9);
        assertEquals(3.0, result.getX(2), 1e-9);
        assertEquals(28.0, result.getY(2), 1e-9);
    }

    @Test
    void testDivideFunctions() {
        TabulatedFunctionOperationService service = new TabulatedFunctionOperationService();

        double[] xValues1 = {1.0, 2.0, 3.0};
        double[] yValues1 = {10.0, 20.0, 30.0};
        double[] xValues2 = {1.0, 2.0, 3.0};
        double[] yValues2 = {2.0, 5.0, 6.0};

        TabulatedFunction func1 = new ArrayTabulatedFunction(xValues1, yValues1);
        TabulatedFunction func2 = new LinkedListTabulatedFunction(xValues2, yValues2);

        TabulatedFunction result = service.divide(func1, func2);

        assertEquals(3, result.getCount());
        assertEquals(1.0, result.getX(0), 1e-9);
        assertEquals(5.0, result.getY(0), 1e-9);
        assertEquals(2.0, result.getX(1), 1e-9);
        assertEquals(4.0, result.getY(1), 1e-9);
        assertEquals(3.0, result.getX(2), 1e-9);
        assertEquals(5.0, result.getY(2), 1e-9);
    }

    @Test
    void testMultiplyWithDifferentFactories() {
        TabulatedFunctionOperationService service1 = new TabulatedFunctionOperationService(new ArrayTabulatedFunctionFactory());
        TabulatedFunctionOperationService service2 = new TabulatedFunctionOperationService(new LinkedListTabulatedFunctionFactory());

        double[] xValues1 = {1.0, 2.0};
        double[] yValues1 = {3.0, 4.0};
        double[] xValues2 = {1.0, 2.0};
        double[] yValues2 = {2.0, 3.0};

        TabulatedFunction func1 = new ArrayTabulatedFunction(xValues1, yValues1);
        TabulatedFunction func2 = new ArrayTabulatedFunction(xValues2, yValues2);

        TabulatedFunction result1 = service1.multiply(func1, func2);
        TabulatedFunction result2 = service2.multiply(func1, func2);

        assertTrue(result1 instanceof ArrayTabulatedFunction);
        assertTrue(result2 instanceof LinkedListTabulatedFunction);
        assertEquals(6.0, result1.getY(0), 1e-9);
        assertEquals(12.0, result1.getY(1), 1e-9);
        assertEquals(6.0, result2.getY(0), 1e-9);
        assertEquals(12.0, result2.getY(1), 1e-9);
    }

    @Test
    void testInconsistentFunctionsExceptionConstructors() {
        InconsistentFunctionsException exception1 = new InconsistentFunctionsException();
        assertNotNull(exception1);

        InconsistentFunctionsException exception2 = new InconsistentFunctionsException("Test message");
        assertNotNull(exception2);
        assertEquals("Test message", exception2.getMessage());
    }

    @Test
    void testAsPointsWithLinkedListTabulatedFunction() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};

        TabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);
        Point[] points = TabulatedFunctionOperationService.asPoints(function);

        assertEquals(3, points.length);
        assertEquals(1.0, points[0].x, 1e-9);
        assertEquals(10.0, points[0].y, 1e-9);
        assertEquals(2.0, points[1].x, 1e-9);
        assertEquals(20.0, points[1].y, 1e-9);
        assertEquals(3.0, points[2].x, 1e-9);
        assertEquals(30.0, points[2].y, 1e-9);
    }

    @Test
    void testAddSameTypeFunctions() {
        TabulatedFunctionOperationService service = new TabulatedFunctionOperationService();

        double[] xValues1 = {1.0, 2.0, 3.0};
        double[] yValues1 = {10.0, 20.0, 30.0};
        double[] xValues2 = {1.0, 2.0, 3.0};
        double[] yValues2 = {5.0, 10.0, 15.0};

        TabulatedFunction func1 = new ArrayTabulatedFunction(xValues1, yValues1);
        TabulatedFunction func2 = new ArrayTabulatedFunction(xValues2, yValues2);

        TabulatedFunction result = service.add(func1, func2);

        assertEquals(3, result.getCount());
        assertEquals(1.0, result.getX(0), 1e-9);
        assertEquals(15.0, result.getY(0), 1e-9);
        assertEquals(2.0, result.getX(1), 1e-9);
        assertEquals(30.0, result.getY(1), 1e-9);
        assertEquals(3.0, result.getX(2), 1e-9);
        assertEquals(45.0, result.getY(2), 1e-9);
    }

    @Test
    void testSubtractDifferentTypeFunctions() {
        TabulatedFunctionOperationService service = new TabulatedFunctionOperationService();

        double[] xValues1 = {1.0, 2.0, 3.0};
        double[] yValues1 = {20.0, 30.0, 40.0};
        double[] xValues2 = {1.0, 2.0, 3.0};
        double[] yValues2 = {5.0, 10.0, 15.0};

        TabulatedFunction func1 = new ArrayTabulatedFunction(xValues1, yValues1);
        TabulatedFunction func2 = new LinkedListTabulatedFunction(xValues2, yValues2);

        TabulatedFunction result = service.subtract(func1, func2);

        assertEquals(3, result.getCount());
        assertEquals(1.0, result.getX(0), 1e-9);
        assertEquals(15.0, result.getY(0), 1e-9);
        assertEquals(2.0, result.getX(1), 1e-9);
        assertEquals(20.0, result.getY(1), 1e-9);
        assertEquals(3.0, result.getX(2), 1e-9);
        assertEquals(25.0, result.getY(2), 1e-9);
    }

    @Test
    void testAddWithLinkedListFactory() {
        TabulatedFunctionOperationService service = new TabulatedFunctionOperationService(new LinkedListTabulatedFunctionFactory());

        double[] xValues1 = {1.0, 2.0};
        double[] yValues1 = {5.0, 10.0};
        double[] xValues2 = {1.0, 2.0};
        double[] yValues2 = {3.0, 7.0};

        TabulatedFunction func1 = new ArrayTabulatedFunction(xValues1, yValues1);
        TabulatedFunction func2 = new LinkedListTabulatedFunction(xValues2, yValues2);

        TabulatedFunction result = service.add(func1, func2);

        assertTrue(result instanceof LinkedListTabulatedFunction);
        assertEquals(2, result.getCount());
        assertEquals(1.0, result.getX(0), 1e-9);
        assertEquals(8.0, result.getY(0), 1e-9);
        assertEquals(2.0, result.getX(1), 1e-9);
        assertEquals(17.0, result.getY(1), 1e-9);
    }

    @Test
    void testInconsistentFunctionsExceptionDifferentCounts() {
        TabulatedFunctionOperationService service = new TabulatedFunctionOperationService();

        double[] xValues1 = {1.0, 2.0, 3.0};
        double[] yValues1 = {10.0, 20.0, 30.0};
        double[] xValues2 = {1.0, 2.0};
        double[] yValues2 = {5.0, 10.0};

        TabulatedFunction func1 = new ArrayTabulatedFunction(xValues1, yValues1);
        TabulatedFunction func2 = new ArrayTabulatedFunction(xValues2, yValues2);

        assertThrows(InconsistentFunctionsException.class, () -> service.add(func1, func2));
    }

    @Test
    void testInconsistentFunctionsExceptionDifferentXValues() {
        TabulatedFunctionOperationService service = new TabulatedFunctionOperationService();

        double[] xValues1 = {1.0, 2.0, 3.0};
        double[] yValues1 = {10.0, 20.0, 30.0};
        double[] xValues2 = {1.0, 2.5, 3.0};
        double[] yValues2 = {5.0, 10.0, 15.0};

        TabulatedFunction func1 = new ArrayTabulatedFunction(xValues1, yValues1);
        TabulatedFunction func2 = new ArrayTabulatedFunction(xValues2, yValues2);

        assertThrows(InconsistentFunctionsException.class, () -> service.subtract(func1, func2));
    }

    @Test
    void testGetSetFactory() {
        TabulatedFunctionOperationService service = new TabulatedFunctionOperationService();

        assertTrue(service.getFactory() instanceof ArrayTabulatedFunctionFactory);

        service.setFactory(new LinkedListTabulatedFunctionFactory());
        assertTrue(service.getFactory() instanceof LinkedListTabulatedFunctionFactory);
    }

}