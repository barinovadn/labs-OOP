package io;

import functions.ArrayTabulatedFunction;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;

public class ArrayTabulatedFunctionJsonSerialization {
    public static void main(String[] args) {
        try (FileWriter fileWriter = new FileWriter("output/array_function.json");
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {

            double[] xValues = {0.0, 1.0, 2.0, 3.0, 4.0};
            double[] yValues = {0.0, 1.0, 4.0, 9.0, 16.0};

            ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

            FunctionsIO.serializeJson(bufferedWriter, function);

        } catch (IOException e) {
            e.printStackTrace(); // "The 2% of exceptions causes all of our test coverage problems" - Asmongold
        }

        try (FileReader fileReader = new FileReader("output/array_function.json");
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            ArrayTabulatedFunction deserializedFunction = FunctionsIO.deserializeJson(bufferedReader);

            System.out.println("Deserialized function:");
            System.out.println(deserializedFunction);

        } catch (IOException e) {
            e.printStackTrace(); // "The 2% of exceptions causes all of our test coverage problems" - Asmongold
        }
    }
}