package entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserEntityTest {

    @Test
    void testAllGettersAndSetters() {
        UserEntity user = new UserEntity();

        // Тестируем все сеттеры и геттеры
        user.setUserId(1L);
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setEmail("test@email.com");
        LocalDateTime now = LocalDateTime.now();
        user.setCreatedAt(now);

        List<FunctionEntity> functions = new ArrayList<>();
        user.setFunctions(functions);

        List<CompositeFunctionEntity> compositeFunctions = new ArrayList<>();
        user.setCompositeFunctions(compositeFunctions);

        // Проверяем все геттеры
        assertEquals(1L, user.getUserId());
        assertEquals("testuser", user.getUsername());
        assertEquals("password123", user.getPassword());
        assertEquals("test@email.com", user.getEmail());
        assertEquals(now, user.getCreatedAt());
        assertEquals(functions, user.getFunctions());
        assertEquals(compositeFunctions, user.getCompositeFunctions());
    }

    @Test
    void testConstructor() {
        UserEntity user = new UserEntity("constructorUser", "pass", "constructor@test.com");

        assertEquals("constructorUser", user.getUsername());
        assertEquals("pass", user.getPassword());
        assertEquals("constructor@test.com", user.getEmail());
        assertNotNull(user.getCreatedAt());
    }
}