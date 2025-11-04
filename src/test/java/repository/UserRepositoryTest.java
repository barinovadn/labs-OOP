package repository;

import entity.UserEntity;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest {

    @Test
    void testUserCreation() {
        UserEntity user = new UserEntity("test", "pass", "test@test.com");
        assertNotNull(user);
        assertEquals("test", user.getUsername());
        assertEquals("pass", user.getPassword());
        assertEquals("test@test.com", user.getEmail());
        assertNotNull(user.getCreatedAt());
    }

    @Test
    void testSetters() {
        UserEntity user = new UserEntity();
        user.setUserId(1L);
        user.setUsername("user");
        user.setPassword("pwd");
        user.setEmail("email@test.com");
        LocalDateTime now = LocalDateTime.now();
        user.setCreatedAt(now);
        user.setFunctions(new ArrayList<>());
        user.setCompositeFunctions(new ArrayList<>());

        assertEquals(1L, user.getUserId());
        assertEquals("user", user.getUsername());
        assertEquals("pwd", user.getPassword());
        assertEquals("email@test.com", user.getEmail());
        assertEquals(now, user.getCreatedAt());
        assertNotNull(user.getFunctions());
        assertNotNull(user.getCompositeFunctions());
    }

    @Test
    void testConstructor() {
        UserEntity user = new UserEntity("constructor", "pass", "con@test.com");
        assertEquals("constructor", user.getUsername());
        assertEquals("pass", user.getPassword());
        assertEquals("con@test.com", user.getEmail());
        assertNotNull(user.getCreatedAt());
    }
}