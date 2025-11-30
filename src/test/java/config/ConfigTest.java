package config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import service.CustomUserDetailsService;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ConfigTest {

    @Mock
    private CustomUserDetailsService userDetailsService;

    @InjectMocks
    private SecurityConfig securityConfig;

    @Test
    void testPasswordEncoderBean() {
        PasswordEncoder encoder = securityConfig.passwordEncoder();
        assertNotNull(encoder);
        String password = "testPassword123";
        assertEquals(password, encoder.encode(password));
    }

    @Test
    void testSecurityConfigInstantiation() {
        SecurityConfig config = new SecurityConfig();
        assertNotNull(config);
    }

    @Test
    void testPasswordEncoderMatches() {
        PasswordEncoder encoder = securityConfig.passwordEncoder();
        String rawPassword = "password123";
        String encodedPassword = encoder.encode(rawPassword);
        assertTrue(encoder.matches(rawPassword, encodedPassword));
    }

    @Test
    void testPasswordEncoderDifferentPasswords() {
        PasswordEncoder encoder = securityConfig.passwordEncoder();
        String rawPassword = "password123";
        String wrongPassword = "wrongpassword";
        String encodedPassword = encoder.encode(rawPassword);
        assertFalse(encoder.matches(wrongPassword, encodedPassword));
    }

    @Test
    void testPasswordEncoderEmptyPassword() {
        PasswordEncoder encoder = securityConfig.passwordEncoder();
        String emptyPassword = "";
        assertEquals(emptyPassword, encoder.encode(emptyPassword));
        assertTrue(encoder.matches(emptyPassword, emptyPassword));
    }

    @Test
    void testPasswordEncoderSpecialCharacters() {
        PasswordEncoder encoder = securityConfig.passwordEncoder();
        String specialPassword = "p@$$w0rd!#%^&*()";
        assertEquals(specialPassword, encoder.encode(specialPassword));
        assertTrue(encoder.matches(specialPassword, specialPassword));
    }

    @Test
    void testSecurityConfigClassAnnotations() {
        assertTrue(SecurityConfig.class.isAnnotationPresent(
            org.springframework.context.annotation.Configuration.class));
        assertTrue(SecurityConfig.class.isAnnotationPresent(
            org.springframework.security.config.annotation.web.configuration.EnableWebSecurity.class));
        assertTrue(SecurityConfig.class.isAnnotationPresent(
            org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity.class));
    }

    @Test
    void testSecurityConfigExtendsWebSecurityConfigurerAdapter() {
        assertTrue(org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter.class
            .isAssignableFrom(SecurityConfig.class));
    }

    @Test
    void testPasswordEncoderBeanAnnotation() throws NoSuchMethodException {
        assertTrue(SecurityConfig.class.getMethod("passwordEncoder")
            .isAnnotationPresent(org.springframework.context.annotation.Bean.class));
    }

    @Test
    void testConfigureMethodExists() throws NoSuchMethodException {
        Method configureMethod = SecurityConfig.class.getDeclaredMethod("configure", 
            org.springframework.security.config.annotation.web.builders.HttpSecurity.class);
        assertNotNull(configureMethod);
        assertTrue(java.lang.reflect.Modifier.isProtected(configureMethod.getModifiers()));
    }

    @Test
    void testGlobalMethodSecurityAnnotation() {
        org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity annotation = 
            SecurityConfig.class.getAnnotation(
                org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity.class);
        assertNotNull(annotation);
        assertTrue(annotation.prePostEnabled());
    }

    @Test
    void testPasswordEncoderLongPassword() {
        PasswordEncoder encoder = securityConfig.passwordEncoder();
        String longPassword = "a".repeat(1000);
        assertEquals(longPassword, encoder.encode(longPassword));
        assertTrue(encoder.matches(longPassword, longPassword));
    }

    @Test
    void testPasswordEncoderUnicodePassword() {
        PasswordEncoder encoder = securityConfig.passwordEncoder();
        String unicodePassword = "пароль密码كلمة";
        assertEquals(unicodePassword, encoder.encode(unicodePassword));
        assertTrue(encoder.matches(unicodePassword, unicodePassword));
    }
}

