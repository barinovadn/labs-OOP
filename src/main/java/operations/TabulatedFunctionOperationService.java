package operations;

import functions.Point;
import functions.TabulatedFunction;
import functions.factory.TabulatedFunctionFactory;
import functions.factory.ArrayTabulatedFunctionFactory;
import exceptions.InconsistentFunctionsException;
import java.util.Iterator;
import java.util.logging.Logger;

public class TabulatedFunctionOperationService {
    private static final Logger logger = Logger.getLogger(TabulatedFunctionOperationService.class.getName());
    private static final double EPSILON = 1e-9;
    private TabulatedFunctionFactory factory;

    public TabulatedFunctionOperationService() {
        this.factory = new ArrayTabulatedFunctionFactory();
    }

    public TabulatedFunctionOperationService(TabulatedFunctionFactory factory) {
        this.factory = factory;
    }

    public TabulatedFunctionFactory getFactory() {
        return factory;
    }

    public void setFactory(TabulatedFunctionFactory factory) {
        this.factory = factory;
    }

    public static Point[] asPoints(TabulatedFunction tabulatedFunction) {
        Point[] points = new Point[tabulatedFunction.getCount()];
        Iterator<Point> iterator = tabulatedFunction.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            points[i] = iterator.next();
            i++;
        }
        return points;
    }

    public TabulatedFunction multiply(TabulatedFunction a, TabulatedFunction b) {
        logger.info("Начало операции умножения функций");
        TabulatedFunction result = doOperation(a, b, new BiOperation() {
            public double apply(double u, double v) {
                return u * v;
            }
        });
        logger.info("Операция умножения функций завершена");
        return result;
    }

    public TabulatedFunction divide(TabulatedFunction a, TabulatedFunction b) {
        logger.info("Начало операции деления функций");
        TabulatedFunction result = doOperation(a, b, new BiOperation() {
            public double apply(double u, double v) {
                return u / v;
            }
        });
        logger.info("Операция деления функций завершена");
        return result;
    }

    private interface BiOperation {
        double apply(double u, double v);
    }

    private TabulatedFunction doOperation(TabulatedFunction a, TabulatedFunction b, BiOperation operation) {
        if (a.getCount() != b.getCount()) {
            logger.severe("Ошибка: функции имеют разное количество точек");
            throw new InconsistentFunctionsException("Functions have different counts");
        }

        Point[] pointsA = asPoints(a);
        Point[] pointsB = asPoints(b);
        int count = pointsA.length;

        double[] xValues = new double[count];
        double[] yValues = new double[count];

        for (int i = 0; i < count; i++) {
            if (Math.abs(pointsA[i].x - pointsB[i].x) > EPSILON) {
                logger.severe("Ошибка: значения X не совпадают");
                throw new InconsistentFunctionsException("X values don't match");
            }
            xValues[i] = pointsA[i].x;
            yValues[i] = operation.apply(pointsA[i].y, pointsB[i].y);
        }

        return factory.create(xValues, yValues);
    }

    public TabulatedFunction add(TabulatedFunction a, TabulatedFunction b) {
        logger.info("Начало операции сложения функций");
        TabulatedFunction result = doOperation(a, b, new BiOperation() {
            public double apply(double u, double v) {
                return u + v;
            }
        });
        logger.info("Операция сложения функций завершена");
        return result;
    }

    public TabulatedFunction subtract(TabulatedFunction a, TabulatedFunction b) {
        logger.info("Начало операции вычитания функций");
        TabulatedFunction result = doOperation(a, b, new BiOperation() {
            public double apply(double u, double v) {
                return u - v;
            }
        });
        logger.info("Операция вычитания функций завершена");
        return result;
    }
}