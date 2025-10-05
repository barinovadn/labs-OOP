package functions;

import exceptions.ArrayIsNotSortedException;
import exceptions.DifferentLengthOfArraysException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AbstractTabulatedFunctionTest {

    @Test
    void testCheckLengthIsTheSame() {
        double[] x1 = {1, 2, 3};
        double[] y1 = {1, 2, 3};
        assertDoesNotThrow(() -> AbstractTabulatedFunction.checkLengthIsTheSame(x1, y1));

        double[] x2 = {1, 2, 3};
        double[] y2 = {1, 2};
        assertThrows(DifferentLengthOfArraysException.class, () -> AbstractTabulatedFunction.checkLengthIsTheSame(x2, y2));
    }

    @Test
    void testCheckSorted() {
        double[] sorted = {1, 2, 3};
        assertDoesNotThrow(() -> AbstractTabulatedFunction.checkSorted(sorted));

        double[] notSorted = {1, 3, 2};
        assertThrows(ArrayIsNotSortedException.class, () -> AbstractTabulatedFunction.checkSorted(notSorted));

        double[] withDuplicates = {1, 2, 2};
        assertThrows(ArrayIsNotSortedException.class, () -> AbstractTabulatedFunction.checkSorted(withDuplicates));
    }
}