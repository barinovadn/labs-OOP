package functions;

import java.util.Iterator;

public class UnmodifiableTabulatedFunction implements TabulatedFunction {
    private TabulatedFunction function;

    public UnmodifiableTabulatedFunction(TabulatedFunction function) {
        this.function = function;
    }

    public int getCount() {
        return function.getCount();
    }

    public double getX(int index) {
        return function.getX(index);
    }

    public double getY(int index) {
        return function.getY(index);
    }

    public void setY(int index, double value) {
        throw new UnsupportedOperationException();
    }

    public int indexOfX(double x) {
        return function.indexOfX(x);
    }

    public int indexOfY(double y) {
        return function.indexOfY(y);
    }

    public double leftBound() {
        return function.leftBound();
    }

    public double rightBound() {
        return function.rightBound();
    }

    public double apply(double x) {
        return function.apply(x);
    }

    public Iterator<Point> iterator() {
        return function.iterator();
    }
}