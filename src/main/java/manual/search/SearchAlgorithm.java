package manual.search;

import java.util.logging.Logger;

public enum SearchAlgorithm {
    QUICK,
    DEEP;

    private static final Logger logger = Logger.getLogger(SearchAlgorithm.class.getName());

    public static SearchAlgorithm fromString(String algorithm) {
        logger.fine("Parsing search algorithm: " + algorithm);
        try {
            return SearchAlgorithm.valueOf(algorithm.toUpperCase());
        } catch (IllegalArgumentException e) {
            logger.warning("Unknown algorithm: " + algorithm + ", defaulting to QUICK");
            return QUICK;
        }
    }
}