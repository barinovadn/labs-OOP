package operations;

import functions.Point;
import functions.TabulatedFunction;
import functions.factory.TabulatedFunctionFactory;
import functions.factory.ArrayTabulatedFunctionFactory;
import concurrent.SynchronizedTabulatedFunction;
import java.util.logging.Logger;

public class TabulatedDifferentialOperator implements DifferentialOperator<TabulatedFunction> {
    private static final Logger logger = Logger.getLogger(TabulatedDifferentialOperator.class.getName());
    private TabulatedFunctionFactory factory;

    public TabulatedDifferentialOperator() {
        this.factory = new ArrayTabulatedFunctionFactory();
    }

    public TabulatedDifferentialOperator(TabulatedFunctionFactory factory) {
        this.factory = factory;
    }

    public TabulatedFunctionFactory getFactory() {
        return factory;
    }

    public void setFactory(TabulatedFunctionFactory factory) {
        this.factory = factory;
    }

    public TabulatedFunction derive(TabulatedFunction function) {
        logger.info("Начало вычисления производной табулированной функции");

        Point[] points = TabulatedFunctionOperationService.asPoints(function);
        int count = points.length;

        double[] xValues = new double[count];
        double[] yValues = new double[count];

        for (int i = 0; i < count; i++) {
            xValues[i] = points[i].x;
        }

        for (int i = 0; i < count - 1; i++) {
            yValues[i] = (points[i + 1].y - points[i].y) / (points[i + 1].x - points[i].x);
        }

        yValues[count - 1] = yValues[count - 2];

        logger.info("Вычисление производной завершено");
        return factory.create(xValues, yValues);
    }

    public TabulatedFunction deriveSynchronously(TabulatedFunction function) {
        SynchronizedTabulatedFunction syncFunction = (function instanceof SynchronizedTabulatedFunction)
                ? (SynchronizedTabulatedFunction) function
                : new SynchronizedTabulatedFunction(function);

        return syncFunction.doSynchronously(this::derive);
    }
}