package functions;

import exceptions.ArrayIsNotSortedException;
import exceptions.DifferentLengthOfArraysException;
import exceptions.InterpolationException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ExceptionsTest {

    @Test
    void testExceptionsWithMessages() {
        ArrayIsNotSortedException e1 = new ArrayIsNotSortedException("test1");
        assertEquals("test1", e1.getMessage());

        DifferentLengthOfArraysException e2 = new DifferentLengthOfArraysException("test2");
        assertEquals("test2", e2.getMessage());

        InterpolationException e3 = new InterpolationException("test3");
        assertEquals("test3", e3.getMessage());
    }

    @Test
    void testExceptionsWithoutMessages() {
        assertNotNull(new ArrayIsNotSortedException());
        assertNotNull(new DifferentLengthOfArraysException());
        assertNotNull(new InterpolationException());
    }
}