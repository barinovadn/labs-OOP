package io;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Method;

public class ArrayTabulatedFunctionSerializationTest {

    @Test
    void testMainMethodExecution() {
        assertDoesNotThrow(() -> {
            ArrayTabulatedFunctionSerialization.main(new String[]{});
        });
    }

    @Test
    void testMainMethodStructure() throws Exception {
        Method mainMethod = ArrayTabulatedFunctionSerialization.class.getMethod("main", String[].class);
        assertEquals(void.class, mainMethod.getReturnType());
        assertEquals(1, mainMethod.getParameterCount());
        assertEquals(String[].class, mainMethod.getParameterTypes()[0]);
    }
}