package concurrent;

import functions.LinkedListTabulatedFunction;
import functions.TabulatedFunction;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.*;

public class ReadTaskTest {

    @Test
    void testReadTaskExecution() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        TabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            ReadTask readTask = new ReadTask(function);
            readTask.run();

            String output = outputStream.toString();
            assertTrue(output.contains("After read: i = 0, x = 1.000000, y = 10.000000"));
            assertTrue(output.contains("After read: i = 1, x = 2.000000, y = 20.000000"));
            assertTrue(output.contains("After read: i = 2, x = 3.000000, y = 30.000000"));
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    void testReadTaskConstructor() {
        double[] xValues = {1.0, 2.0};
        double[] yValues = {10.0, 20.0};
        TabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);

        ReadTask readTask = new ReadTask(function);
        assertNotNull(readTask);
    }
}