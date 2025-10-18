package io;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Method;

public class TabulatedFunctionFileOutputStreamTest {

    @Test
    void testMainMethodExists() throws Exception {
        Method mainMethod = TabulatedFunctionFileOutputStream.class.getMethod("main", String[].class);
        assertNotNull(mainMethod);
    }

    @Test
    void testMainMethodExecution() {
        assertDoesNotThrow(() -> {
            TabulatedFunctionFileOutputStream.main(new String[]{});
        });
    }

    @Test
    void testClassInstantiation() {
        TabulatedFunctionFileOutputStream stream = new TabulatedFunctionFileOutputStream();
        assertNotNull(stream);
    }
}