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

    @Test
    void testReadTabulatedFunctionBinary() throws IOException {
        java.io.ByteArrayOutputStream byteStream = new java.io.ByteArrayOutputStream();
        java.io.DataOutputStream dataStream = new java.io.DataOutputStream(byteStream);

        dataStream.writeInt(2);
        dataStream.writeDouble(1.0);
        dataStream.writeDouble(10.0);
        dataStream.writeDouble(2.0);
        dataStream.writeDouble(20.0);

        byte[] data = byteStream.toByteArray();
        java.io.ByteArrayInputStream inputStream = new java.io.ByteArrayInputStream(data);
        java.io.BufferedInputStream bufferedStream = new java.io.BufferedInputStream(inputStream);

        functions.factory.ArrayTabulatedFunctionFactory factory = new functions.factory.ArrayTabulatedFunctionFactory();
        functions.TabulatedFunction function = FunctionsIO.readTabulatedFunction(bufferedStream, factory);

        assertEquals(2, function.getCount());
        assertEquals(1.0, function.getX(0), 1e-9);
        assertEquals(10.0, function.getY(0), 1e-9);
        assertEquals(2.0, function.getX(1), 1e-9);
        assertEquals(20.0, function.getY(1), 1e-9);
    }

    @Test
    void testReadTabulatedFunctionParseException() {
        String invalidInput = "2\n1,5 invalid\n2,5 20,5\n";
        java.io.StringReader stringReader = new java.io.StringReader(invalidInput);
        java.io.BufferedReader bufferedReader = new java.io.BufferedReader(stringReader);

        functions.factory.ArrayTabulatedFunctionFactory factory = new functions.factory.ArrayTabulatedFunctionFactory();

        assertThrows(IOException.class, () -> {
            FunctionsIO.readTabulatedFunction(bufferedReader, factory);
        });
    }

    @Test
    void testSerializeDeserializeJson() throws IOException {
        java.io.File tempFile = new java.io.File("temp/json_test.json");
        tempFile.getParentFile().mkdirs();

        try (java.io.FileWriter fileWriter = new java.io.FileWriter(tempFile);
             java.io.BufferedWriter bufferedWriter = new java.io.BufferedWriter(fileWriter)) {

            double[] xValues = {1.0, 2.0, 3.0};
            double[] yValues = {10.0, 20.0, 30.0};
            ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

            FunctionsIO.serializeJson(bufferedWriter, function);
        }

        try (java.io.FileReader fileReader = new java.io.FileReader(tempFile);
             java.io.BufferedReader bufferedReader = new java.io.BufferedReader(fileReader)) {

            ArrayTabulatedFunction deserializedFunction = FunctionsIO.deserializeJson(bufferedReader);

            assertEquals(3, deserializedFunction.getCount());
            assertEquals(1.0, deserializedFunction.getX(0), 1e-9);
            assertEquals(10.0, deserializedFunction.getY(0), 1e-9);
            assertEquals(2.0, deserializedFunction.getX(1), 1e-9);
            assertEquals(20.0, deserializedFunction.getY(1), 1e-9);
            assertEquals(3.0, deserializedFunction.getX(2), 1e-9);
            assertEquals(30.0, deserializedFunction.getY(2), 1e-9);
        }

        tempFile.delete();
    }

    @Test
    void testSerializeDeserialize() throws IOException, ClassNotFoundException {
        java.io.File tempFile = new java.io.File("temp/serialized_test.bin");
        tempFile.getParentFile().mkdirs();

        try (java.io.FileOutputStream fileStream = new java.io.FileOutputStream(tempFile);
             java.io.BufferedOutputStream bufferedStream = new java.io.BufferedOutputStream(fileStream)) {

            double[] xValues = {1.0, 2.0, 3.0};
            double[] yValues = {10.0, 20.0, 30.0};
            TabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

            FunctionsIO.serialize(bufferedStream, function);
        }

        try (java.io.FileInputStream fileStream = new java.io.FileInputStream(tempFile);
             java.io.BufferedInputStream bufferedStream = new java.io.BufferedInputStream(fileStream)) {

            TabulatedFunction deserializedFunction = FunctionsIO.deserialize(bufferedStream);

            assertEquals(3, deserializedFunction.getCount());
            assertEquals(1.0, deserializedFunction.getX(0), 1e-9);
            assertEquals(10.0, deserializedFunction.getY(0), 1e-9);
            assertEquals(2.0, deserializedFunction.getX(1), 1e-9);
            assertEquals(20.0, deserializedFunction.getY(1), 1e-9);
            assertEquals(3.0, deserializedFunction.getX(2), 1e-9);
            assertEquals(30.0, deserializedFunction.getY(2), 1e-9);
        }

        tempFile.delete();
    }
}