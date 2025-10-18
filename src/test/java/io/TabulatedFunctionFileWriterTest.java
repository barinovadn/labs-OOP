package io;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Method;

public class TabulatedFunctionFileWriterTest {

    @Test
    void testMainMethodExists() throws Exception {
        Method mainMethod = TabulatedFunctionFileWriter.class.getMethod("main", String[].class);
        assertNotNull(mainMethod);
    }

    @Test
    void testMainMethodAccess() {
        Method[] methods = TabulatedFunctionFileWriter.class.getDeclaredMethods();
        boolean hasMain = false;
        for (Method method : methods) {
            if (method.getName().equals("main")) {
                hasMain = true;
                assertTrue(java.lang.reflect.Modifier.isStatic(method.getModifiers()));
                assertTrue(java.lang.reflect.Modifier.isPublic(method.getModifiers()));
            }
        }
        assertTrue(hasMain);
    }

    @Test
    void testClassIsPublic() {
        int modifiers = TabulatedFunctionFileWriter.class.getModifiers();
        assertTrue(java.lang.reflect.Modifier.isPublic(modifiers));
    }

    @Test
    void testMainMethodExecution() {
        assertDoesNotThrow(() -> {
            TabulatedFunctionFileWriter.main(new String[]{});
        });
    }

    @Test
    void testMainMethodWithMock() {
        assertDoesNotThrow(() -> {
            TabulatedFunctionFileWriter.main(new String[]{});
        });
    }

    @Test
    void testMainMethodStructure() throws Exception {
        Method mainMethod = TabulatedFunctionFileWriter.class.getMethod("main", String[].class);
        assertEquals(void.class, mainMethod.getReturnType());

        Class<?>[] parameterTypes = mainMethod.getParameterTypes();
        assertEquals(1, parameterTypes.length);
        assertEquals(String[].class, parameterTypes[0]);
    }

    @Test
    void testClassInstantiation() {
        TabulatedFunctionFileWriter writer = new TabulatedFunctionFileWriter();
        assertNotNull(writer);
    }
}