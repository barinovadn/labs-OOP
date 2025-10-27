package io;

import functions.TabulatedFunction;
import functions.factory.ArrayTabulatedFunctionFactory;
import functions.factory.LinkedListTabulatedFunctionFactory;
import functions.factory.TabulatedFunctionFactory;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

public class TabulatedFunctionFileReader {
    private static final Logger logger = Logger.getLogger(TabulatedFunctionFileReader.class.getName());

    public static void main(String[] args) {
        logger.info("Запуск TabulatedFunctionFileReader");

        try (FileReader fileReader1 = new FileReader("input/function.txt");
             FileReader fileReader2 = new FileReader("input/function.txt");
             BufferedReader bufferedReader1 = new BufferedReader(fileReader1);
             BufferedReader bufferedReader2 = new BufferedReader(fileReader2)) {

            TabulatedFunctionFactory arrayFactory = new ArrayTabulatedFunctionFactory();
            TabulatedFunctionFactory listFactory = new LinkedListTabulatedFunctionFactory();

            TabulatedFunction arrayFunction = FunctionsIO.readTabulatedFunction(bufferedReader1, arrayFactory);
            TabulatedFunction listFunction = FunctionsIO.readTabulatedFunction(bufferedReader2, listFactory);

            logger.info("Чтение функций из файла завершено");

            System.out.println("Array function:");
            System.out.println(arrayFunction);

            System.out.println("Linked list function:");
            System.out.println(listFunction);

        } catch (IOException e) {
            logger.severe("Ошибка чтения из файла: " + e.getMessage());
            e.printStackTrace(); // "The 2% of exceptions causes all of our test coverage problems" - Asmongold
        }
    }
}