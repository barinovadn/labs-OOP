package concurrent;

import functions.LinkedListTabulatedFunction;
import functions.TabulatedFunction;
import functions.UnitFunction;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

public class MultiplyingTaskExecutor {
    private static final Logger logger = Logger.getLogger(MultiplyingTaskExecutor.class.getName());
    private static final int POLLING_INTERVAL_MS = 10;
    private static final int THREAD_COUNT = 10;
    private static final int FUNCTION_POINTS = 1000;

    public static void main(String[] args) {
        logger.info("Запуск MultiplyingTaskExecutor");

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

        logger.info("Запуск " + threads.size() + " потоков");
        for (Thread thread : threads) {
            thread.start();
        }

        while (!tasks.isEmpty()) {
            tasks.removeIf(MultiplyingTask::isCompleted);
            try {
                Thread.sleep(POLLING_INTERVAL_MS);
            } catch (InterruptedException e) {
                logger.severe("Прерывание во время опроса потоков");
                Thread.currentThread().interrupt();
                break;
            }
        }

        logger.info("Все задачи завершены");
        System.out.println(tabulatedFunction);
    }
}