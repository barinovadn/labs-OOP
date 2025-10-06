package io;

import functions.LinkedListTabulatedFunction;
import functions.TabulatedFunction;
import operations.TabulatedDifferentialOperator;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class LinkedListTabulatedFunctionSerialization {
    public static void main(String[] args) {
        try (FileOutputStream fileStream = new FileOutputStream("output/serialized linked list functions.bin");
             BufferedOutputStream bufferedStream = new BufferedOutputStream(fileStream)) {

            double[] xValues = {0.0, 1.0, 2.0, 3.0, 4.0};
            double[] yValues = {0.0, 1.0, 4.0, 9.0, 16.0};

            LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);
            TabulatedDifferentialOperator operator = new TabulatedDifferentialOperator();

            TabulatedFunction firstDerivative = operator.derive(function);
            TabulatedFunction secondDerivative = operator.derive(firstDerivative);

            FunctionsIO.serialize(bufferedStream, function);
            FunctionsIO.serialize(bufferedStream, firstDerivative);
            FunctionsIO.serialize(bufferedStream, secondDerivative);

        } catch (IOException e) {
            e.printStackTrace(); // "The 1% of exceptions causes all of our test coverage problems" - Asmongold
        }

        try (FileInputStream fileStream = new FileInputStream("output/serialized linked list functions.bin");
             BufferedInputStream bufferedStream = new BufferedInputStream(fileStream)) {

            TabulatedFunction function = FunctionsIO.deserialize(bufferedStream);
            TabulatedFunction firstDerivative = FunctionsIO.deserialize(bufferedStream);
            TabulatedFunction secondDerivative = FunctionsIO.deserialize(bufferedStream);

            System.out.println("Original function:");
            System.out.println(function);

            System.out.println("First derivative:");
            System.out.println(firstDerivative);

            System.out.println("Second derivative:");
            System.out.println(secondDerivative);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace(); // "The 1% of exceptions causes all of our test coverage problems" - Asmongold
        }
    }
}