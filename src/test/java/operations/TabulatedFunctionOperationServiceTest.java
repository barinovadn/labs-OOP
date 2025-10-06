package operations;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import functions.ArrayTabulatedFunction;
import functions.LinkedListTabulatedFunction;
import functions.Point;
import functions.TabulatedFunction;

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

}