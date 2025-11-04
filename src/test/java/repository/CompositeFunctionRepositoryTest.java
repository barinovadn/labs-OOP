package repository;

import entity.CompositeFunctionEntity;
import entity.FunctionEntity;
import entity.UserEntity;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class CompositeFunctionRepositoryTest {

    @Test
    void testCompositeCreation() {
        UserEntity user = new UserEntity("user", "pass", "email@test.com");
        FunctionEntity first = new FunctionEntity(user, "f", "TYPE", "x", 0.0, 10.0);
        FunctionEntity second = new FunctionEntity(user, "g", "TYPE", "x^2", 0.0, 10.0);
        CompositeFunctionEntity comp = new CompositeFunctionEntity(user, "composite", first, second);

        assertNotNull(comp);
        assertEquals("composite", comp.getCompositeName());
        assertSame(user, comp.getUser());
        assertSame(first, comp.getFirstFunction());
        assertSame(second, comp.getSecondFunction());
        assertNotNull(comp.getCreatedAt());
    }

    @Test
    void testSetters() {
        CompositeFunctionEntity comp = new CompositeFunctionEntity();
        UserEntity user = new UserEntity();
        FunctionEntity first = new FunctionEntity();
        FunctionEntity second = new FunctionEntity();

        comp.setCompositeId(1L);
        comp.setUser(user);
        comp.setCompositeName("test");
        comp.setFirstFunction(first);
        comp.setSecondFunction(second);
        LocalDateTime now = LocalDateTime.now();
        comp.setCreatedAt(now);

        assertEquals(1L, comp.getCompositeId());
        assertSame(user, comp.getUser());
        assertEquals("test", comp.getCompositeName());
        assertSame(first, comp.getFirstFunction());
        assertSame(second, comp.getSecondFunction());
        assertEquals(now, comp.getCreatedAt());
    }
}