package functions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Iterator;

public class UnmodifiableTabulatedFunctionTest {

    @Test
    void testSetYThrowsExceptionArray() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};

        TabulatedFunction arrayFunc = new ArrayTabulatedFunction(xValues, yValues);
        TabulatedFunction unmodifiableArrayFunc = new UnmodifiableTabulatedFunction(arrayFunc);

        assertThrows(UnsupportedOperationException.class, () -> unmodifiableArrayFunc.setY(0, 15.0));
    }

    @Test
    void testSetYThrowsExceptionList() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};

        TabulatedFunction listFunc = new LinkedListTabulatedFunction(xValues, yValues);
        TabulatedFunction unmodifiableListFunc = new UnmodifiableTabulatedFunction(listFunc);

        assertThrows(UnsupportedOperationException.class, () -> unmodifiableListFunc.setY(1, 25.0));
    }

    @Test
    void testGetCount() {
        double[] xValues = {1.0, 2.0};
        double[] yValues = {10.0, 20.0};

        TabulatedFunction arrayFunc = new ArrayTabulatedFunction(xValues, yValues);
        TabulatedFunction unmodifiableArrayFunc = new UnmodifiableTabulatedFunction(arrayFunc);

        assertEquals(2, unmodifiableArrayFunc.getCount());
    }

    @Test
    void testGetX() {
        double[] xValues = {1.0, 2.0};
        double[] yValues = {10.0, 20.0};

        TabulatedFunction arrayFunc = new ArrayTabulatedFunction(xValues, yValues);
        TabulatedFunction unmodifiableArrayFunc = new UnmodifiableTabulatedFunction(arrayFunc);

        assertEquals(1.0, unmodifiableArrayFunc.getX(0), 1e-9);
        assertEquals(2.0, unmodifiableArrayFunc.getX(1), 1e-9);
    }

    @Test
    void testGetY() {
        double[] xValues = {1.0, 2.0};
        double[] yValues = {10.0, 20.0};

        TabulatedFunction arrayFunc = new ArrayTabulatedFunction(xValues, yValues);
        TabulatedFunction unmodifiableArrayFunc = new UnmodifiableTabulatedFunction(arrayFunc);

        assertEquals(10.0, unmodifiableArrayFunc.getY(0), 1e-9);
        assertEquals(20.0, unmodifiableArrayFunc.getY(1), 1e-9);
    }

    @Test
    void testIndexOfX() {
        double[] xValues = {1.0, 2.0};
        double[] yValues = {10.0, 20.0};

        TabulatedFunction arrayFunc = new ArrayTabulatedFunction(xValues, yValues);
        TabulatedFunction unmodifiableArrayFunc = new UnmodifiableTabulatedFunction(arrayFunc);

        assertEquals(0, unmodifiableArrayFunc.indexOfX(1.0));
        assertEquals(-1, unmodifiableArrayFunc.indexOfX(3.0));
    }

    @Test
    void testIndexOfY() {
        double[] xValues = {1.0, 2.0};
        double[] yValues = {10.0, 20.0};

        TabulatedFunction arrayFunc = new ArrayTabulatedFunction(xValues, yValues);
        TabulatedFunction unmodifiableArrayFunc = new UnmodifiableTabulatedFunction(arrayFunc);

        assertEquals(0, unmodifiableArrayFunc.indexOfY(10.0));
        assertEquals(-1, unmodifiableArrayFunc.indexOfY(30.0));
    }

    @Test
    void testLeftBound() {
        double[] xValues = {1.0, 2.0};
        double[] yValues = {10.0, 20.0};

        TabulatedFunction arrayFunc = new ArrayTabulatedFunction(xValues, yValues);
        TabulatedFunction unmodifiableArrayFunc = new UnmodifiableTabulatedFunction(arrayFunc);

        assertEquals(1.0, unmodifiableArrayFunc.leftBound(), 1e-9);
    }

    @Test
    void testRightBound() {
        double[] xValues = {1.0, 2.0};
        double[] yValues = {10.0, 20.0};

        TabulatedFunction arrayFunc = new ArrayTabulatedFunction(xValues, yValues);
        TabulatedFunction unmodifiableArrayFunc = new UnmodifiableTabulatedFunction(arrayFunc);

        assertEquals(2.0, unmodifiableArrayFunc.rightBound(), 1e-9);
    }

    @Test
    void testApply() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};

        TabulatedFunction arrayFunc = new ArrayTabulatedFunction(xValues, yValues);
        TabulatedFunction unmodifiableArrayFunc = new UnmodifiableTabulatedFunction(arrayFunc);

        TabulatedFunction listFunc = new LinkedListTabulatedFunction(xValues, yValues);
        TabulatedFunction unmodifiableListFunc = new UnmodifiableTabulatedFunction(listFunc);

        assertEquals(20.0, unmodifiableArrayFunc.apply(2.0), 1e-9);
        assertEquals(15.0, unmodifiableArrayFunc.apply(1.5), 1e-9);
        assertEquals(20.0, unmodifiableListFunc.apply(2.0), 1e-9);
        assertEquals(15.0, unmodifiableListFunc.apply(1.5), 1e-9);
    }

    @Test
    void testIterator() {
        double[] xValues = {1.0, 2.0};
        double[] yValues = {10.0, 20.0};

        TabulatedFunction arrayFunc = new ArrayTabulatedFunction(xValues, yValues);
        TabulatedFunction unmodifiableArrayFunc = new UnmodifiableTabulatedFunction(arrayFunc);

        Iterator<Point> iterator = unmodifiableArrayFunc.iterator();
        assertTrue(iterator.hasNext());
        Point point = iterator.next();
        assertEquals(1.0, point.x, 1e-9);
        assertEquals(10.0, point.y, 1e-9);

        assertTrue(iterator.hasNext());
        point = iterator.next();
        assertEquals(2.0, point.x, 1e-9);
        assertEquals(20.0, point.y, 1e-9);

        assertFalse(iterator.hasNext());
    }
}