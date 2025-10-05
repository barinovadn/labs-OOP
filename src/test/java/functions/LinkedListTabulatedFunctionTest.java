package functions;

import exceptions.ArrayIsNotSortedException;
import exceptions.DifferentLengthOfArraysException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LinkedListTabulatedFunctionTest {

    @Test
    void testConstructorArraysWithInvalidLength() {
        double[] x = {1};
        double[] y = {2};
        assertThrows(IllegalArgumentException.class, () -> new LinkedListTabulatedFunction(x, y));
    }

    @Test
    void testConstructorWithInvalidArrays() {
        double[] xValues = {1, 2, 3};
        double[] yValues = {1, 2};
        assertThrows(DifferentLengthOfArraysException.class, () -> new LinkedListTabulatedFunction(xValues, yValues));

        double[] xNotSorted = {1, 3, 2};
        double[] yNotSorted = {1, 2, 3};
        assertThrows(ArrayIsNotSortedException.class, () -> new LinkedListTabulatedFunction(xNotSorted, yNotSorted));
    }

    @Test
    void testConstructorFunctionWithInvalidCount() {
        MathFunction source = x -> x * x;
        assertThrows(IllegalArgumentException.class, () -> new LinkedListTabulatedFunction(source, 0, 2, 1));
    }

    @Test
    void testGetXWithInvalidIndex() {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(x, y);

        assertThrows(IllegalArgumentException.class, () -> f.getX(-1));
        assertThrows(IllegalArgumentException.class, () -> f.getX(3));
    }

    @Test
    void testGetYWithInvalidIndex() {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(x, y);

        assertThrows(IllegalArgumentException.class, () -> f.getY(-1));
        assertThrows(IllegalArgumentException.class, () -> f.getY(3));
    }

    @Test
    void testSetYWithInvalidIndex() {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(x, y);

        assertThrows(IllegalArgumentException.class, () -> f.setY(-1, 10));
        assertThrows(IllegalArgumentException.class, () -> f.setY(3, 10));
    }

    @Test
    void testRemoveWithInvalidIndex() {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(x, y);

        assertThrows(IllegalArgumentException.class, () -> f.remove(-1));
        assertThrows(IllegalArgumentException.class, () -> f.remove(3));
    }

    @Test
    void testFloorIndexOfXWithXLessThanLeftBound() {
        double[] x = {2, 4, 6};
        double[] y = {4, 8, 12};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(x, y);

        assertThrows(IllegalArgumentException.class, () -> f.floorIndexOfX(1));
    }

    @Test
    void testFloorNodeOfXWithXLessThanLeftBound() {
        double[] x = {2, 4, 6};
        double[] y = {4, 8, 12};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(x, y);

        assertThrows(IllegalArgumentException.class, () -> f.floorNodeOfX(1));
    }

    @Test
    void testConstructorArrays() {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(x, y);

        assertEquals(3, f.getCount());
        assertEquals(1, f.getX(0));
        assertEquals(6, f.getY(2));
    }

    @Test
    void testConstructorFunction() {
        MathFunction source = x -> x * x;
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(source, 0, 2, 3);

        assertEquals(3, f.getCount());
        assertEquals(0, f.getX(0));
        assertEquals(1, f.getX(1));
        assertEquals(2, f.getX(2));
    }
}