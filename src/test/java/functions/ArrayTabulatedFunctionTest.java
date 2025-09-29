package functions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ArrayTabulatedFunctionTest {

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
    void testConstructorFunctionWithXFromGreaterThanXTo() {
        MathFunction source = x -> x * x;
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(source, 2, 0, 3);

        assertEquals(3, f.getCount());
        assertEquals(0, f.getX(0));
        assertEquals(1, f.getX(1));
        assertEquals(2, f.getX(2));
        assertEquals(0, f.getY(0));
        assertEquals(1, f.getY(1));
        assertEquals(4, f.getY(2));
    }

    @Test
    void testConstructorFunctionWithXFromEqualToXTo() {
        MathFunction source = x -> x * x;
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(source, 3, 3, 5);

        assertEquals(5, f.getCount());
        for (int i = 0; i < 5; i++) {
            assertEquals(3, f.getX(i));
        }
        for (int i = 0; i < 5; i++) {
            assertEquals(9, f.getY(i));
        }
    }

    @Test
    void testConstructorFunctionWithSinglePoint() {
        MathFunction source = x -> x + 1;
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(source, 5, 5, 1);

        assertEquals(1, f.getCount());
        assertEquals(5, f.getX(0));
        assertEquals(6, f.getY(0));
    }

    @Test
    void testConstructorFunctionWithTwoPointsReversed() {
        MathFunction source = x -> 2 * x;
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(source, 10, 5, 2);

        assertEquals(2, f.getCount());
        assertEquals(5, f.getX(0));
        assertEquals(10, f.getX(1));
        assertEquals(10, f.getY(0));
        assertEquals(20, f.getY(1));
    }

    @Test
    void testSetY() {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(x, y);

        f.setY(1, 10);
        assertEquals(10, f.getY(1));
    }

    @Test
    void testIndexOf() {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(x, y);

        assertEquals(1, f.indexOfX(2));
        assertEquals(2, f.indexOfY(6));
        assertEquals(-1, f.indexOfX(5));
        assertEquals(-1, f.indexOfY(100));
    }

    @Test
    void testBounds() {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(x, y);

        assertEquals(1, f.leftBound());
        assertEquals(3, f.rightBound());
    }

    @Test
    void testApplyExactMatch() {
        double[] x = {0, 1, 2};
        double[] y = {0, 1, 4};
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(x, y);

        assertEquals(0, f.apply(0));
        assertEquals(1, f.apply(1));
        assertEquals(4, f.apply(2));
    }

    @Test
    void testApplyInterpolation() {
        double[] x = {0, 1, 2};
        double[] y = {0, 1, 4};
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(x, y);

        assertEquals(0.5, f.apply(0.5), 0.001);
        assertEquals(2.5, f.apply(1.5), 0.001);
        assertEquals(3.25, f.apply(1.75), 0.001);
    }

    @Test
    void testApplyExtrapolationLeft() {
        double[] x = {1, 2, 3};
        double[] y = {1, 4, 9};
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(x, y);

        double result = f.apply(0);
        assertEquals(-2.0, result, 0.001);
    }

    @Test
    void testApplyExtrapolationRight() {
        double[] x = {1, 2, 3};
        double[] y = {1, 4, 9};
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(x, y);

        double result = f.apply(4);
        assertEquals(14.0, result, 0.001);
    }

    @Test
    void testFloorIndexOfX() {
        double[] x = {1, 3, 5};
        double[] y = {2, 4, 6};
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(x, y);

        assertEquals(0, f.floorIndexOfX(1));
        assertEquals(0, f.floorIndexOfX(2));
        assertEquals(1, f.floorIndexOfX(3));
        assertEquals(1, f.floorIndexOfX(4));
        assertEquals(2, f.floorIndexOfX(5));
        assertEquals(3, f.floorIndexOfX(6));
    }

    @Test
    void testFloorIndexOfXWithXLessThanLeftBound() {
        double[] x = {2, 4, 6};
        double[] y = {4, 8, 12};
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(x, y);

        // Тестируем случай когда x < xValues[0]
        assertEquals(0, f.floorIndexOfX(0));
        assertEquals(0, f.floorIndexOfX(1));
    }

    @Test
    void testFloorIndexOfXWithExactMatchInMiddle() {
        double[] x = {1, 2, 3, 4, 5};
        double[] y = {1, 4, 9, 16, 25};
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(x, y);

        // Тестируем случай когда x точно равен одному из значений в середине
        assertEquals(2, f.floorIndexOfX(3));

        // Тестируем случай когда x находится между значениями
        assertEquals(2, f.floorIndexOfX(3.5));
        assertEquals(3, f.floorIndexOfX(4.2));
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
    void testFloorIndexOfXInLastInterval() {
        double[] x = {1, 3, 5, 7};
        double[] y = {2, 4, 6, 8};
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(x, y);

        assertEquals(2, f.floorIndexOfX(5.5));
        assertEquals(2, f.floorIndexOfX(6.0));
        assertEquals(2, f.floorIndexOfX(6.9));

        assertEquals(2, f.floorIndexOfX(5.0));
        assertEquals(3, f.floorIndexOfX(7.0));
    }

    @Test
    void testFloorIndexOfXBetweenLastElements() {
        double[] x = {1.0, 2.0, 3.0, 4.0};
        double[] y = {1.0, 4.0, 9.0, 16.0};
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(x, y);

        assertEquals(2, f.floorIndexOfX(3.5));
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
    void testInterpolateWithCoordinates() {
        double[] x = {1, 2, 3};
        double[] y = {1, 4, 9};
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(x, y);

        double result = f.interpolate(1.5, 1, 2, 1, 4);
        assertEquals(2.5, result, 0.001);
    }

    @Test
    void testInsertReplace() {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(x, y);

        f.insert(2, 10);
        assertEquals(10, f.getY(1));
        assertEquals(3, f.getCount());
    }

    @Test
    void testInsertAtBeginning() {
        double[] x = {2, 3, 4};
        double[] y = {5, 6, 7};
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(x, y);

        f.insert(1, 10);
        assertEquals(1, f.getX(0));
        assertEquals(10, f.getY(0));
        assertEquals(4, f.getCount());
    }

    @Test
    void testInsertAtEnd() {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(x, y);

        f.insert(4, 10);
        assertEquals(4, f.getX(3));
        assertEquals(10, f.getY(3));
        assertEquals(4, f.getCount());
    }

    @Test
    void testInsertInMiddle() {
        double[] x = {1, 3, 4};
        double[] y = {4, 6, 7};
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(x, y);

        f.insert(2, 10);
        assertEquals(2, f.getX(1));
        assertEquals(10, f.getY(1));
        assertEquals(4, f.getCount());
    }

    @Test
    void testRemoveFirst() {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(x, y);

        f.remove(0);
        assertEquals(2, f.getCount());
        assertEquals(2, f.getX(0));
        assertEquals(3, f.getX(1));
    }

    @Test
    void testRemoveLast() {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(x, y);

        f.remove(2);
        assertEquals(2, f.getCount());
        assertEquals(1, f.getX(0));
        assertEquals(2, f.getX(1));
    }

    @Test
    void testRemoveMiddle() {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(x, y);

        f.remove(1);
        assertEquals(2, f.getCount());
        assertEquals(1, f.getX(0));
        assertEquals(3, f.getX(1));
    }

    @Test
    void testRemoveSingle() {
        double[] x = {1};
        double[] y = {4};
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(x, y);

        f.remove(0);
        assertEquals(0, f.getCount());
    }

    @Test
    void testToString() {
        double[] x = {1, 2};
        double[] y = {3, 4};
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(x, y);

        String result = f.toString();
        assertNotNull(result);
        assertFalse(result.contains("1.0"));
        assertFalse(result.contains("3.0"));
    }

    @Test
    void testEqualsAndHashCode() {
        double[] x1 = {1, 2, 3};
        double[] y1 = {4, 5, 6};
        ArrayTabulatedFunction f1 = new ArrayTabulatedFunction(x1, y1);

        double[] x2 = {1, 2, 3};
        double[] y2 = {4, 5, 6};
        ArrayTabulatedFunction f2 = new ArrayTabulatedFunction(x2, y2);

        double[] x3 = {1, 2, 3};
        double[] y3 = {4, 5, 7};
        ArrayTabulatedFunction f3 = new ArrayTabulatedFunction(x3, y3);

        assertNotEquals(f1, f2);
        assertNotEquals(f1, f3);
        assertNotEquals(f1.hashCode(), f2.hashCode());
    }

}