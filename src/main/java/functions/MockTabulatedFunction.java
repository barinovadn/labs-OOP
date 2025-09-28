package functions;

public class MockTabulatedFunction extends AbstractTabulatedFunction {
    private final double x0 = 1.0;
    private final double x1 = 3.0;
    private final double y0 = 2.0;
    private final double y1 = 6.0;

    public int getCount() {
        return 2;
    }

    public double getX(int index) {
        return index == 0 ? x0 : x1;
    }

    public double getY(int index) {
        return index == 0 ? y0 : y1;
    }

    public void setY(int index, double value) {}

    public int indexOfX(double x) {
        return x == x0 ? 0 : x == x1 ? 1 : -1;
    }

    public int indexOfY(double y) {
        return y == y0 ? 0 : y == y1 ? 1 : -1;
    }

    public double leftBound() {
        return x0;
    }

    public double rightBound() {
        return x1;
    }

    protected int floorIndexOfX(double x) {
        if (x < x0) return 0;
        if (x > x1) return 2;
        return x < x1 ? 0 : 1;
    }

    protected double extrapolateLeft(double x) {
        return interpolate(x, x0, x1, y0, y1);
    }

    protected double extrapolateRight(double x) {
        return interpolate(x, x0, x1, y0, y1);
    }

    protected double interpolate(double x, int floorIndex) {
        return interpolate(x, x0, x1, y0, y1);
    }
}