package concurrent;

import functions.ArrayTabulatedFunction;
import functions.LinkedListTabulatedFunction;
import functions.Point;
import functions.TabulatedFunction;
import org.junit.jupiter.api.Test;
import java.util.Iterator;
import static org.junit.jupiter.api.Assertions.*;

/**
 * ВНИМАНИЕ: Тесты требуются по заданию для проверки в однопоточной среде
 */
public class SynchronizedTabulatedFunctionTest {

    @Test
    void testGetCount() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        TabulatedFunction baseFunction = new ArrayTabulatedFunction(xValues, yValues);
        SynchronizedTabulatedFunction syncFunction = new SynchronizedTabulatedFunction(baseFunction);

        assertEquals(3, syncFunction.getCount());
    }

    @Test
    void testGetX() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        TabulatedFunction baseFunction = new LinkedListTabulatedFunction(xValues, yValues);
        SynchronizedTabulatedFunction syncFunction = new SynchronizedTabulatedFunction(baseFunction);

        assertEquals(1.0, syncFunction.getX(0), 1e-9);
        assertEquals(2.0, syncFunction.getX(1), 1e-9);
        assertEquals(3.0, syncFunction.getX(2), 1e-9);
    }

    @Test
    void testGetY() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        TabulatedFunction baseFunction = new ArrayTabulatedFunction(xValues, yValues);
        SynchronizedTabulatedFunction syncFunction = new SynchronizedTabulatedFunction(baseFunction);

        assertEquals(10.0, syncFunction.getY(0), 1e-9);
        assertEquals(20.0, syncFunction.getY(1), 1e-9);
        assertEquals(30.0, syncFunction.getY(2), 1e-9);
    }

    @Test
    void testSetY() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        TabulatedFunction baseFunction = new LinkedListTabulatedFunction(xValues, yValues);
        SynchronizedTabulatedFunction syncFunction = new SynchronizedTabulatedFunction(baseFunction);

        syncFunction.setY(1, 25.0);
        assertEquals(25.0, syncFunction.getY(1), 1e-9);
        assertEquals(25.0, baseFunction.getY(1), 1e-9);
    }

    @Test
    void testIndexOfX() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        TabulatedFunction baseFunction = new ArrayTabulatedFunction(xValues, yValues);
        SynchronizedTabulatedFunction syncFunction = new SynchronizedTabulatedFunction(baseFunction);

        assertEquals(0, syncFunction.indexOfX(1.0));
        assertEquals(1, syncFunction.indexOfX(2.0));
        assertEquals(-1, syncFunction.indexOfX(4.0));
    }

    @Test
    void testIndexOfY() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        TabulatedFunction baseFunction = new LinkedListTabulatedFunction(xValues, yValues);
        SynchronizedTabulatedFunction syncFunction = new SynchronizedTabulatedFunction(baseFunction);

        assertEquals(0, syncFunction.indexOfY(10.0));
        assertEquals(1, syncFunction.indexOfY(20.0));
        assertEquals(-1, syncFunction.indexOfY(40.0));
    }

    @Test
    void testLeftBound() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        TabulatedFunction baseFunction = new ArrayTabulatedFunction(xValues, yValues);
        SynchronizedTabulatedFunction syncFunction = new SynchronizedTabulatedFunction(baseFunction);

        assertEquals(1.0, syncFunction.leftBound(), 1e-9);
    }

    @Test
    void testRightBound() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        TabulatedFunction baseFunction = new LinkedListTabulatedFunction(xValues, yValues);
        SynchronizedTabulatedFunction syncFunction = new SynchronizedTabulatedFunction(baseFunction);

        assertEquals(3.0, syncFunction.rightBound(), 1e-9);
    }

    @Test
    void testApply() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        TabulatedFunction baseFunction = new ArrayTabulatedFunction(xValues, yValues);
        SynchronizedTabulatedFunction syncFunction = new SynchronizedTabulatedFunction(baseFunction);

        assertEquals(10.0, syncFunction.apply(1.0), 1e-9);
        assertEquals(20.0, syncFunction.apply(2.0), 1e-9);
        assertEquals(15.0, syncFunction.apply(1.5), 1e-9);
    }

    @Test
    void testIterator() {
        double[] xValues = {1.0, 2.0};
        double[] yValues = {10.0, 20.0};
        TabulatedFunction baseFunction = new LinkedListTabulatedFunction(xValues, yValues);
        SynchronizedTabulatedFunction syncFunction = new SynchronizedTabulatedFunction(baseFunction);

        Iterator<Point> iterator = syncFunction.iterator();
        assertTrue(iterator.hasNext());

        Point point1 = iterator.next();
        assertEquals(1.0, point1.x, 1e-9);
        assertEquals(10.0, point1.y, 1e-9);

        Point point2 = iterator.next();
        assertEquals(2.0, point2.x, 1e-9);
        assertEquals(20.0, point2.y, 1e-9);

        assertFalse(iterator.hasNext());
    }

    @Test
    void testWithDifferentBaseFunctions() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};

        TabulatedFunction arrayBase = new ArrayTabulatedFunction(xValues, yValues);
        TabulatedFunction listBase = new LinkedListTabulatedFunction(xValues, yValues);

        SynchronizedTabulatedFunction syncArray = new SynchronizedTabulatedFunction(arrayBase);
        SynchronizedTabulatedFunction syncList = new SynchronizedTabulatedFunction(listBase);

        assertEquals(3, syncArray.getCount());
        assertEquals(3, syncList.getCount());
        assertEquals(20.0, syncArray.getY(1), 1e-9);
        assertEquals(20.0, syncList.getY(1), 1e-9);
    }
}