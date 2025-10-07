package functions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MathFunctionTest {

    @Test
    void testAndThenChain() {
        MathFunction f = x -> x * 2;
        MathFunction g = x -> x + 3;
        MathFunction h = x -> x - 1;

        MathFunction composite = f.andThen(g).andThen(h);
        assertEquals(9.0, composite.apply(3.5));
    }

}
