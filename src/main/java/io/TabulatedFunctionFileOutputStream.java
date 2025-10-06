package io;

import functions.ArrayTabulatedFunction;
import functions.LinkedListTabulatedFunction;
import functions.TabulatedFunction;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class TabulatedFunctionFileOutputStream {
    public static void main(String[] args) {
        try (FileOutputStream arrayStream = new FileOutputStream("output/array function.bin");
             FileOutputStream listStream = new FileOutputStream("output/linked list function.bin");
             BufferedOutputStream bufferedArrayStream = new BufferedOutputStream(arrayStream);
             BufferedOutputStream bufferedListStream = new BufferedOutputStream(listStream)) {

            double[] xValues = {0.0, 0.5, 1.0, 1.5, 2.0};
            double[] yValues = {0.0, 0.25, 1.0, 2.25, 4.0};

            TabulatedFunction arrayFunction = new ArrayTabulatedFunction(xValues, yValues);
            TabulatedFunction listFunction = new LinkedListTabulatedFunction(xValues, yValues);

            FunctionsIO.writeTabulatedFunction(bufferedArrayStream, arrayFunction);
            FunctionsIO.writeTabulatedFunction(bufferedListStream, listFunction);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}