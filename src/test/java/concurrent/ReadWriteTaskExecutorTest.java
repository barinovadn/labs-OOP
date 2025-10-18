package concurrent;

import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;
import static org.junit.jupiter.api.Assertions.*;

public class ReadWriteTaskExecutorTest {

    @Test
    void testMainMethodExists() throws Exception {
        Method mainMethod = ReadWriteTaskExecutor.class.getMethod("main", String[].class);
        assertNotNull(mainMethod);
    }

    @Test
    void testClassIsPublic() {
        int modifiers = ReadWriteTaskExecutor.class.getModifiers();
        assertTrue(java.lang.reflect.Modifier.isPublic(modifiers));
    }

    @Test
    void testMainMethodExecution() {
        assertDoesNotThrow(() -> {
            ReadWriteTaskExecutor.main(new String[]{});
        });
    }

    @Test
    void testMainMethodStructure() throws Exception {
        Method mainMethod = ReadWriteTaskExecutor.class.getMethod("main", String[].class);
        assertEquals(void.class, mainMethod.getReturnType());
        assertEquals(1, mainMethod.getParameterCount());
        assertEquals(String[].class, mainMethod.getParameterTypes()[0]);
    }

    @Test
    void testClassInstantiation() {
        ReadWriteTaskExecutor executor = new ReadWriteTaskExecutor();
        assertNotNull(executor);
    }
}