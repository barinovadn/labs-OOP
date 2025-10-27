package functions;

import exceptions.ArrayIsNotSortedException;
import exceptions.DifferentLengthOfArraysException;
import java.util.logging.Logger;

public abstract class AbstractTabulatedFunction implements TabulatedFunction {
    private static final Logger logger = Logger.getLogger(AbstractTabulatedFunction.class.getName());

    protected abstract int floorIndexOfX(double x);

    protected abstract double extrapolateLeft(double x);

    protected abstract double extrapolateRight(double x);

    protected abstract double interpolate(double x, int floorIndex);

    protected double interpolate(double x, double leftX, double rightX, double leftY, double rightY) {
        return leftY + (rightY - leftY) * (x - leftX) / (rightX - leftX);
    }

    public static void checkLengthIsTheSame(double[] xValues, double[] yValues) {
        if (xValues.length != yValues.length) {
            logger.severe("Массивы имеют разную длину: xValues=" + xValues.length + ", yValues=" + yValues.length);
            throw new DifferentLengthOfArraysException("Arrays have different lengths");
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName())
                .append(" size = ")
                .append(getCount())
                .append("\n");

        for (int i = 0; i < getCount(); i++) {
            sb.append("[")
                    .append(getX(i))
                    .append("; ")
                    .append(getY(i))
                    .append("]\n");
        }

        return sb.toString();
    }

    public static void checkSorted(double[] xValues) {
        for (int i = 1; i < xValues.length; i++) {
            if (xValues[i] <= xValues[i - 1]) {
                logger.severe("Массив не отсортирован: xValues[" + (i-1) + "]=" + xValues[i-1] + ", xValues[" + i + "]=" + xValues[i]);
                throw new ArrayIsNotSortedException("Array is not sorted");
            }
        }
    }

    public double apply(double x) {
        if (x < leftBound()) {
            return extrapolateLeft(x);
        } else if (x > rightBound()) {
            return extrapolateRight(x);
        } else {
            int index = indexOfX(x);
            if (index != -1) {
                return getY(index);
            } else {
                int floorIndex = floorIndexOfX(x);
                return interpolate(x, floorIndex);
            }
        }
    }
}