package concurrent;

import functions.ArrayTabulatedFunction;
import functions.LinkedListTabulatedFunction;
import functions.Point;
import functions.TabulatedFunction;
import org.junit.jupiter.api.Test;
import java.util.Iterator;
import java.util.NoSuchElementException;
import static org.junit.jupiter.api.Assertions.*;

public class SynchronizedTabulatedFunctionTest {

    @Test
    void testMethods() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        TabulatedFunction baseFunction = new ArrayTabulatedFunction(xValues, yValues);
        SynchronizedTabulatedFunction syncFunction = new SynchronizedTabulatedFunction(baseFunction);

        assertEquals(3, syncFunction.getCount());
        assertEquals(1.0, syncFunction.getX(0), 1e-9);
        assertEquals(10.0, syncFunction.getY(0), 1e-9);

        syncFunction.setY(1, 25.0);
        assertEquals(25.0, syncFunction.getY(1), 1e-9);

        assertEquals(0, syncFunction.indexOfX(1.0));
        assertEquals(1, syncFunction.indexOfY(25.0));

        assertEquals(1.0, syncFunction.leftBound(), 1e-9);
        assertEquals(3.0, syncFunction.rightBound(), 1e-9);

        assertEquals(10.0, syncFunction.apply(1.0), 1e-9);
        assertEquals(17.5, syncFunction.apply(1.5), 1e-9);
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

        assertTrue(iterator.hasNext());
        Point point2 = iterator.next();
        assertEquals(2.0, point2.x, 1e-9);
        assertEquals(20.0, point2.y, 1e-9);

        assertFalse(iterator.hasNext());
    }

    @Test
    void testIteratorException() {
        double[] xValues = {1.0, 2.0};
        double[] yValues = {10.0, 20.0};
        TabulatedFunction baseFunction = new ArrayTabulatedFunction(xValues, yValues);
        SynchronizedTabulatedFunction syncFunction = new SynchronizedTabulatedFunction(baseFunction);

        Iterator<Point> iterator = syncFunction.iterator();
        iterator.next();
        iterator.next();

        assertThrows(NoSuchElementException.class, iterator::next);
    }
}