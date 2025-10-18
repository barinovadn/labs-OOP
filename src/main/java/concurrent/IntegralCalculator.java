package concurrent;

import functions.TabulatedFunction;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class IntegralCalculator {

    public static double calculate(TabulatedFunction function, int threads) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        List<Future<Double>> results = new ArrayList<>();

        int pointsPerThread = function.getCount() / threads;
        int remainingPoints = function.getCount() % threads;

        int startIndex = 0;
        for (int i = 0; i < threads; i++) {
            int endIndex = startIndex + pointsPerThread + (i < remainingPoints ? 1 : 0);
            if (endIndex > function.getCount() - 1) endIndex = function.getCount() - 1;

            IntegralTask task = new IntegralTask(function, startIndex, endIndex);
            results.add(executor.submit(task));
            startIndex = endIndex;
        }

        double total = 0;
        for (Future<Double> result : results) {
            total += result.get();
        }

        executor.shutdown();
        return total;
    }
}