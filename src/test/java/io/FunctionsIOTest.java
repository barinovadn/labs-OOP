package io;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import functions.ArrayTabulatedFunction;
import functions.TabulatedFunction;
import functions.factory.TabulatedFunctionFactory;
import java.io.StringWriter;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class FunctionsIOTest {

    @Test
    void testWriteTabulatedFunction() throws IOException {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        TabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        StringWriter stringWriter = new StringWriter();
        BufferedWriter bufferedWriter = new BufferedWriter(stringWriter);

        FunctionsIO.writeTabulatedFunction(bufferedWriter, function);

        String result = stringWriter.toString();
        String[] lines = result.split("\n");

        assertTrue(lines[1].contains("1.0") && lines[1].contains("10.0"));
        assertTrue(lines[2].contains("2.0") && lines[2].contains("20.0"));
        assertTrue(lines[3].contains("3.0") && lines[3].contains("30.0"));
    }

    @Test
    void testFunctionsIOConstructor() throws Exception {
        Constructor<FunctionsIO> constructor = FunctionsIO.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        assertThrows(InvocationTargetException.class, () -> constructor.newInstance());
    }

    @Test
    void testWriteTabulatedFunctionBinary() throws IOException {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        TabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        java.io.ByteArrayOutputStream byteStream = new java.io.ByteArrayOutputStream();
        java.io.BufferedOutputStream bufferedStream = new java.io.BufferedOutputStream(byteStream);

        FunctionsIO.writeTabulatedFunction(bufferedStream, function);

        byte[] result = byteStream.toByteArray();
        assertTrue(result.length > 0);
    }

    public static TabulatedFunction readTabulatedFunction(BufferedReader reader, TabulatedFunctionFactory factory) throws IOException {
        try {
            String countLine = reader.readLine();
            int count = Integer.parseInt(countLine);

            double[] xValues = new double[count];
            double[] yValues = new double[count];

            java.text.NumberFormat format = java.text.NumberFormat.getInstance(java.util.Locale.forLanguageTag("ru"));

            for (int i = 0; i < count; i++) {
                String line = reader.readLine();
                String[] parts = line.split(" ");

                xValues[i] = format.parse(parts[0]).doubleValue();
                yValues[i] = format.parse(parts[1]).doubleValue();
            }

            return factory.create(xValues, yValues);

        } catch (java.text.ParseException e) {
            throw new IOException("Error parsing number", e);
        }
    }

    @Test
    void testReadTabulatedFunction() throws IOException {
        String input = "3\n1,5 10,5\n2,5 20,5\n3,5 30,5\n";
        java.io.StringReader stringReader = new java.io.StringReader(input);
        java.io.BufferedReader bufferedReader = new java.io.BufferedReader(stringReader);

        functions.factory.ArrayTabulatedFunctionFactory factory = new functions.factory.ArrayTabulatedFunctionFactory();
        functions.TabulatedFunction function = FunctionsIO.readTabulatedFunction(bufferedReader, factory);

        assertEquals(3, function.getCount());
        assertEquals(1.5, function.getX(0), 1e-9);
        assertEquals(10.5, function.getY(0), 1e-9);
        assertEquals(2.5, function.getX(1), 1e-9);
        assertEquals(20.5, function.getY(1), 1e-9);
        assertEquals(3.5, function.getX(2), 1e-9);
        assertEquals(30.5, function.getY(2), 1e-9);
    }
}