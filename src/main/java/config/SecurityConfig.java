package config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import service.CustomUserDetailsService;

import java.util.logging.Logger;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static final Logger logger = Logger.getLogger(SecurityConfig.class.getName());

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        logger.info("Configuring Spring Security with Basic Authentication");
        
        http
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
                // Public endpoints
                .antMatchers("/api/auth/register").permitAll()
                // Admin only
                .antMatchers("/api/admin/**").hasRole("ADMIN")
                .antMatchers("/api/roles/**").hasRole("ADMIN")
                // User management - requires authentication
                .antMatchers("/api/users/**").hasAnyRole("ADMIN", "USER")
                // Functions - authenticated users
                .antMatchers("/api/functions/**").hasAnyRole("ADMIN", "USER")
                .antMatchers("/api/points/**").hasAnyRole("ADMIN", "USER")
                .antMatchers("/api/composite-functions/**").hasAnyRole("ADMIN", "USER")
                // All other requests require authentication
                .anyRequest().authenticated()
            .and()
            .httpBasic()
            .and()
            .userDetailsService(userDetailsService);
        
        logger.info("Spring Security configuration completed");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // TODO Add password encoding to manual, then uncomment
        // logger.info("Creating BCryptPasswordEncoder bean");
        // return new BCryptPasswordEncoder();
        return NoOpPasswordEncoder.getInstance();
    }
}

