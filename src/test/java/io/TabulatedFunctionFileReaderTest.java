package io;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Method;

public class TabulatedFunctionFileReaderTest {

    @Test
    void testMainMethodExists() throws Exception {
        Method mainMethod = TabulatedFunctionFileReader.class.getMethod("main", String[].class);
        assertNotNull(mainMethod);
    }

    @Test
    void testMainMethodExecution() {
        assertDoesNotThrow(() -> {
            TabulatedFunctionFileReader.main(new String[]{});
        });
    }

    @Test
    void testClassInstantiation() {
        TabulatedFunctionFileReader reader = new TabulatedFunctionFileReader();
        assertNotNull(reader);
    }
}