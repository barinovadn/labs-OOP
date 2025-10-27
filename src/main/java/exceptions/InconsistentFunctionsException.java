package exceptions;

import java.util.logging.Logger;

public class InconsistentFunctionsException extends RuntimeException {
    private static final Logger logger = Logger.getLogger(InconsistentFunctionsException.class.getName());

    public InconsistentFunctionsException() {
        super();
    }

    public InconsistentFunctionsException(String message) {
        super(message);
        logger.severe("InconsistentFunctionsException: " + message);
    }
}