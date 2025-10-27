package concurrent;

import functions.ConstantFunction;
import functions.LinkedListTabulatedFunction;
import functions.TabulatedFunction;
import java.util.logging.Logger;

public class ReadWriteTaskExecutor {
    private static final Logger logger = Logger.getLogger(ReadWriteTaskExecutor.class.getName());

    public static void main(String[] args) {
        logger.info("Запуск ReadWriteTaskExecutor");

        ConstantFunction sourceFunction = new ConstantFunction(-1);
        TabulatedFunction tabulatedFunction = new LinkedListTabulatedFunction(sourceFunction, 1, 1000, 1000);

        WriteTask writeTask = new WriteTask(tabulatedFunction, 0.5);
        ReadTask readTask = new ReadTask(tabulatedFunction);

        Thread writeThread = new Thread(writeTask);
        Thread readThread = new Thread(readTask);

        logger.info("Запуск потоков записи и чтения");
        writeThread.start();
        readThread.start();

        logger.info("ReadWriteTaskExecutor завершен");
    }
}