package concurrent;

import functions.TabulatedFunction;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

public class IntegralCalculator {
    private static final Logger logger = Logger.getLogger(IntegralCalculator.class.getName());

    public static double calculate(TabulatedFunction function, int threads) throws Exception {
        logger.info("Начало вычисления интеграла с " + threads + " потоками");

        ExecutorService executor = Executors.newFixedThreadPool(threads);
        List<Future<Double>> results = new ArrayList<>();

        int pointsPerThread = function.getCount() / threads;
        int remainingPoints = function.getCount() % threads;

        int startIndex = 0;
        for (int i = 0; i < threads; i++) {
            int endIndex = startIndex + pointsPerThread + (i < remainingPoints ? 1 : 0);
            if (endIndex > function.getCount() - 1) endIndex = function.getCount() - 1;

            IntegralTask task = new IntegralTask(function, startIndex, endIndex);
            logger.fine("Создана задача IntegralTask для сегмента " + startIndex + " до " + endIndex);
            results.add(executor.submit(task));
            startIndex = endIndex;
        }

        double total = 0;
        for (Future<Double> result : results) {
            total += result.get();
        }

        executor.shutdown();
        logger.info("Вычисление интеграла завершено. Результат: " + total);
        return total;
    }
}