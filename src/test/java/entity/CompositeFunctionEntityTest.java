package entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;

public class CompositeFunctionEntityTest {

    @Test
    void testAllGettersAndSetters() {
        CompositeFunctionEntity composite = new CompositeFunctionEntity();
        UserEntity user = new UserEntity();
        FunctionEntity firstFunc = new FunctionEntity();
        FunctionEntity secondFunc = new FunctionEntity();

        composite.setCompositeId(1L);
        composite.setUser(user);
        composite.setCompositeName("composite test");
        composite.setFirstFunction(firstFunc);
        composite.setSecondFunction(secondFunc);
        LocalDateTime now = LocalDateTime.now();
        composite.setCreatedAt(now);

        assertEquals(1L, composite.getCompositeId());
        assertSame(user, composite.getUser());
        assertEquals("composite test", composite.getCompositeName());
        assertSame(firstFunc, composite.getFirstFunction());
        assertSame(secondFunc, composite.getSecondFunction());
        assertEquals(now, composite.getCreatedAt());
    }

    @Test
    void testConstructor() {
        UserEntity user = new UserEntity("user", "pass", "email@test.com");
        FunctionEntity firstFunc = new FunctionEntity(user, "x^2", "SQR", "x*x", 0.0, 10.0);
        FunctionEntity secondFunc = new FunctionEntity(user, "x+1", "LINEAR", "x+1", 0.0, 10.0);

        CompositeFunctionEntity composite = new CompositeFunctionEntity(
                user, "composite_test", firstFunc, secondFunc
        );

        assertSame(user, composite.getUser());
        assertEquals("composite_test", composite.getCompositeName());
        assertSame(firstFunc, composite.getFirstFunction());
        assertSame(secondFunc, composite.getSecondFunction());
        assertNotNull(composite.getCreatedAt());
    }
}