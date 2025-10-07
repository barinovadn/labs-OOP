package functions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Iterator;

public class StrictTabulatedFunctionTest {

    @Test
    void testApplyWithInterpolationArray() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};

        TabulatedFunction arrayFunc = new ArrayTabulatedFunction(xValues, yValues);
        TabulatedFunction strictArrayFunc = new StrictTabulatedFunction(arrayFunc);

        assertThrows(UnsupportedOperationException.class, () -> strictArrayFunc.apply(1.5));
    }

    @Test
    void testGetCount() {
        double[] xValues = {1.0, 2.0};
        double[] yValues = {10.0, 20.0};

        TabulatedFunction arrayFunc = new ArrayTabulatedFunction(xValues, yValues);
        TabulatedFunction strictArrayFunc = new StrictTabulatedFunction(arrayFunc);

        assertEquals(2, strictArrayFunc.getCount());
    }

    @Test
    void testGetX() {
        double[] xValues = {1.0, 2.0};
        double[] yValues = {10.0, 20.0};

        TabulatedFunction arrayFunc = new ArrayTabulatedFunction(xValues, yValues);
        TabulatedFunction strictArrayFunc = new StrictTabulatedFunction(arrayFunc);

        assertEquals(1.0, strictArrayFunc.getX(0), 1e-9);
        assertEquals(2.0, strictArrayFunc.getX(1), 1e-9);
    }

    @Test
    void testStrictWrappedInUnmodifiable() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};

        TabulatedFunction arrayFunc = new ArrayTabulatedFunction(xValues, yValues);
        TabulatedFunction strictFunc = new StrictTabulatedFunction(arrayFunc);
        TabulatedFunction unmodifiableStrictFunc = new UnmodifiableTabulatedFunction(strictFunc);

        assertEquals(20.0, unmodifiableStrictFunc.apply(2.0), 1e-9);
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableStrictFunc.apply(1.5));
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableStrictFunc.setY(0, 15.0));
    }

    @Test
    void testUnmodifiableWrappedInStrict() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};

        TabulatedFunction arrayFunc = new ArrayTabulatedFunction(xValues, yValues);
        TabulatedFunction unmodifiableFunc = new UnmodifiableTabulatedFunction(arrayFunc);
        TabulatedFunction strictUnmodifiableFunc = new StrictTabulatedFunction(unmodifiableFunc);

        assertEquals(20.0, strictUnmodifiableFunc.apply(2.0), 1e-9);
        assertThrows(UnsupportedOperationException.class, () -> strictUnmodifiableFunc.apply(1.5));
        assertThrows(UnsupportedOperationException.class, () -> strictUnmodifiableFunc.setY(0, 15.0));
    }

    @Test
    void testGetY() {
        double[] xValues = {1.0, 2.0};
        double[] yValues = {10.0, 20.0};

        TabulatedFunction arrayFunc = new ArrayTabulatedFunction(xValues, yValues);
        TabulatedFunction strictArrayFunc = new StrictTabulatedFunction(arrayFunc);

        assertEquals(10.0, strictArrayFunc.getY(0), 1e-9);
        assertEquals(20.0, strictArrayFunc.getY(1), 1e-9);
    }

    @Test
    void testSetY() {
        double[] xValues = {1.0, 2.0};
        double[] yValues = {10.0, 20.0};

        TabulatedFunction arrayFunc = new ArrayTabulatedFunction(xValues, yValues);
        TabulatedFunction strictArrayFunc = new StrictTabulatedFunction(arrayFunc);

        strictArrayFunc.setY(1, 25.0);
        assertEquals(25.0, strictArrayFunc.getY(1), 1e-9);
    }

    @Test
    void testIndexOfX() {
        double[] xValues = {1.0, 2.0};
        double[] yValues = {10.0, 20.0};

        TabulatedFunction arrayFunc = new ArrayTabulatedFunction(xValues, yValues);
        TabulatedFunction strictArrayFunc = new StrictTabulatedFunction(arrayFunc);

        assertEquals(0, strictArrayFunc.indexOfX(1.0));
        assertEquals(-1, strictArrayFunc.indexOfX(3.0));
    }

    @Test
    void testIndexOfY() {
        double[] xValues = {1.0, 2.0};
        double[] yValues = {10.0, 20.0};

        TabulatedFunction arrayFunc = new ArrayTabulatedFunction(xValues, yValues);
        TabulatedFunction strictArrayFunc = new StrictTabulatedFunction(arrayFunc);

        assertEquals(0, strictArrayFunc.indexOfY(10.0));
        assertEquals(-1, strictArrayFunc.indexOfY(30.0));
    }

    @Test
    void testLeftBound() {
        double[] xValues = {1.0, 2.0};
        double[] yValues = {10.0, 20.0};

        TabulatedFunction arrayFunc = new ArrayTabulatedFunction(xValues, yValues);
        TabulatedFunction strictArrayFunc = new StrictTabulatedFunction(arrayFunc);

        assertEquals(1.0, strictArrayFunc.leftBound(), 1e-9);
    }

    @Test
    void testRightBound() {
        double[] xValues = {1.0, 2.0};
        double[] yValues = {10.0, 20.0};

        TabulatedFunction arrayFunc = new ArrayTabulatedFunction(xValues, yValues);
        TabulatedFunction strictArrayFunc = new StrictTabulatedFunction(arrayFunc);

        assertEquals(2.0, strictArrayFunc.rightBound(), 1e-9);
    }

    @Test
    void testIterator() {
        double[] xValues = {1.0, 2.0};
        double[] yValues = {10.0, 20.0};

        TabulatedFunction arrayFunc = new ArrayTabulatedFunction(xValues, yValues);
        TabulatedFunction strictArrayFunc = new StrictTabulatedFunction(arrayFunc);

        Iterator<Point> iterator = strictArrayFunc.iterator();
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