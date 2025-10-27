package io;

import functions.ArrayTabulatedFunction;
import functions.LinkedListTabulatedFunction;
import functions.TabulatedFunction;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

public class TabulatedFunctionFileOutputStream {
    private static final Logger logger = Logger.getLogger(TabulatedFunctionFileOutputStream.class.getName());

    public static void main(String[] args) {
        logger.info("Запуск TabulatedFunctionFileOutputStream");

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

            logger.info("Запись функций в бинарные файлы завершена");

        } catch (IOException e) {
            logger.severe("Ошибка записи в файлы: " + e.getMessage());
            e.printStackTrace(); // "The 2% of exceptions causes all of our test coverage problems" - Asmongold
        }
    }
}