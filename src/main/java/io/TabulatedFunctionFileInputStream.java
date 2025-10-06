package io;

import functions.TabulatedFunction;
import operations.TabulatedDifferentialOperator;
import functions.factory.ArrayTabulatedFunctionFactory;
import functions.factory.LinkedListTabulatedFunctionFactory;
import functions.factory.TabulatedFunctionFactory;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;

public class TabulatedFunctionFileInputStream {

    public static TabulatedFunction readFromBinaryFile(String filename) throws IOException {
        try (FileInputStream fileStream = new FileInputStream(filename);
             BufferedInputStream bufferedStream = new BufferedInputStream(fileStream)) {

            TabulatedFunctionFactory arrayFactory = new ArrayTabulatedFunctionFactory();
            return FunctionsIO.readTabulatedFunction(bufferedStream, arrayFactory);
        }
    }

    public static TabulatedFunction readFromConsole() throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(System.in);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        TabulatedFunctionFactory listFactory = new LinkedListTabulatedFunctionFactory();
        return FunctionsIO.readTabulatedFunction(bufferedReader, listFactory);
    }

    public static TabulatedFunction calculateDerivative(TabulatedFunction function) {
        TabulatedDifferentialOperator operator = new TabulatedDifferentialOperator();
        return operator.derive(function);
    }

    public static void printFunction(String label, TabulatedFunction function) {
        System.out.println(label);
        System.out.println(function);
    }

    public static void main(String[] args) {
        try {
            TabulatedFunction arrayFunction = readFromBinaryFile("input/binary function.bin");
            printFunction("Function from binary file:", arrayFunction);
        } catch (IOException e) {
            e.printStackTrace(); // "The 2% of exceptions cause all of our test coverage problems" - Asmongold
        }

        System.out.println("Введите размер и значения функции");

        try {
            TabulatedFunction listFunction = readFromConsole();
            TabulatedFunction derivative = calculateDerivative(listFunction);
            printFunction("Производная функции:", derivative);
        } catch (IOException e) {
            e.printStackTrace(); // "The 2% of exceptions cause all of our test coverage problems" - Asmongold
        }
    }
}