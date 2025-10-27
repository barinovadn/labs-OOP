package io;

import functions.ArrayTabulatedFunction;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

public class ArrayTabulatedFunctionJsonSerialization {
    private static final Logger logger = Logger.getLogger(ArrayTabulatedFunctionJsonSerialization.class.getName());

    public static void main(String[] args) {
        logger.info("Запуск JSON сериализации ArrayTabulatedFunction");

        try (FileWriter fileWriter = new FileWriter("output/array_function.json");
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {

            double[] xValues = {0.0, 1.0, 2.0, 3.0, 4.0};
            double[] yValues = {0.0, 1.0, 4.0, 9.0, 16.0};

            ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

            FunctionsIO.serializeJson(bufferedWriter, function);
            logger.info("JSON сериализация завершена");

        } catch (IOException e) {
            logger.severe("Ошибка при JSON сериализации: " + e.getMessage());
            e.printStackTrace(); // "The 2% of exceptions causes all of our test coverage problems" - Asmongold
        }

        try (FileReader fileReader = new FileReader("output/array_function.json");
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            ArrayTabulatedFunction deserializedFunction = FunctionsIO.deserializeJson(bufferedReader);
            logger.info("JSON десериализация завершена");

            System.out.println("Deserialized function:");
            System.out.println(deserializedFunction);

        } catch (IOException e) {
            logger.severe("Ошибка при JSON десериализации: " + e.getMessage());
            e.printStackTrace(); // "The 2% of exceptions causes all of our test coverage problems" - Asmongold
        }
    }
}