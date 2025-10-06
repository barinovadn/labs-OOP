package io;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import functions.ArrayTabulatedFunction;
import functions.TabulatedFunction;
import java.io.StringWriter;
import java.io.BufferedWriter;
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
}