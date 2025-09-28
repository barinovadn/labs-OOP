package functions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MathFunctionTest {

    @Test
    void testAndThen() {
        MathFunction f = x -> x + 1;
        MathFunction g = x -> x * 2;
        MathFunction h = x -> x * x;

        MathFunction composite = f.andThen(g).andThen(h);
        assertEquals(36.0, composite.apply(2.0));
    }

    @Test
    void testAndThenSingle() {
        MathFunction f = x -> x + 5;
        MathFunction g = x -> x * 2;

        MathFunction composite = f.andThen(g);
        assertEquals(14.0, composite.apply(2.0));
    }

    @Test
    void testAndThenChain() {
        MathFunction f = x -> x * 2;
        MathFunction g = x -> x + 3;
        MathFunction h = x -> x - 1;

        MathFunction composite = f.andThen(g).andThen(h);
        assertEquals(9.0, composite.apply(3.5));
    }
}
