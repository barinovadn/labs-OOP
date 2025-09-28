package functions;

public class BSpline {

    public static double apply(int i,            // индекс базисной функции
                               int k,            // степень сплайна
                               double t,         // параметр
                               double[] knots) { // узловой вектор
        if (k == 0) {
            return (t >= knots[i] && t < knots[i + 1]) ? 1.0 : 0.0;
        }

        double a = 0, b = 0;
        double d1 = knots[i + k] - knots[i];
        double d2 = knots[i + k + 1] - knots[i + 1];

        if (d1 != 0) a = (t - knots[i]) / d1 * apply(i, k - 1, t, knots);
        if (d2 != 0) b = (knots[i + k + 1] - t) / d2 * apply(i + 1, k - 1, t, knots);

        return a + b;
    }

}
