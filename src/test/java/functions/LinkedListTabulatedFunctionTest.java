package functions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LinkedListTabulatedFunctionTest {

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

    @Test
    void testSetY() {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(x, y);

        f.setY(1, 10);
        assertEquals(10, f.getY(1));
    }

    @Test
    void testBounds() {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(x, y);

        assertEquals(1, f.leftBound());
        assertEquals(3, f.rightBound());
    }

    @Test
    void testApply() {
        double[] x = {0, 1, 2};
        double[] y = {0, 1, 4};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(x, y);

        assertEquals(0, f.apply(0));
        assertEquals(1, f.apply(1));
        assertEquals(2.5, f.apply(1.5), 0.001);
    }

    @Test
    void testInsertReplace() {
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
    void testInsertAtEnd() {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(x, y);

        f.insert(4, 10);
        assertEquals(4, f.getX(3));
        assertEquals(10, f.getY(3));
        assertEquals(4, f.getCount());
    }

    @Test
    void testInsertInMiddle() {
        double[] x = {1, 3, 4};
        double[] y = {4, 6, 7};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(x, y);

        f.insert(2, 10);
        assertEquals(2, f.getX(1));
        assertEquals(10, f.getY(1));
        assertEquals(4, f.getCount());
    }

    @Test
    void testInsertEmptyList() {
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(new double[0], new double[0]);
        f.insert(1, 10);
        assertEquals(1, f.getCount());
        assertEquals(1, f.getX(0));
        assertEquals(10, f.getY(0));
    }

    @Test
    void testRemoveFirst() {
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
    void testRemoveLast() {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(x, y);

        f.remove(2);
        assertEquals(2, f.getCount());
        assertEquals(1, f.getX(0));
        assertEquals(2, f.getX(1));
        assertEquals(2, f.rightBound());
    }

    @Test
    void testRemoveMiddle() {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(x, y);

        f.remove(1);
        assertEquals(2, f.getCount());
        assertEquals(1, f.getX(0));
        assertEquals(3, f.getX(1));
    }

    @Test
    void testRemoveSingle() {
        double[] x = {1};
        double[] y = {4};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(x, y);

        f.remove(0);
        assertEquals(0, f.getCount());
        assertEquals(0, f.getCount());
    }

    @Test
    void testIndexOf() {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(x, y);

        assertEquals(1, f.indexOfX(2));
        assertEquals(2, f.indexOfY(6));
        assertEquals(-1, f.indexOfX(5));
        assertEquals(-1, f.indexOfY(100));
    }

    @Test
    void testFloorIndexOfX() {
        double[] x = {1, 3, 5};
        double[] y = {2, 4, 6};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(x, y);

        assertEquals(0, f.floorIndexOfX(1));
        assertEquals(0, f.floorIndexOfX(2));
        assertEquals(1, f.floorIndexOfX(3));
        assertEquals(1, f.floorIndexOfX(4));
        assertEquals(2, f.floorIndexOfX(5));
        assertEquals(3, f.floorIndexOfX(6));
    }

    @Test
    void testExtrapolation() {
        double[] x = {1, 2, 3};
        double[] y = {1, 4, 9};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(x, y);

        double leftResult = f.apply(0);
        double rightResult = f.apply(4);
        assertEquals(-2.0, leftResult, 0.001);
        assertEquals(14.0, rightResult, 0.001);
    }

    @Test
    void testInterpolateWithIndex() {
        double[] x = {1, 2, 3};
        double[] y = {1, 4, 9};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(x, y);

        double result = f.interpolate(1.5, 0);
        assertEquals(2.5, result, 0.001);
    }

    @Test
    void testConstructorFunctionWithXFromEqualToXTo() {
        MathFunction source = x -> x * x;
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(source, 3, 3, 5);

        assertEquals(5, f.getCount());
        for (int i = 0; i < 5; i++) {
            assertEquals(3, f.getX(i));
            assertEquals(9, f.getY(i));
        }
    }

    @Test
    void testConstructorFunctionWithXFromGreaterThanXTo() {
        MathFunction source = x -> 2 * x;
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(source, 10, 5, 2);

        assertEquals(2, f.getCount());
        assertEquals(5, f.getX(0));
        assertEquals(10, f.getX(1));
        assertEquals(10, f.getY(0));
        assertEquals(20, f.getY(1));
    }

    @Test
    void testFloorIndexOfXEdgeCases() {
        double[] x = {2, 4, 6};
        double[] y = {4, 8, 12};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(x, y);

        assertEquals(0, f.floorIndexOfX(1));
        assertEquals(3, f.floorIndexOfX(7));
    }

    @Test
    void testFloorNodeOfXBoundaries() {
        double[] x = {2, 4, 6};
        double[] y = {4, 8, 12};
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(x, y);

        assertEquals(2, f.apply(1), 0.001);
        assertEquals(14, f.apply(7), 0.001);
        assertEquals(12, f.apply(6), 0.001);
    }

}