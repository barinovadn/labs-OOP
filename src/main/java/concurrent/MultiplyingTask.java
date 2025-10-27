package concurrent;

import functions.TabulatedFunction;
import java.util.logging.Logger;

public class MultiplyingTask implements Runnable {
    private static final Logger logger = Logger.getLogger(MultiplyingTask.class.getName());

    private final TabulatedFunction function;
    private volatile boolean completed;

    public MultiplyingTask(TabulatedFunction function) {
        this.function = function;
        this.completed = false;
    }

    public void run() {
        logger.info("Начало операции умножения функции");
        for (int i = 0; i < function.getCount(); i++) {
            synchronized (function) {
                double currentY = function.getY(i);
                function.setY(i, currentY * 2);
            }
        }
        completed = true;
        logger.info("Операция умножения функции завершена");
        System.out.println(Thread.currentThread().getName() + " finished task");
    }

    public boolean isCompleted() {
        return completed;
    }
}