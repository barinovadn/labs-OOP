package functions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.io.Serializable;

public class ArrayTabulatedFunction extends AbstractTabulatedFunction implements Insertable, Removable, Serializable {
    private static final long serialVersionUID = -8306707219971993877L;

    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private double[] xValues;

    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private double[] yValues;

    private int count;

    @JsonCreator
    public ArrayTabulatedFunction(@JsonProperty("xValues") double[] xValues,
                                  @JsonProperty("yValues") double[] yValues) {
        if (xValues.length < 2) {
            throw new IllegalArgumentException("Length is less than minimum");
        }
        AbstractTabulatedFunction.checkLengthIsTheSame(xValues, yValues);
        AbstractTabulatedFunction.checkSorted(xValues);
        this.count = xValues.length;
        this.xValues = Arrays.copyOf(xValues, count);
        this.yValues = Arrays.copyOf(yValues, count);
    }

    public ArrayTabulatedFunction(MathFunction source, double xFrom, double xTo, int count) {
        if (count < 2) {
            throw new IllegalArgumentException("Length is less than minimum");
        }
        this.count = count;
        this.xValues = new double[count];
        this.yValues = new double[count];

        if (xFrom > xTo) {
            double temp = xFrom;
            xFrom = xTo;
            xTo = temp;
        }

        if (xFrom == xTo) {
            Arrays.fill(xValues, xFrom);
            double y = source.apply(xFrom);
            Arrays.fill(yValues, y);
        } else {
            double step = (xTo - xFrom) / (count - 1);
            for (int i = 0; i < count; i++) {
                xValues[i] = xFrom + i * step;
                yValues[i] = source.apply(xValues[i]);
            }
        }
    }

    public int getCount() {
        return count;
    }

    public double getX(int index) {
        if (index < 0 || index >= count) {
            throw new IllegalArgumentException("Index out of bounds");
        }
        return xValues[index];
    }

    public double getY(int index) {
        if (index < 0 || index >= count) {
            throw new IllegalArgumentException("Index out of bounds");
        }
        return yValues[index];
    }

    public void setY(int index, double value) {
        if (index < 0 || index >= count) {
            throw new IllegalArgumentException("Index out of bounds");
        }
        yValues[index] = value;
    }

    protected int floorIndexOfX(double x) {
        if (x < xValues[0]) {
            throw new IllegalArgumentException("X is less than left bound");
        }
        if (x > xValues[count - 1]) return count;

        for (int i = 0; i < count - 1; i++) {
            if (x < xValues[i + 1]) return i;
        }
        return count - 1;
    }

    public void insert(double x, double y) {
        int index = indexOfX(x);
        if (index != -1) {
            yValues[index] = y;
            return;
        }

        int insertIndex = 0;
        while (insertIndex < count && xValues[insertIndex] < x) {
            insertIndex++;
        }

        double[] newXValues = new double[count + 1];
        double[] newYValues = new double[count + 1];

        System.arraycopy(xValues, 0, newXValues, 0, insertIndex);
        System.arraycopy(yValues, 0, newYValues, 0, insertIndex);

        newXValues[insertIndex] = x;
        newYValues[insertIndex] = y;

        System.arraycopy(xValues, insertIndex, newXValues, insertIndex + 1, count - insertIndex);
        System.arraycopy(yValues, insertIndex, newYValues, insertIndex + 1, count - insertIndex);

        xValues = newXValues;
        yValues = newYValues;
        count++;
    }

    public void remove(int index) {
        if (index < 0 || index >= count) {
            throw new IllegalArgumentException("Index out of bounds");
        }
        double[] newXValues = new double[count - 1];
        double[] newYValues = new double[count - 1];

        System.arraycopy(xValues, 0, newXValues, 0, index);
        System.arraycopy(yValues, 0, newYValues, 0, index);

        System.arraycopy(xValues, index + 1, newXValues, index, count - index - 1);
        System.arraycopy(yValues, index + 1, newYValues, index, count - index - 1);

        xValues = newXValues;
        yValues = newYValues;
        count--;
    }

    public double leftBound() {
        return xValues[0];
    }

    public double rightBound() {
        return xValues[count - 1];
    }

    public int indexOfX(double x) {
        for (int i = 0; i < count; i++) {
            if (xValues[i] == x) return i;
        }
        return -1;
    }

    public int indexOfY(double y) {
        for (int i = 0; i < count; i++) {
            if (yValues[i] == y) return i;
        }
        return -1;
    }

    protected double extrapolateLeft(double x) {
        return interpolate(x, xValues[0], xValues[1], yValues[0], yValues[1]);
    }

    protected double extrapolateRight(double x) {
        return interpolate(x, xValues[count - 2], xValues[count - 1], yValues[count - 2], yValues[count - 1]);
    }

    protected double interpolate(double x, int floorIndex) {
        return interpolate(x, xValues[floorIndex], xValues[floorIndex + 1], yValues[floorIndex], yValues[floorIndex + 1]);
    }

    public Iterator<Point> iterator() {
        return new Iterator<Point>() {
            private int i = 0;

            public boolean hasNext() {
                return i < count;
            }

            public Point next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                Point point = new Point(xValues[i], yValues[i]);
                i++;
                return point;
            }
        };
    }
}