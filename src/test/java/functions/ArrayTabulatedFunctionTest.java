package functions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ArrayTabulatedFunctionTest {

    @Test
    void testConstructorArraysWithInvalidLength() {
        double[] x = {1};
        double[] y = {2};
        assertThrows(IllegalArgumentException.class, () -> new ArrayTabulatedFunction(x, y));
    }

    @Test
    void testConstructorFunctionWithInvalidCount() {
        MathFunction source = x -> x * x;
        assertThrows(IllegalArgumentException.class, () -> new ArrayTabulatedFunction(source, 0, 2, 1));
    }

    @Test
    void testGetXWithInvalidIndex() {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(x, y);

        assertThrows(IllegalArgumentException.class, () -> f.getX(-1));
        assertThrows(IllegalArgumentException.class, () -> f.getX(3));
    }

    @Test
    void testGetYWithInvalidIndex() {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(x, y);

        assertThrows(IllegalArgumentException.class, () -> f.getY(-1));
        assertThrows(IllegalArgumentException.class, () -> f.getY(3));
    }

    @Test
    void testSetYWithInvalidIndex() {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(x, y);

        assertThrows(IllegalArgumentException.class, () -> f.setY(-1, 10));
        assertThrows(IllegalArgumentException.class, () -> f.setY(3, 10));
    }

    @Test
    void testRemoveWithInvalidIndex() {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(x, y);

        assertThrows(IllegalArgumentException.class, () -> f.remove(-1));
        assertThrows(IllegalArgumentException.class, () -> f.remove(3));
    }

    @Test
    void testFloorIndexOfXWithXLessThanLeftBound() {
        double[] x = {2, 4, 6};
        double[] y = {4, 8, 12};
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(x, y);

        assertThrows(IllegalArgumentException.class, () -> f.floorIndexOfX(1));
    }

    @Test
    void testConstructorArrays() {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(x, y);

        assertEquals(3, f.getCount());
        assertEquals(1, f.getX(0));
        assertEquals(6, f.getY(2));
    }

    @Test
    void testConstructorFunction() {
        MathFunction source = x -> x * x;
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(source, 0, 2, 3);

        assertEquals(3, f.getCount());
        assertEquals(0, f.getX(0));
        assertEquals(1, f.getX(1));
        assertEquals(2, f.getX(2));
        assertEquals(0, f.getY(0));
        assertEquals(1, f.getY(1));
        assertEquals(4, f.getY(2));
    }

    @Test
    void testInsertReplaceExistingX() {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(x, y);

        f.insert(2, 10);
        assertEquals(10, f.getY(1));
        assertEquals(3, f.getCount());
    }

    @Test
    void testInsertAtExactPosition() {
        double[] x = {1, 3, 4};
        double[] y = {4, 6, 7};
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(x, y);

        f.insert(2, 10);
        assertEquals(2, f.getX(1));
        assertEquals(10, f.getY(1));
        assertEquals(4, f.getCount());
    }

    @Test
    void testIndexOfXFound() {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(x, y);

        assertEquals(0, f.indexOfX(1));
        assertEquals(1, f.indexOfX(2));
        assertEquals(2, f.indexOfX(3));
    }

    @Test
    void testIndexOfXNotFound() {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(x, y);

        assertEquals(-1, f.indexOfX(0));
        assertEquals(-1, f.indexOfX(4));
    }

    @Test
    void testIndexOfYFound() {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(x, y);

        assertEquals(0, f.indexOfY(4));
        assertEquals(1, f.indexOfY(5));
        assertEquals(2, f.indexOfY(6));
    }

    @Test
    void testIndexOfYNotFound() {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(x, y);

        assertEquals(-1, f.indexOfY(3));
        assertEquals(-1, f.indexOfY(7));
    }

    @Test
    void testRemoveFirstElement() {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(x, y);

        f.remove(0);
        assertEquals(2, f.getCount());
        assertEquals(2, f.getX(0));
        assertEquals(3, f.getX(1));
        assertEquals(5, f.getY(0));
        assertEquals(6, f.getY(1));
    }

    @Test
    void testConstructorFunctionWithXFromGreaterThanXToB() {
        MathFunction source = x -> 2 * x;
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(source, 10, 5, 3);

        assertEquals(3, f.getCount());
        assertEquals(5, f.getX(0), 0.001);
        assertEquals(7.5, f.getX(1), 0.001);
        assertEquals(10, f.getX(2), 0.001);
    }

    @Test
    void testConstructorFunctionWithXFromEqualToXToB() {
        MathFunction source = x -> x * x;
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(source, 3, 3, 3);

        assertEquals(3, f.getCount());
        for (int i = 0; i < 3; i++) {
            assertEquals(3, f.getX(i));
            assertEquals(9, f.getY(i));
        }
    }

    @Test
    void testSetYValidIndexB() {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(x, y);

        f.setY(1, 10);
        assertEquals(10, f.getY(1));
        f.setY(0, 20);
        assertEquals(20, f.getY(0));
        f.setY(2, 30);
        assertEquals(30, f.getY(2));
    }

    @Test
    void testFloorIndexOfXGreaterThanRightBoundB() {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(x, y);

        assertEquals(3, f.floorIndexOfX(4));
        assertEquals(3, f.floorIndexOfX(5));
    }

    @Test
    void testFloorIndexOfXExactMatchB() {
        double[] x = {1, 2, 3, 4};
        double[] y = {1, 4, 9, 16};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(x, y);

        assertEquals(1, f.floorIndexOfX(2));
        assertEquals(2, f.floorIndexOfX(3));
    }

    @Test
    void testFloorIndexOfXLastIntervalB() {
        double[] x = {1, 2, 3, 4};
        double[] y = {1, 4, 9, 16};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(x, y);

        assertEquals(2, f.floorIndexOfX(3.5));
        assertEquals(2, f.floorIndexOfX(3.9));
        assertEquals(3, f.floorIndexOfX(4.0));
    }

    @Test
    void testFloorNodeOfXMiddle() {
        double[] x = {1, 2, 3, 4};
        double[] y = {1, 4, 9, 16};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(x, y);

        assertEquals(6.5, f.apply(2.5), 0.001);
        assertEquals(6.5, f.apply(2.5), 0.001);
    }

    @Test
    void testRemoveFirstElementB() {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(x, y);

        f.remove(0);
        assertEquals(2, f.getCount());
        assertEquals(2, f.getX(0));
        assertEquals(3, f.getX(1));
        assertEquals(2, f.leftBound());
    }

    @Test
    void testRemoveMiddleElementUpdatesLinks() {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(x, y);

        f.remove(1);
        assertEquals(2, f.getCount());
        assertEquals(1, f.getX(0));
        assertEquals(3, f.getX(1));

        assertEquals(6, f.getY(1));
        assertEquals(6, f.getY(1));
    }

    @Test
    void testApplyExtrapolationLeft() {
        double[] x = {1, 2, 3};
        double[] y = {1, 4, 9};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(x, y);

        double result = f.apply(0);
        assertEquals(-2.0, result, 0.001);
    }

    @Test
    void testApplyExtrapolationRight() {
        double[] x = {1, 2, 3};
        double[] y = {1, 4, 9};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(x, y);

        double result = f.apply(4);
        assertEquals(14.0, result, 0.001);
    }

    @Test
    void testApplyInterpolationBetweenNodes() {
        double[] x = {1, 2, 3};
        double[] y = {1, 4, 9};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(x, y);

        double result = f.apply(1.5);
        assertEquals(2.5, result, 0.001);
    }

    @Test
    void testInsertIntoEmptyList() {
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(new double[]{1, 2}, new double[]{3, 4});
        f.remove(0);
        f.remove(0);

        f.insert(5, 10);
        assertEquals(1, f.getCount());
        assertEquals(5, f.getX(0));
        assertEquals(10, f.getY(0));
    }

    @Test
    void testInsertReplaceExisting() {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(x, y);

        f.insert(2, 10);
        assertEquals(10, f.getY(1));
        assertEquals(3, f.getCount());
    }

    @Test
    void testInsertAtBeginning() {
        double[] x = {2, 3, 4};
        double[] y = {5, 6, 7};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(x, y);

        f.insert(1, 10);
        assertEquals(1, f.getX(0));
        assertEquals(10, f.getY(0));
        assertEquals(4, f.getCount());
    }

    @Test
    void testInsertInMiddleWhileLoopB() {
        double[] x = {1, 3, 5};
        double[] y = {2, 4, 6};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(x, y);

        f.insert(2, 10);
        assertEquals(2, f.getX(1));
        assertEquals(10, f.getY(1));
        assertEquals(4, f.getCount());
    }

    @Test
    void testInsertAtEndWhileLoopB() {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(x, y);

        f.insert(5, 10);
        assertEquals(5, f.getX(3));
        assertEquals(10, f.getY(3));
        assertEquals(4, f.getCount());
    }

    @Test
    void testIndexOfXFoundB() {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(x, y);

        assertEquals(0, f.indexOfX(1));
        assertEquals(1, f.indexOfX(2));
        assertEquals(2, f.indexOfX(3));
    }

    @Test
    void testIndexOfXNotFoundB() {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(x, y);

        assertEquals(-1, f.indexOfX(0));
        assertEquals(-1, f.indexOfX(4));
    }

    @Test
    void testIndexOfYFoundB() {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(x, y);

        assertEquals(0, f.indexOfY(4));
        assertEquals(1, f.indexOfY(5));
        assertEquals(2, f.indexOfY(6));
    }

    @Test
    void testIndexOfYNotFoundB() {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(x, y);

        assertEquals(-1, f.indexOfY(3));
        assertEquals(-1, f.indexOfY(7));
    }

    @Test
    void testApplyExactMatch() {
        double[] x = {0, 1, 2};
        double[] y = {0, 1, 4};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(x, y);

        assertEquals(0, f.apply(0));
        assertEquals(1, f.apply(1));
        assertEquals(4, f.apply(2));
    }

    @Test
    void testApplyInterpolation() {
        double[] x = {0, 1, 2};
        double[] y = {0, 1, 4};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(x, y);

        assertEquals(0.5, f.apply(0.5), 0.001);
        assertEquals(2.5, f.apply(1.5), 0.001);
    }

    @Test
    void testExtrapolateLeftB() {
        double[] x = {2, 4, 6};
        double[] y = {4, 8, 12};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(x, y);

        double result = f.extrapolateLeft(0);
        assertEquals(0.0, result, 0.001);
    }

    @Test
    void testExtrapolateRightB() {
        double[] x = {2, 4, 6};
        double[] y = {4, 8, 12};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(x, y);

        double result = f.extrapolateRight(8);
        assertEquals(16.0, result, 0.001);
    }

    @Test
    void testInterpolateWithIndexВ() {
        double[] x = {1, 2, 3};
        double[] y = {1, 4, 9};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(x, y);

        double result = f.interpolate(1.5, 0);
        assertEquals(2.5, result, 0.001);
    }

    @Test
    void testRemoveLastElement() {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(x, y);

        f.remove(2);
        assertEquals(2, f.getCount());
        assertEquals(1, f.getX(0));
        assertEquals(2, f.getX(1));
        assertEquals(4, f.getY(0));
        assertEquals(5, f.getY(1));
    }

    @Test
    void testRemoveMiddleElement() {
        double[] x = {1, 2, 3, 4};
        double[] y = {4, 5, 6, 7};
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(x, y);

        f.remove(1);
        assertEquals(3, f.getCount());
        assertEquals(1, f.getX(0));
        assertEquals(3, f.getX(1));
        assertEquals(4, f.getX(2));
        assertEquals(4, f.getY(0));
        assertEquals(6, f.getY(1));
        assertEquals(7, f.getY(2));
    }

    @Test
    void testInsertAtBeginningWhileLoop() {
        double[] x = {2, 3, 4};
        double[] y = {5, 6, 7};
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(x, y);

        f.insert(1, 10);
        assertEquals(4, f.getCount());
        assertEquals(1, f.getX(0));
        assertEquals(2, f.getX(1));
        assertEquals(3, f.getX(2));
        assertEquals(4, f.getX(3));
        assertEquals(10, f.getY(0));
        assertEquals(5, f.getY(1));
        assertEquals(6, f.getY(2));
        assertEquals(7, f.getY(3));
    }

    @Test
    void testInsertInMiddleWhileLoop() {
        double[] x = {1, 3, 5};
        double[] y = {2, 4, 6};
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(x, y);

        f.insert(2, 10);
        assertEquals(4, f.getCount());
        assertEquals(1, f.getX(0));
        assertEquals(2, f.getX(1));
        assertEquals(3, f.getX(2));
        assertEquals(5, f.getX(3));
        assertEquals(2, f.getY(0));
        assertEquals(10, f.getY(1));
        assertEquals(4, f.getY(2));
        assertEquals(6, f.getY(3));
    }

    @Test
    void testInsertAtEndWhileLoop() {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(x, y);

        f.insert(5, 10);
        assertEquals(4, f.getCount());
        assertEquals(1, f.getX(0));
        assertEquals(2, f.getX(1));
        assertEquals(3, f.getX(2));
        assertEquals(5, f.getX(3));
        assertEquals(4, f.getY(0));
        assertEquals(5, f.getY(1));
        assertEquals(6, f.getY(2));
        assertEquals(10, f.getY(3));
    }

    @Test
    void testFloorIndexOfXExactMatch() {
        double[] x = {1, 2, 3, 4};
        double[] y = {1, 4, 9, 16};
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(x, y);

        assertEquals(1, f.floorIndexOfX(2));
        assertEquals(2, f.floorIndexOfX(3));
    }

    @Test
    void testFloorIndexOfXGreaterThanRightBound() {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(x, y);

        assertEquals(3, f.floorIndexOfX(4));
        assertEquals(3, f.floorIndexOfX(5));
    }

    @Test
    void testFloorIndexOfXLastInterval() {
        double[] x = {1, 2, 3, 4};
        double[] y = {1, 4, 9, 16};
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(x, y);

        assertEquals(2, f.floorIndexOfX(3.5));
        assertEquals(2, f.floorIndexOfX(3.9));
        assertEquals(3, f.floorIndexOfX(4.0));
    }

    @Test
    void testExtrapolateLeft() {
        double[] x = {2, 4, 6};
        double[] y = {4, 8, 12};
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(x, y);

        double result = f.extrapolateLeft(0);
        assertEquals(0.0, result, 0.001);
    }

    @Test
    void testExtrapolateRight() {
        double[] x = {2, 4, 6};
        double[] y = {4, 8, 12};
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(x, y);

        double result = f.extrapolateRight(8);
        assertEquals(16.0, result, 0.001);
    }

    @Test
    void testInterpolateWithIndex() {
        double[] x = {1, 2, 3};
        double[] y = {1, 4, 9};
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(x, y);

        double result = f.interpolate(1.5, 0);
        assertEquals(2.5, result, 0.001);
    }

    @Test
    void testConstructorFunctionWithXFromEqualToXTo() {
        MathFunction source = x -> x * x;
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(source, 3, 3, 3);

        assertEquals(3, f.getCount());
        for (int i = 0; i < 3; i++) {
            assertEquals(3, f.getX(i));
            assertEquals(9, f.getY(i));
        }
    }

    @Test
    void testConstructorFunctionWithXFromGreaterThanXTo() {
        MathFunction source = x -> 2 * x;
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(source, 10, 5, 3);

        assertEquals(3, f.getCount());
        assertEquals(5, f.getX(0));
        assertEquals(7.5, f.getX(1), 0.001);
        assertEquals(10, f.getX(2), 0.001);
    }

    @Test
    void testSetYValidIndex() {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(x, y);

        f.setY(1, 10);
        assertEquals(10, f.getY(1));
        f.setY(0, 20);
        assertEquals(20, f.getY(0));
        f.setY(2, 30);
        assertEquals(30, f.getY(2));
    }
}