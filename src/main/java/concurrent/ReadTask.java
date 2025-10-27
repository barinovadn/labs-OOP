package concurrent;

import functions.TabulatedFunction;
import java.util.logging.Logger;

public class ReadTask implements Runnable {
    private static final Logger logger = Logger.getLogger(ReadTask.class.getName());

    private final TabulatedFunction function;

    public ReadTask(TabulatedFunction function) {
        this.function = function;
    }

    public void run() {
        logger.info("Начало операции чтения функции");
        for (int i = 0; i < function.getCount(); i++) {
            synchronized (function) {
                double x = function.getX(i);
                double y = function.getY(i);
                System.out.printf("After read: i = %d, x = %f, y = %f%n", i, x, y);
            }
        }
        logger.info("Операция чтения функции завершена");
    }
}