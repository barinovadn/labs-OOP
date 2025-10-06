package io;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Method;

public class ArrayTabulatedFunctionXmlSerializationTest {

    @Test
    void testMainMethodExists() throws Exception {
        Method mainMethod = ArrayTabulatedFunctionXmlSerialization.class.getMethod("main", String[].class);
        assertNotNull(mainMethod);
    }

    @Test
    void testClassIsPublic() {
        int modifiers = ArrayTabulatedFunctionXmlSerialization.class.getModifiers();
        assertTrue(java.lang.reflect.Modifier.isPublic(modifiers));
    }

    @Test
    void testMainMethodExecution() {
        assertDoesNotThrow(() -> {
            ArrayTabulatedFunctionXmlSerialization.main(new String[]{});
        });
    }
}