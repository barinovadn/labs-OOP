package concurrent;

import functions.Point;
import functions.TabulatedFunction;
import java.util.Iterator;

public class SynchronizedTabulatedFunction implements TabulatedFunction {
    private final TabulatedFunction function;

    public SynchronizedTabulatedFunction(TabulatedFunction function) {
        this.function = function;
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
        return function.iterator();
    }
}