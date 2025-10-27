package concurrent;

import functions.TabulatedFunction;
import java.util.logging.Logger;

public class WriteTask implements Runnable {
    private static final Logger logger = Logger.getLogger(WriteTask.class.getName());

    private final TabulatedFunction function;
    private final double value;

    public WriteTask(TabulatedFunction function, double value) {
        this.function = function;
        this.value = value;
    }

    public void run() {
        logger.info("Начало операции записи функции");
        for (int i = 0; i < function.getCount(); i++) {
            synchronized (function) {
                function.setY(i, value);
                System.out.printf("Writing for index %d complete%n", i);
            }
        }
        logger.info("Операция записи функции завершена");
    }
}