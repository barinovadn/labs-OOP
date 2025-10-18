package io;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Method;
import java.io.File;
import java.io.*;
import functions.LinkedListTabulatedFunction;


public class LinkedListTabulatedFunctionSerializationTest {

    @Test
    void testMainMethodExists() throws Exception {
        Method mainMethod = LinkedListTabulatedFunctionSerialization.class.getMethod("main", String[].class);
        assertNotNull(mainMethod);
    }

    @Test
    void testClassIsPublic() {
        int modifiers = LinkedListTabulatedFunctionSerialization.class.getModifiers();
        assertTrue(java.lang.reflect.Modifier.isPublic(modifiers));
    }

    @Test
    void testMainMethodExecution() {
        assertDoesNotThrow(() -> {
            LinkedListTabulatedFunctionSerialization.main(new String[]{});
        });
    }

    @Test
    void testMainMethodStructure() throws Exception {
        Method mainMethod = LinkedListTabulatedFunctionSerialization.class.getMethod("main", String[].class);
        assertEquals(void.class, mainMethod.getReturnType());
        assertEquals(1, mainMethod.getParameterCount());
        assertEquals(String[].class, mainMethod.getParameterTypes()[0]);
    }

    @Test
    void testClassInstantiation() {
        LinkedListTabulatedFunctionSerialization serialization = new LinkedListTabulatedFunctionSerialization();
        assertNotNull(serialization);
    }
}