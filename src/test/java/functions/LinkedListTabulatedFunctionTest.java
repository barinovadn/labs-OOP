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
    
}