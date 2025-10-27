package io;

import functions.Point;
import functions.ArrayTabulatedFunction;
import functions.TabulatedFunction;
import functions.factory.TabulatedFunctionFactory;
import java.io.BufferedWriter;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.util.Iterator;
import java.util.logging.Logger;

public final class FunctionsIO {
    private static final Logger logger = Logger.getLogger(FunctionsIO.class.getName());

    private FunctionsIO() {
        throw new UnsupportedOperationException();
    }

    public static void writeTabulatedFunction(BufferedWriter writer, TabulatedFunction function) throws IOException {
        logger.info("Начало записи табулированной функции в текстовый файл");
        PrintWriter printWriter = new PrintWriter(writer);
        printWriter.println(function.getCount());

        Iterator<Point> iterator = function.iterator();
        while (iterator.hasNext()) {
            Point point = iterator.next();
            printWriter.printf("%f %f\n", point.x, point.y);
        }

        printWriter.flush();
        logger.info("Запись табулированной функции в текстовый файл завершена");
    }

    public static void writeTabulatedFunction(BufferedOutputStream outputStream, TabulatedFunction function) throws IOException {
        logger.info("Начало записи табулированной функции в бинарный файл");
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        dataOutputStream.writeInt(function.getCount());

        Iterator<Point> iterator = function.iterator();
        while (iterator.hasNext()) {
            Point point = iterator.next();
            dataOutputStream.writeDouble(point.x);
            dataOutputStream.writeDouble(point.y);
        }

        dataOutputStream.flush();
        logger.info("Запись табулированной функции в бинарный файл завершена");
    }

    public static void serializeXml(BufferedWriter writer, ArrayTabulatedFunction function) throws IOException {
        logger.info("Начало XML сериализации функции");
        writer.write("<ArrayTabulatedFunction>");
        writer.write("<count>" + function.getCount() + "</count>");
        for (int i = 0; i < function.getCount(); i++) {
            writer.write("<point>");
            writer.write("<x>" + function.getX(i) + "</x>");
            writer.write("<y>" + function.getY(i) + "</y>");
            writer.write("</point>");
        }
        writer.write("</ArrayTabulatedFunction>");
        writer.flush();
        logger.info("XML сериализация функции завершена");
    }

    public static ArrayTabulatedFunction deserializeXml(BufferedReader reader) throws IOException {
        logger.info("Начало XML десериализации функции");
        StringBuilder xml = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            xml.append(line);
        }

        String content = xml.toString();
        int count = Integer.parseInt(extractTag(content, "count"));

        double[] xValues = new double[count];
        double[] yValues = new double[count];

        String[] points = content.split("<point>");
        for (int i = 1; i <= count; i++) {
            String point = points[i];
            xValues[i-1] = Double.parseDouble(extractTag(point, "x"));
            yValues[i-1] = Double.parseDouble(extractTag(point, "y"));
        }

        logger.info("XML десериализация функции завершена");
        return new ArrayTabulatedFunction(xValues, yValues);
    }

    private static String extractTag(String xml, String tag) {
        int start = xml.indexOf("<" + tag + ">") + tag.length() + 2;
        int end = xml.indexOf("</" + tag + ">");
        return xml.substring(start, end);
    }

    public static TabulatedFunction readTabulatedFunction(BufferedReader reader, TabulatedFunctionFactory factory) throws IOException {
        logger.info("Начало чтения табулированной функции из текстового файла");
        try {
            String countLine = reader.readLine();
            int count = Integer.parseInt(countLine);

            double[] xValues = new double[count];
            double[] yValues = new double[count];

            java.text.NumberFormat format = java.text.NumberFormat.getInstance(java.util.Locale.forLanguageTag("ru"));

            for (int i = 0; i < count; i++) {
                String line = reader.readLine();
                String[] parts = line.split(" ");

                xValues[i] = format.parse(parts[0]).doubleValue();
                yValues[i] = format.parse(parts[1]).doubleValue();
            }

            logger.info("Чтение табулированной функции из текстового файла завершено");
            return factory.create(xValues, yValues);

        } catch (java.text.ParseException e) {
            logger.severe("Ошибка парсинга числа при чтении функции: " + e.getMessage());
            throw new IOException("Error parsing number", e);
        }
    }

    public static void serialize(BufferedOutputStream stream, TabulatedFunction function) throws IOException {
        logger.info("Начало сериализации функции");
        ObjectOutputStream objectStream = new ObjectOutputStream(stream);
        objectStream.writeObject(function);
        objectStream.flush();
        logger.info("Сериализация функции завершена");
    }

    public static void serializeJson(BufferedWriter writer, ArrayTabulatedFunction function) throws IOException {
        logger.info("Начало JSON сериализации функции");
        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
        String json = mapper.writeValueAsString(function);
        writer.write(json);
        writer.flush();
        logger.info("JSON сериализация функции завершена");
    }

    public static ArrayTabulatedFunction deserializeJson(BufferedReader reader) throws IOException {
        logger.info("Начало JSON десериализации функции");
        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
        ArrayTabulatedFunction result = mapper.readerFor(ArrayTabulatedFunction.class).readValue(reader);
        logger.info("JSON десериализация функции завершена");
        return result;
    }

    public static TabulatedFunction deserialize(BufferedInputStream stream) throws IOException, ClassNotFoundException {
        logger.info("Начало десериализации функции");
        ObjectInputStream objectStream = new ObjectInputStream(stream);
        TabulatedFunction result = (TabulatedFunction) objectStream.readObject();
        logger.info("Десериализация функции завершена");
        return result;
    }

    public static TabulatedFunction readTabulatedFunction(BufferedInputStream inputStream, TabulatedFunctionFactory factory) throws IOException {
        logger.info("Начало чтения табулированной функции из бинарного файла");
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        int count = dataInputStream.readInt();

        double[] xValues = new double[count];
        double[] yValues = new double[count];

        for (int i = 0; i < count; i++) {
            xValues[i] = dataInputStream.readDouble();
            yValues[i] = dataInputStream.readDouble();
        }

        logger.info("Чтение табулированной функции из бинарного файла завершено");
        return factory.create(xValues, yValues);
    }
}