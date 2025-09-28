package functions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ComplexFunctionsTest {

    @Test
    void testArrayAndLinkedList() {
        double[] x1 = {0, 1, 2};
        double[] y1 = {0, 1, 4};
        ArrayTabulatedFunction arrayFunc = new ArrayTabulatedFunction(x1, y1);

        double[] x2 = {0, 1, 4};
        double[] y2 = {0, 1, 2};
        LinkedListTabulatedFunction listFunc = new LinkedListTabulatedFunction(x2, y2);

        MathFunction composite = arrayFunc.andThen(listFunc);
        assertEquals(2.0, composite.apply(2.0), 0.001);
    }

    @Test
    void testLinkedListAndArray() {
        double[] x1 = {0, 1, 2};
        double[] y1 = {0, 2, 4};
        LinkedListTabulatedFunction listFunc = new LinkedListTabulatedFunction(x1, y1);

        double[] x2 = {0, 2, 4};
        double[] y2 = {0, 1, 2};
        ArrayTabulatedFunction arrayFunc = new ArrayTabulatedFunction(x2, y2);

        MathFunction composite = listFunc.andThen(arrayFunc);
        assertEquals(1.0, composite.apply(1.0), 0.001);
    }

    @Test
    void testTabulatedAndSqrFunction() {
        double[] x = {1, 2, 3};
        double[] y = {1, 2, 3};
        ArrayTabulatedFunction tabulated = new ArrayTabulatedFunction(x, y);

        SqrFunction sqr = new SqrFunction();

        MathFunction composite = tabulated.andThen(sqr);
        assertEquals(9.0, composite.apply(3.0), 0.001);
    }

    @Test
    void testIdentityAndTabulated() {
        IdentityFunction identity = new IdentityFunction();

        double[] x = {0, 1, 2};
        double[] y = {0, 2, 4};
        LinkedListTabulatedFunction tabulated = new LinkedListTabulatedFunction(x, y);

        MathFunction composite = identity.andThen(tabulated);
        assertEquals(4.0, composite.apply(2.0), 0.001);
    }

    @Test
    void testThreeFunctionChain() {
        double[] x1 = {0, 1, 2};
        double[] y1 = {0, 1, 2};
        ArrayTabulatedFunction f1 = new ArrayTabulatedFunction(x1, y1);

        double[] x2 = {0, 1, 2};
        double[] y2 = {0, 1, 2};
        LinkedListTabulatedFunction f2 = new LinkedListTabulatedFunction(x2, y2);

        SqrFunction f3 = new SqrFunction();

        MathFunction composite = f1.andThen(f2).andThen(f3);

        assertEquals(4.0, composite.apply(2.0), 0.001);
    }

    @Test
    void testCompositeWithTabulated() {
        double[] x = {0, 1, 2};
        double[] y = {0, 1, 4};
        ArrayTabulatedFunction tabulated = new ArrayTabulatedFunction(x, y);

        MathFunction f = a -> a + 1;
        MathFunction g = a -> a * 2;

        CompositeFunction comp = new CompositeFunction(f, g);
        MathFunction composite = tabulated.andThen(comp);

        assertEquals(10.0, composite.apply(2.0), 0.001);
    }

}