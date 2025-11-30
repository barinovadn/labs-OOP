package entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class RoleEntityTest {

    @Test
    void testDefaultConstructorAndSetters() {
        RoleEntity role = new RoleEntity();
        
        role.setRoleId(1L);
        role.setRoleName("ADMIN");
        role.setDescription("Administrator role");
        
        List<UserEntity> users = new ArrayList<>();
        UserEntity user = new UserEntity("testuser", "pass", "test@email.com");
        users.add(user);
        role.setUsers(users);
        
        assertEquals(1L, role.getRoleId());
        assertEquals("ADMIN", role.getRoleName());
        assertEquals("Administrator role", role.getDescription());
        assertNotNull(role.getUsers());
        assertEquals(1, role.getUsers().size());
    }

    @Test
    void testParameterizedConstructor() {
        RoleEntity role = new RoleEntity("USER", "Default user role");
        
        assertEquals("USER", role.getRoleName());
        assertEquals("Default user role", role.getDescription());
        assertNull(role.getRoleId());
        assertNotNull(role.getUsers());
        assertTrue(role.getUsers().isEmpty());
    }

    @Test
    void testMultipleRolesWithUsers() {
        RoleEntity adminRole = new RoleEntity("ADMIN", "Admin role");
        RoleEntity userRole = new RoleEntity("USER", "User role");
        
        UserEntity user1 = new UserEntity("admin", "pass", "admin@test.com");
        UserEntity user2 = new UserEntity("user", "pass", "user@test.com");
        
        List<UserEntity> adminUsers = new ArrayList<>();
        adminUsers.add(user1);
        adminRole.setUsers(adminUsers);
        
        List<UserEntity> regularUsers = new ArrayList<>();
        regularUsers.add(user1);
        regularUsers.add(user2);
        userRole.setUsers(regularUsers);
        
        assertEquals(1, adminRole.getUsers().size());
        assertEquals(2, userRole.getUsers().size());
    }
}

