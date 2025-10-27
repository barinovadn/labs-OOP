package concurrent;

import functions.Point;
import functions.TabulatedFunction;
import operations.TabulatedFunctionOperationService;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

public class SynchronizedTabulatedFunction implements TabulatedFunction {
    private static final Logger logger = Logger.getLogger(SynchronizedTabulatedFunction.class.getName());

    private final TabulatedFunction function;

    public SynchronizedTabulatedFunction(TabulatedFunction function) {
        this.function = function;
    }

    public interface Operation<T> {
        T apply(SynchronizedTabulatedFunction function);
    }

    public synchronized <T> T doSynchronously(Operation<? extends T> operation) {
        logger.fine("Начало синхронизированной операции");
        T result = operation.apply(this);
        logger.fine("Синхронизированная операция завершена");
        return result;
    }

    public synchronized int getCount() {
        return function.getCount();
    }

    public synchronized double getX(int index) {
        return function.getX(index);
    }

    public synchronized double getY(int index) {
        return function.getY(index);
    }

    public synchronized void setY(int index, double value) {
        function.setY(index, value);
    }

    public synchronized int indexOfX(double x) {
        return function.indexOfX(x);
    }

    public synchronized int indexOfY(double y) {
        return function.indexOfY(y);
    }

    public synchronized double leftBound() {
        return function.leftBound();
    }

    public synchronized double rightBound() {
        return function.rightBound();
    }

    public synchronized double apply(double x) {
        return function.apply(x);
    }

    public synchronized Iterator<Point> iterator() {
        Point[] points = TabulatedFunctionOperationService.asPoints(function);
        return new Iterator<Point>() {
            private int index = 0;

            public boolean hasNext() {
                return index < points.length;
            }

            public Point next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return points[index++];
            }
        };
    }
}