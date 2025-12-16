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

    public TabulatedFunction integrate(TabulatedFunction function) {
        logger.info("Начало операции интегрирования функции");
        Point[] points = asPoints(function);
        int count = points.length;
        
        if (count < 2) {
            throw new RuntimeException("Function must have at least 2 points for integration");
        }
        
        double[] xValues = new double[count];
        double[] yValues = new double[count];
        
        double cumulativeIntegral = 0.0;
        xValues[0] = points[0].x;
        yValues[0] = 0.0;
        
        for (int i = 1; i < count; i++) {
            double x1 = points[i - 1].x;
            double x2 = points[i].x;
            double y1 = points[i - 1].y;
            double y2 = points[i].y;
            
            // Трапециевидное правило для интегрирования
            double segmentIntegral = (y1 + y2) * (x2 - x1) / 2.0;
            cumulativeIntegral += segmentIntegral;
            
            xValues[i] = x2;
            yValues[i] = cumulativeIntegral;
        }
        
        logger.info("Операция интегрирования функции завершена");
        return factory.create(xValues, yValues);
    }

    public TabulatedFunction interpolateLinearly(TabulatedFunction function, double[] newXValues) {
        logger.info("Начало линейной интерполяции функции");
        Point[] points = asPoints(function);
        int count = points.length;
        
        if (count < 2) {
            throw new RuntimeException("Function must have at least 2 points for interpolation");
        }
        
        double[] yValues = new double[newXValues.length];
        
        for (int i = 0; i < newXValues.length; i++) {
            double x = newXValues[i];
            yValues[i] = interpolateAtX(points, x);
        }
        
        logger.info("Линейная интерполяция функции завершена");
        return factory.create(newXValues, yValues);
    }
    
    private double interpolateAtX(Point[] points, double x) {
        int n = points.length;
        
        // Если x меньше минимального значения, экстраполируем влево
        if (x < points[0].x) {
            if (n < 2) return points[0].y;
            return linearExtrapolate(points[0].x, points[0].y, points[1].x, points[1].y, x);
        }
        
        // Если x больше максимального значения, экстраполируем вправо
        if (x > points[n - 1].x) {
            if (n < 2) return points[n - 1].y;
            return linearExtrapolate(points[n - 2].x, points[n - 2].y, points[n - 1].x, points[n - 1].y, x);
        }
        
        // Ищем интервал для интерполяции
        for (int i = 0; i < n - 1; i++) {
            if (x >= points[i].x && x <= points[i + 1].x) {
                return linearInterpolate(points[i].x, points[i].y, points[i + 1].x, points[i + 1].y, x);
            }
        }
        
        // Если x точно равен одной из точек
        for (int i = 0; i < n; i++) {
            if (Math.abs(x - points[i].x) < EPSILON) {
                return points[i].y;
            }
        }
        
        return points[n - 1].y;
    }
    
    private double linearInterpolate(double x1, double y1, double x2, double y2, double x) {
        if (Math.abs(x2 - x1) < EPSILON) {
            return y1;
        }
        return y1 + (y2 - y1) * (x - x1) / (x2 - x1);
    }
    
    private double linearExtrapolate(double x1, double y1, double x2, double y2, double x) {
        return linearInterpolate(x1, y1, x2, y2, x);
    }
}