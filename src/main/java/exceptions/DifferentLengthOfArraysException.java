package exceptions;

import java.util.logging.Logger;

public class DifferentLengthOfArraysException extends RuntimeException {
    private static final Logger logger = Logger.getLogger(DifferentLengthOfArraysException.class.getName());

    public DifferentLengthOfArraysException() {
        super();
    }

    public DifferentLengthOfArraysException(String message) {
        super(message);
        logger.severe("DifferentLengthOfArraysException: " + message);
    }
}