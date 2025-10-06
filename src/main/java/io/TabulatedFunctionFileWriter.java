package io;

import functions.ArrayTabulatedFunction;
import functions.LinkedListTabulatedFunction;
import functions.TabulatedFunction;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class TabulatedFunctionFileWriter {
    public static void main(String[] args) {
        try (FileWriter arrayWriter = new FileWriter("output/array function.txt");
             FileWriter listWriter = new FileWriter("output/linked list function.txt");
             BufferedWriter bufferedArrayWriter = new BufferedWriter(arrayWriter);
             BufferedWriter bufferedListWriter = new BufferedWriter(listWriter)) {

            double[] xValues = {0.0, 0.5, 1.0, 1.5, 2.0};
            double[] yValues = {0.0, 0.25, 1.0, 2.25, 4.0};

            TabulatedFunction arrayFunction = new ArrayTabulatedFunction(xValues, yValues);
            TabulatedFunction listFunction = new LinkedListTabulatedFunction(xValues, yValues);

            FunctionsIO.writeTabulatedFunction(bufferedArrayWriter, arrayFunction);
            FunctionsIO.writeTabulatedFunction(bufferedListWriter, listFunction);

        } catch (IOException e) {
            e.printStackTrace(); // "The 1% of exceptions causes all of our test coverage problems" - Asmongold
        }
    }
}