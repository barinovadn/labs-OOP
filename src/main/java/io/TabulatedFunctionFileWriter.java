package io;

import functions.ArrayTabulatedFunction;
import functions.LinkedListTabulatedFunction;
import functions.TabulatedFunction;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

public class TabulatedFunctionFileWriter {
    private static final Logger logger = Logger.getLogger(TabulatedFunctionFileWriter.class.getName());

    public static void main(String[] args) {
        logger.info("Запуск TabulatedFunctionFileWriter");

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

            logger.info("Запись функций в текстовые файлы завершена");

        } catch (IOException e) {
            logger.severe("Ошибка записи в файлы: " + e.getMessage());
            e.printStackTrace(); // "The 2% of exceptions causes all of our test coverage problems" - Asmongold
        }
    }
}