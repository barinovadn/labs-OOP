package io;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import functions.TabulatedFunction;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class TabulatedFunctionFileInputStreamTest {

    @Test
    void testReadFromBinaryFile() throws IOException {
        TabulatedFunction function = TabulatedFunctionFileInputStream.readFromBinaryFile("input/binary function.bin");
        assertNotNull(function);
        assertTrue(function.getCount() > 0);
    }

    @Test
    void testCalculateDerivative() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {1.0, 4.0, 9.0};
        TabulatedFunction function = new functions.ArrayTabulatedFunction(xValues, yValues);

        TabulatedFunction derivative = TabulatedFunctionFileInputStream.calculateDerivative(function);
        assertNotNull(derivative);
        assertEquals(3, derivative.getCount());
    }

    @Test
    void testPrintFunction() {
        java.io.PrintStream originalOut = System.out;
        java.io.ByteArrayOutputStream testOut = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(testOut));

        try {
            double[] xValues = {1.0, 2.0};
            double[] yValues = {1.0, 2.0};
            TabulatedFunction function = new functions.ArrayTabulatedFunction(xValues, yValues);

            TabulatedFunctionFileInputStream.printFunction("Test function:", function);

            String output = testOut.toString();
            assertTrue(output.contains("Test function:"));
            assertTrue(output.contains("ArrayTabulatedFunction"));
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    void testReadFromConsoleWithMockInput() throws IOException {
        String input = "2\n1,0 1,0\n2,0 2,0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        TabulatedFunction function = TabulatedFunctionFileInputStream.readFromConsole();
        assertNotNull(function);
        assertEquals(2, function.getCount());
    }

    @Test
    void testMainMethod() throws IOException {
        String input = "2\n1,0 1,0\n2,0 2,0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        java.io.PrintStream originalOut = System.out;
        java.io.ByteArrayOutputStream testOut = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(testOut));

        try {
            TabulatedFunctionFileInputStream.main(new String[]{});

            String output = testOut.toString();
            assertTrue(output.contains("Function from binary file:") || output.contains("Введите размер"));
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    void testClassInstantiation() {
        TabulatedFunctionFileInputStream stream = new TabulatedFunctionFileInputStream();
        assertNotNull(stream);
    }
}