package concurrent;

import functions.TabulatedFunction;

public class MultiplyingTask implements Runnable {
    private final TabulatedFunction function;
    private volatile boolean completed;

    public MultiplyingTask(TabulatedFunction function) {
        this.function = function;
        this.completed = false;
    }

    public void run() {
        for (int i = 0; i < function.getCount(); i++) {
            synchronized (function) {
                double currentY = function.getY(i);
                function.setY(i, currentY * 2);
            }
        }
        completed = true;
        System.out.println(Thread.currentThread().getName() + " finished task");
    }

    public boolean isCompleted() {
        return completed;
    }
}