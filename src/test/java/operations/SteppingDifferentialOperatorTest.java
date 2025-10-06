package operations;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import functions.MathFunction;
import functions.SqrFunction;

public class SteppingDifferentialOperatorTest {

    @Test
    void testLeftSteppingOperator() {
        LeftSteppingDifferentialOperator operator = new LeftSteppingDifferentialOperator(0.001);
        MathFunction sqr = new SqrFunction();
        MathFunction derivative = operator.derive(sqr);

        assertEquals(2.0, derivative.apply(1.0), 0.01);
        assertEquals(4.0, derivative.apply(2.0), 0.01);
        assertEquals(6.0, derivative.apply(3.0), 0.01);
    }

    @Test
    void testRightSteppingOperator() {
        RightSteppingDifferentialOperator operator = new RightSteppingDifferentialOperator(0.001);
        MathFunction sqr = new SqrFunction();
        MathFunction derivative = operator.derive(sqr);

        assertEquals(2.0, derivative.apply(1.0), 0.01);
        assertEquals(4.0, derivative.apply(2.0), 0.01);
        assertEquals(6.0, derivative.apply(3.0), 0.01);
    }

    @Test
    void testMiddleSteppingOperator() {
        MiddleSteppingDifferentialOperator operator = new MiddleSteppingDifferentialOperator(0.001);
        MathFunction sqr = new SqrFunction();
        MathFunction derivative = operator.derive(sqr);

        assertEquals(2.0, derivative.apply(1.0), 0.01);
        assertEquals(4.0, derivative.apply(2.0), 0.01);
        assertEquals(6.0, derivative.apply(3.0), 0.01);
    }

    @Test
    void testInvalidStepInConstructor() {
        assertThrows(IllegalArgumentException.class, () -> new LeftSteppingDifferentialOperator(0));
        assertThrows(IllegalArgumentException.class, () -> new RightSteppingDifferentialOperator(-1));
        assertThrows(IllegalArgumentException.class, () -> new MiddleSteppingDifferentialOperator(Double.POSITIVE_INFINITY));
        assertThrows(IllegalArgumentException.class, () -> new LeftSteppingDifferentialOperator(Double.NaN));
    }

    @Test
    void testGetSetStep() {
        LeftSteppingDifferentialOperator operator = new LeftSteppingDifferentialOperator(0.1);
        assertEquals(0.1, operator.getStep(), 1e-9);

        operator.setStep(0.2);
        assertEquals(0.2, operator.getStep(), 1e-9);

        assertThrows(IllegalArgumentException.class, () -> operator.setStep(0));
        assertThrows(IllegalArgumentException.class, () -> operator.setStep(-0.1));
    }

    @Test
    void testConstantFunction() {
        LeftSteppingDifferentialOperator operator = new LeftSteppingDifferentialOperator(0.001);
        MathFunction constant = new MathFunction() {
            public double apply(double x) {
                return 5.0;
            }
        };

        MathFunction derivative = operator.derive(constant);
        assertEquals(0.0, derivative.apply(1.0), 1e-9);
        assertEquals(0.0, derivative.apply(2.0), 1e-9);
    }

    @Test
    void testStepValidationCoverage() {
        assertThrows(IllegalArgumentException.class, () -> new LeftSteppingDifferentialOperator(0));
        assertThrows(IllegalArgumentException.class, () -> new LeftSteppingDifferentialOperator(-0.5));
        assertThrows(IllegalArgumentException.class, () -> new LeftSteppingDifferentialOperator(Double.POSITIVE_INFINITY));
        assertThrows(IllegalArgumentException.class, () -> new LeftSteppingDifferentialOperator(Double.NEGATIVE_INFINITY));
        assertThrows(IllegalArgumentException.class, () -> new LeftSteppingDifferentialOperator(Double.NaN));

        assertThrows(IllegalArgumentException.class, () -> new RightSteppingDifferentialOperator(0));
        assertThrows(IllegalArgumentException.class, () -> new MiddleSteppingDifferentialOperator(-1));
    }

    @Test
    void testSetStepValidationCoverage() {
        LeftSteppingDifferentialOperator operator = new LeftSteppingDifferentialOperator(0.1);

        assertThrows(IllegalArgumentException.class, () -> operator.setStep(0));
        assertThrows(IllegalArgumentException.class, () -> operator.setStep(-0.001));
        assertThrows(IllegalArgumentException.class, () -> operator.setStep(Double.POSITIVE_INFINITY));
        assertThrows(IllegalArgumentException.class, () -> operator.setStep(Double.NEGATIVE_INFINITY));
        assertThrows(IllegalArgumentException.class, () -> operator.setStep(Double.NaN));
    }
}