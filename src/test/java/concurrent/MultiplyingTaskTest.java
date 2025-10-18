package concurrent;

import functions.LinkedListTabulatedFunction;
import functions.TabulatedFunction;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.*;

public class MultiplyingTaskTest {

    @Test
    void testMultiplyingTaskConstructor() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        TabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);

        MultiplyingTask task = new MultiplyingTask(function);
        assertNotNull(task);
    }

    @Test
    void testMultiplyingTaskExecution() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        TabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            MultiplyingTask task = new MultiplyingTask(function);
            task.run();

            String output = outputStream.toString();
            assertTrue(output.contains("Thread " + Thread.currentThread().getName() + " finished task"));

            assertEquals(20.0, function.getY(0));
            assertEquals(40.0, function.getY(1));
            assertEquals(60.0, function.getY(2));
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    void testMultiplyingTaskWithThread() {
        double[] xValues = {1.0, 2.0};
        double[] yValues = {5.0, 10.0};
        TabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            MultiplyingTask task = new MultiplyingTask(function);
            Thread thread = new Thread(task);
            thread.start();

            try {
                thread.join(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            String output = outputStream.toString();
            assertTrue(output.contains("finished task"));

            assertEquals(10.0, function.getY(0));
            assertEquals(20.0, function.getY(1));
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    void testMultiplyingTaskWithNegativeValues() {
        double[] xValues = {1.0, 2.0};
        double[] yValues = {-5.0, -10.0};
        TabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);

        MultiplyingTask task = new MultiplyingTask(function);
        task.run();

        assertEquals(-10.0, function.getY(0));
        assertEquals(-20.0, function.getY(1));
    }

    @Test
    void testMultiplyingTaskWithZeroValues() {
        double[] xValues = {1.0, 2.0};
        double[] yValues = {0.0, 0.0};
        TabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);

        MultiplyingTask task = new MultiplyingTask(function);
        task.run();

        assertEquals(0.0, function.getY(0));
        assertEquals(0.0, function.getY(1));
    }
}