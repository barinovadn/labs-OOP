package concurrent;

import functions.LinkedListTabulatedFunction;
import functions.TabulatedFunction;
import functions.UnitFunction;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MultiplyingTaskExecutor {
    private static final int POLLING_INTERVAL_MS = 10; // Интервал опроса завершения потоков
    private static final int THREAD_COUNT = 10; // Количество потоков
    private static final int FUNCTION_POINTS = 1000; // Количество точек функции

    public static void main(String[] args) {
        UnitFunction sourceFunction = new UnitFunction();
        TabulatedFunction tabulatedFunction = new LinkedListTabulatedFunction(
                sourceFunction, 1, FUNCTION_POINTS, FUNCTION_POINTS);

        List<MultiplyingTask> tasks = new CopyOnWriteArrayList<>();
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < THREAD_COUNT; i++) {
            MultiplyingTask task = new MultiplyingTask(tabulatedFunction);
            tasks.add(task);
            Thread thread = new Thread(task);
            threads.add(thread);
        }

        for (Thread thread : threads) {
            thread.start();
        }

        while (!tasks.isEmpty()) {
            tasks.removeIf(MultiplyingTask::isCompleted);
            try {
                Thread.sleep(POLLING_INTERVAL_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        System.out.println(tabulatedFunction);
    }
}