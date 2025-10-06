package io;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Method;

public class ArrayTabulatedFunctionJsonSerializationTest {

    @Test
    void testMainMethodExists() throws Exception {
        Method mainMethod = ArrayTabulatedFunctionJsonSerialization.class.getMethod("main", String[].class);
        assertNotNull(mainMethod);
    }

    @Test
    void testClassIsPublic() {
        int modifiers = ArrayTabulatedFunctionJsonSerialization.class.getModifiers();
        assertTrue(java.lang.reflect.Modifier.isPublic(modifiers));
    }

    @Test
    void testMainMethodExecution() {
        assertDoesNotThrow(() -> {
            ArrayTabulatedFunctionJsonSerialization.main(new String[]{});
        });
    }
}