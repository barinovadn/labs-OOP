package exceptions;

import java.util.logging.Logger;

public class InterpolationException extends RuntimeException {
    private static final Logger logger = Logger.getLogger(InterpolationException.class.getName());

    public InterpolationException() {
        super();
    }

    public InterpolationException(String message) {
        super(message);
        logger.severe("InterpolationException: " + message);
    }
}