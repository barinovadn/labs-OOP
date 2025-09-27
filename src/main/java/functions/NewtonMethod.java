package functions;

import java.util.function.Function;

public class NewtonMethod {

    public static double apply(
            Function<Double, Double> f,  // функция
            Function<Double, Double> df, // производная
            double x0,                   // начальное приближение
            double eps,                  // точность
            int maxIterations            // максимальное число итераций
    ) {
        double x = x0;
        for (int i = 0; i < maxIterations; i++) {
            double fx = f.apply(x);
            double dfx = df.apply(x);

            if (Math.abs(dfx) < eps) break;

            double xNew = x - fx / dfx;
            if (Math.abs(xNew - x) < eps) return xNew;
            x = xNew;
        }
        return x;
    }

}