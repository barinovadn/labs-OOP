package concurrent;

import functions.TabulatedFunction;
import java.util.logging.Logger;

public class IntegralTask implements java.util.concurrent.Callable<Double> {
    private static final Logger logger = Logger.getLogger(IntegralTask.class.getName());

    private final TabulatedFunction function;
    private final int startIndex;
    private final int endIndex;

    public IntegralTask(TabulatedFunction function, int startIndex, int endIndex) {
        this.function = function;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    public Double call() {
        logger.fine("Начало вычисления сегмента интеграла с " + startIndex + " до " + endIndex);

        double sum = 0;
        for (int i = startIndex; i < endIndex; i++) {
            double x1 = function.getX(i);
            double x2 = function.getX(i + 1);
            double y1 = function.getY(i);
            double y2 = function.getY(i + 1);
            sum += (y1 + y2) * (x2 - x1) / 2;
        }

        logger.fine("Вычисление сегмента завершено. Результат: " + sum);
        return sum;
    }
}