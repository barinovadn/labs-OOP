package io;

import functions.Point;
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
import java.util.Iterator;

public final class FunctionsIO {
    private FunctionsIO() {
        throw new UnsupportedOperationException();
    }

    public static void writeTabulatedFunction(BufferedWriter writer, TabulatedFunction function) throws IOException {
        PrintWriter printWriter = new PrintWriter(writer);
        printWriter.println(function.getCount());

        Iterator<Point> iterator = function.iterator();
        while (iterator.hasNext()) {
            Point point = iterator.next();
            printWriter.printf("%f %f\n", point.x, point.y);
        }

        printWriter.flush();
    }

    public static void writeTabulatedFunction(BufferedOutputStream outputStream, TabulatedFunction function) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        dataOutputStream.writeInt(function.getCount());

        Iterator<Point> iterator = function.iterator();
        while (iterator.hasNext()) {
            Point point = iterator.next();
            dataOutputStream.writeDouble(point.x);
            dataOutputStream.writeDouble(point.y);
        }

        dataOutputStream.flush();
    }

    public static TabulatedFunction readTabulatedFunction(BufferedReader reader, TabulatedFunctionFactory factory) throws IOException {
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

            return factory.create(xValues, yValues);

        } catch (java.text.ParseException e) {
            throw new IOException("Error parsing number", e);
        }
    }

    public static TabulatedFunction readTabulatedFunction(BufferedInputStream inputStream, TabulatedFunctionFactory factory) throws IOException {
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        int count = dataInputStream.readInt();

        double[] xValues = new double[count];
        double[] yValues = new double[count];

        for (int i = 0; i < count; i++) {
            xValues[i] = dataInputStream.readDouble();
            yValues[i] = dataInputStream.readDouble();
        }

        return factory.create(xValues, yValues);
    }
}