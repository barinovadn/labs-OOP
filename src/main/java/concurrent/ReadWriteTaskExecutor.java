package concurrent;

import functions.ConstantFunction;
import functions.LinkedListTabulatedFunction;
import functions.TabulatedFunction;

public class ReadWriteTaskExecutor {
    public static void main(String[] args) {
        ConstantFunction sourceFunction = new ConstantFunction(-1);
        TabulatedFunction tabulatedFunction = new LinkedListTabulatedFunction(sourceFunction, 1, 1000, 1000);

        WriteTask writeTask = new WriteTask(tabulatedFunction, 0.5);
        ReadTask readTask = new ReadTask(tabulatedFunction);

        Thread writeThread = new Thread(writeTask);
        Thread readThread = new Thread(readTask);

        writeThread.start();
        readThread.start();
    }
}