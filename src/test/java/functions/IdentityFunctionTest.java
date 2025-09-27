package functions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class IdentityFunctionTest {
    @Test
    void testApply() {
        MathFunction identity = new IdentityFunction();
        assertEquals(0.0, identity.apply(0.0));
        assertEquals(5.0, identity.apply(5.0));
        assertEquals(-3.5, identity.apply(-3.5));
    }
}