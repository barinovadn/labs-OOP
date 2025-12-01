package manual.security;

import manual.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomUserDetailsTest {

    private UserEntity testUser;
    private List<Role> testRoles;

    @BeforeEach
    void setUp() {
        testUser = new UserEntity();
        testUser.setUserId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("password123");
        testUser.setEmail("test@example.com");
        
        testRoles = Arrays.asList(Role.USER, Role.ADMIN);
    }

    @Test
    void testConstructorAndGetters() {
        CustomUserDetails userDetails = new CustomUserDetails(testUser, testRoles);
        
        assertEquals(1L, userDetails.getUserId());
        assertEquals("testuser", userDetails.getUsername());
        assertEquals("password123", userDetails.getPassword());
        assertEquals("test@example.com", userDetails.getEmail());
        assertEquals(testUser, userDetails.getUserEntity());
    }

    @Test
    void testGetAuthorities() {
        CustomUserDetails userDetails = new CustomUserDetails(testUser, testRoles);
        
        Collection<String> authorities = userDetails.getAuthorities();
        
        assertTrue(authorities.contains("ROLE_USER"));
        assertTrue(authorities.contains("ROLE_ADMIN"));
        assertEquals(2, authorities.size());
    }

    @Test
    void testGetAuthoritiesWithEmptyRoles() {
        CustomUserDetails userDetails = new CustomUserDetails(testUser, Collections.emptyList());
        
        Collection<String> authorities = userDetails.getAuthorities();
        
        assertTrue(authorities.isEmpty());
    }

    @Test
    void testGetAuthoritiesWithNullRoles() {
        CustomUserDetails userDetails = new CustomUserDetails(testUser, null);
        
        Collection<String> authorities = userDetails.getAuthorities();
        
        assertTrue(authorities.isEmpty());
    }

    @Test
    void testAccountStatus() {
        CustomUserDetails userDetails = new CustomUserDetails(testUser, testRoles);
        
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.isEnabled());
    }

    @Test
    void testHasRole() {
        CustomUserDetails userDetails = new CustomUserDetails(testUser, testRoles);
        
        assertTrue(userDetails.hasRole(Role.USER));
        assertTrue(userDetails.hasRole(Role.ADMIN));
    }

    @Test
    void testHasRoleReturnsFalseForMissingRole() {
        List<Role> userOnly = Collections.singletonList(Role.USER);
        CustomUserDetails userDetails = new CustomUserDetails(testUser, userOnly);
        
        assertTrue(userDetails.hasRole(Role.USER));
        assertFalse(userDetails.hasRole(Role.ADMIN));
    }

    @Test
    void testHasAnyRole() {
        CustomUserDetails userDetails = new CustomUserDetails(testUser, testRoles);
        
        assertTrue(userDetails.hasAnyRole(Role.USER));
        assertTrue(userDetails.hasAnyRole(Role.ADMIN));
        assertTrue(userDetails.hasAnyRole(Role.USER, Role.ADMIN));
    }

    @Test
    void testHasAnyRoleReturnsFalseWhenNoMatch() {
        List<Role> emptyRoles = Collections.emptyList();
        CustomUserDetails userDetails = new CustomUserDetails(testUser, emptyRoles);
        
        assertFalse(userDetails.hasAnyRole(Role.USER, Role.ADMIN));
    }

    @Test
    void testGetRoles() {
        CustomUserDetails userDetails = new CustomUserDetails(testUser, testRoles);
        
        List<Role> roles = userDetails.getRoles();
        
        assertEquals(2, roles.size());
        assertTrue(roles.contains(Role.USER));
        assertTrue(roles.contains(Role.ADMIN));
    }

    @Test
    void testEqualsWithSameUserId() {
        CustomUserDetails userDetails1 = new CustomUserDetails(testUser, testRoles);
        CustomUserDetails userDetails2 = new CustomUserDetails(testUser, testRoles);
        
        assertEquals(userDetails1, userDetails2);
    }

    @Test
    void testEqualsWithDifferentUserId() {
        UserEntity anotherUser = new UserEntity();
        anotherUser.setUserId(2L);
        anotherUser.setUsername("anotheruser");
        anotherUser.setPassword("pass");
        anotherUser.setEmail("another@example.com");
        
        CustomUserDetails userDetails1 = new CustomUserDetails(testUser, testRoles);
        CustomUserDetails userDetails2 = new CustomUserDetails(anotherUser, testRoles);
        
        assertNotEquals(userDetails1, userDetails2);
    }

    @Test
    void testEqualsWithNullUserId() {
        UserEntity userWithNullId = new UserEntity();
        userWithNullId.setUsername("user");
        userWithNullId.setPassword("pass");
        userWithNullId.setEmail("email@test.com");
        
        CustomUserDetails userDetails1 = new CustomUserDetails(userWithNullId, testRoles);
        CustomUserDetails userDetails2 = new CustomUserDetails(userWithNullId, testRoles);
        
        // Two users with null IDs should not be considered equal
        assertNotEquals(userDetails1, userDetails2);
    }

    @Test
    void testEqualsWithNull() {
        CustomUserDetails userDetails = new CustomUserDetails(testUser, testRoles);
        
        assertNotEquals(userDetails, null);
    }

    @Test
    void testEqualsWithDifferentClass() {
        CustomUserDetails userDetails = new CustomUserDetails(testUser, testRoles);
        
        assertNotEquals(userDetails, "not a CustomUserDetails");
    }

    @Test
    void testEqualsSameInstance() {
        CustomUserDetails userDetails = new CustomUserDetails(testUser, testRoles);
        
        assertEquals(userDetails, userDetails);
    }

    @Test
    void testHashCode() {
        CustomUserDetails userDetails1 = new CustomUserDetails(testUser, testRoles);
        CustomUserDetails userDetails2 = new CustomUserDetails(testUser, testRoles);
        
        assertEquals(userDetails1.hashCode(), userDetails2.hashCode());
    }

    @Test
    void testToString() {
        CustomUserDetails userDetails = new CustomUserDetails(testUser, testRoles);
        
        String toString = userDetails.toString();
        
        assertTrue(toString.contains("userId=1"));
        assertTrue(toString.contains("username='testuser'"));
        assertTrue(toString.contains("email='test@example.com'"));
        assertTrue(toString.contains("ROLE_USER"));
        assertTrue(toString.contains("ROLE_ADMIN"));
    }

    @Test
    void testGetUserIdWithNullEntity() {
        UserEntity nullIdUser = new UserEntity();
        CustomUserDetails userDetails = new CustomUserDetails(nullIdUser, testRoles);
        
        assertNull(userDetails.getUserId());
    }

    @Test
    void testGetEmailWithNullEntity() {
        UserEntity nullEmailUser = new UserEntity();
        nullEmailUser.setUserId(1L);
        nullEmailUser.setUsername("test");
        nullEmailUser.setPassword("pass");
        CustomUserDetails userDetails = new CustomUserDetails(nullEmailUser, testRoles);
        
        assertNull(userDetails.getEmail());
    }
}

