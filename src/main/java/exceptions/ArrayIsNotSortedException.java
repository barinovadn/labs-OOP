package exceptions;

import java.util.logging.Logger;

public class ArrayIsNotSortedException extends RuntimeException {
    private static final Logger logger = Logger.getLogger(ArrayIsNotSortedException.class.getName());

    public ArrayIsNotSortedException() {
        super();
    }

    public ArrayIsNotSortedException(String message) {
        super(message);
        logger.severe("ArrayIsNotSortedException: " + message);
    }
}