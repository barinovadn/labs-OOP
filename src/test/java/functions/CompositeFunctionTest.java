package functions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CompositeFunctionTest {

    @Test
    void testSimpleComposite() {
        MathFunction f = x -> x * 2;
        MathFunction g = x -> x + 1;
        CompositeFunction h = new CompositeFunction(f, g);

        assertEquals(5.0, h.apply(2.0));
    }

    @Test
    void testSameFunction() {
        MathFunction square = x -> x * x;
        CompositeFunction fourthPower = new CompositeFunction(square, square);

        assertEquals(16.0, fourthPower.apply(2.0));
    }

    @Test
    void testNestedComposite() {
        MathFunction f = x -> x + 1;
        MathFunction g = x -> x * 2;
        MathFunction h = x -> x * x;

        CompositeFunction comp1 = new CompositeFunction(f, g);
        CompositeFunction comp2 = new CompositeFunction(comp1, h);

        assertEquals(36.0, comp2.apply(2.0));
    }

    @Test
    void testWithIdentity() {
        MathFunction identity = new IdentityFunction();
        MathFunction square = x -> x * x;
        CompositeFunction comp = new CompositeFunction(identity, square);

        assertEquals(9.0, comp.apply(3.0));
    }
}