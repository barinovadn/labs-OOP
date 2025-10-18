package concurrent;

import functions.LinkedListTabulatedFunction;
import functions.TabulatedFunction;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.*;

public class WriteTaskTest {

    @Test
    void testWriteTaskExecution() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        TabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            WriteTask writeTask = new WriteTask(function, 5.0);
            writeTask.run();

            String output = outputStream.toString();
            assertTrue(output.contains("Writing for index 0 complete"));
            assertTrue(output.contains("Writing for index 1 complete"));
            assertTrue(output.contains("Writing for index 2 complete"));

            assertEquals(5.0, function.getY(0));
            assertEquals(5.0, function.getY(1));
            assertEquals(5.0, function.getY(2));
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    void testWriteTaskConstructor() {
        double[] xValues = {1.0, 2.0};
        double[] yValues = {10.0, 20.0};
        TabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);

        WriteTask writeTask = new WriteTask(function, 5.0);
        assertNotNull(writeTask);
    }

    @Test
    void testWriteTaskWithDifferentValue() {
        double[] xValues = {1.0, 2.0};
        double[] yValues = {10.0, 20.0};
        TabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);

        WriteTask writeTask = new WriteTask(function, 7.5);
        writeTask.run();

        assertEquals(7.5, function.getY(0));
        assertEquals(7.5, function.getY(1));
    }
}