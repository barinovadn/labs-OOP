package security;

import entity.RoleEntity;
import entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class CustomUserDetailsTest {

    private UserEntity testUser;
    private RoleEntity userRole;
    private RoleEntity adminRole;

    @BeforeEach
    void setUp() {
        testUser = new UserEntity("testuser", "password123", "test@email.com");
        testUser.setUserId(1L);
        
        userRole = new RoleEntity("USER", "Default user role");
        userRole.setRoleId(1L);
        
        adminRole = new RoleEntity("ADMIN", "Admin role");
        adminRole.setRoleId(2L);
    }

    @Test
    void testConstructorWithRoles() {
        testUser.setRoles(Arrays.asList(userRole));
        CustomUserDetails userDetails = new CustomUserDetails(testUser);

        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
        assertEquals("password123", userDetails.getPassword());
        assertEquals(1L, userDetails.getUserId());
        assertEquals("test@email.com", userDetails.getEmail());
    }

    @Test
    void testConstructorWithMultipleRoles() {
        testUser.setRoles(Arrays.asList(userRole, adminRole));
        CustomUserDetails userDetails = new CustomUserDetails(testUser);

        assertEquals(2, userDetails.getAuthorities().size());
        assertTrue(userDetails.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
        assertTrue(userDetails.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void testConstructorWithNoRolesAssignsDefaultUserRole() {
        testUser.setRoles(Collections.emptyList());
        CustomUserDetails userDetails = new CustomUserDetails(testUser);

        assertEquals(1, userDetails.getAuthorities().size());
        assertTrue(userDetails.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void testGetAuthorities() {
        testUser.setRoles(Arrays.asList(userRole));
        CustomUserDetails userDetails = new CustomUserDetails(testUser);

        assertNotNull(userDetails.getAuthorities());
        assertEquals(1, userDetails.getAuthorities().size());
        
        GrantedAuthority authority = userDetails.getAuthorities().iterator().next();
        assertEquals("ROLE_USER", authority.getAuthority());
    }

    @Test
    void testIsAccountNonExpired() {
        testUser.setRoles(Collections.emptyList());
        CustomUserDetails userDetails = new CustomUserDetails(testUser);

        assertTrue(userDetails.isAccountNonExpired());
    }

    @Test
    void testIsAccountNonLocked() {
        testUser.setRoles(Collections.emptyList());
        CustomUserDetails userDetails = new CustomUserDetails(testUser);

        assertTrue(userDetails.isAccountNonLocked());
    }

    @Test
    void testIsCredentialsNonExpired() {
        testUser.setRoles(Collections.emptyList());
        CustomUserDetails userDetails = new CustomUserDetails(testUser);

        assertTrue(userDetails.isCredentialsNonExpired());
    }

    @Test
    void testIsEnabled() {
        testUser.setRoles(Collections.emptyList());
        CustomUserDetails userDetails = new CustomUserDetails(testUser);

        assertTrue(userDetails.isEnabled());
    }

    @Test
    void testGetUserEntity() {
        testUser.setRoles(Collections.emptyList());
        CustomUserDetails userDetails = new CustomUserDetails(testUser);

        assertSame(testUser, userDetails.getUserEntity());
    }

    @Test
    void testEqualsWithSameUser() {
        testUser.setRoles(Collections.emptyList());
        CustomUserDetails userDetails1 = new CustomUserDetails(testUser);
        CustomUserDetails userDetails2 = new CustomUserDetails(testUser);

        assertEquals(userDetails1, userDetails2);
    }

    @Test
    void testEqualsWithDifferentUser() {
        UserEntity anotherUser = new UserEntity("another", "pass", "another@email.com");
        anotherUser.setUserId(2L);
        anotherUser.setRoles(Collections.emptyList());
        testUser.setRoles(Collections.emptyList());

        CustomUserDetails userDetails1 = new CustomUserDetails(testUser);
        CustomUserDetails userDetails2 = new CustomUserDetails(anotherUser);

        assertNotEquals(userDetails1, userDetails2);
    }

    @Test
    void testEqualsWithNull() {
        testUser.setRoles(Collections.emptyList());
        CustomUserDetails userDetails = new CustomUserDetails(testUser);

        assertNotEquals(userDetails, null);
    }

    @Test
    void testEqualsWithSameInstance() {
        testUser.setRoles(Collections.emptyList());
        CustomUserDetails userDetails = new CustomUserDetails(testUser);

        assertEquals(userDetails, userDetails);
    }

    @Test
    void testEqualsWithDifferentClass() {
        testUser.setRoles(Collections.emptyList());
        CustomUserDetails userDetails = new CustomUserDetails(testUser);

        assertNotEquals(userDetails, "string");
    }

    @Test
    void testEqualsWithNullUserId() {
        UserEntity userWithNullId = new UserEntity("user", "pass", "email@test.com");
        userWithNullId.setRoles(Collections.emptyList());
        
        CustomUserDetails userDetails1 = new CustomUserDetails(userWithNullId);
        CustomUserDetails userDetails2 = new CustomUserDetails(userWithNullId);

        assertNotEquals(userDetails1, userDetails2);
    }
    
    @Test
    void testEqualsWithSameUserEntity() {
        testUser.setRoles(Collections.emptyList());
        CustomUserDetails userDetails1 = new CustomUserDetails(testUser);
        CustomUserDetails userDetails2 = new CustomUserDetails(testUser);

        assertEquals(userDetails1, userDetails2);
    }

    @Test
    void testHashCode() {
        testUser.setRoles(Collections.emptyList());
        CustomUserDetails userDetails1 = new CustomUserDetails(testUser);
        CustomUserDetails userDetails2 = new CustomUserDetails(testUser);

        assertEquals(userDetails1.hashCode(), userDetails2.hashCode());
    }

    @Test
    void testHashCodeWithNullUserId() {
        UserEntity userWithNullId = new UserEntity("user", "pass", "email@test.com");
        userWithNullId.setRoles(Collections.emptyList());
        
        CustomUserDetails userDetails = new CustomUserDetails(userWithNullId);

        assertEquals(0, userDetails.hashCode());
    }

    @Test
    void testToString() {
        testUser.setRoles(Arrays.asList(userRole));
        CustomUserDetails userDetails = new CustomUserDetails(testUser);

        String toString = userDetails.toString();

        assertTrue(toString.contains("CustomUserDetails"));
        assertTrue(toString.contains("userId=1"));
        assertTrue(toString.contains("username='testuser'"));
        assertTrue(toString.contains("email='test@email.com'"));
        assertTrue(toString.contains("ROLE_USER"));
    }

    @Test
    void testRoleNameToUpperCase() {
        RoleEntity lowercaseRole = new RoleEntity("admin", "lowercase admin");
        lowercaseRole.setRoleId(3L);
        testUser.setRoles(Arrays.asList(lowercaseRole));
        
        CustomUserDetails userDetails = new CustomUserDetails(testUser);

        assertTrue(userDetails.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }
}

