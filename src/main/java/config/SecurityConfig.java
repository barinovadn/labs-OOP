package config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
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
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(
                "/",
                "/index.html",
                "/template.html",
                "/favicon.ico",
                "/static/**",
                "/css/**",
                "/js/**",
                "/images/**",
                "/**/*.css",
                "/**/*.html",
                "/**/*.js",
                "/**/*.png",
                "/**/*.jpg",
                "/**/*.svg",
                "/webjars/**",
                "/error"
        );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        logger.info("Configuring Spring Security with Basic Authentication");
        
        http
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
                // Static UI - public
                .antMatchers("/", "/index.html", "/template.html", "/**/*.html",
                        "/static/**", "/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // Public endpoints
                .antMatchers("/api/auth/register").permitAll()
                // Admin only
                .antMatchers("/api/admin/**").hasRole("ADMIN")
                .antMatchers("/api/roles/**").hasRole("ADMIN")
                // User management - Auth
                .antMatchers("/api/users/**").hasAnyRole("ADMIN", "USER")
                // Functions - Auth
                .antMatchers("/api/functions/**").hasAnyRole("ADMIN", "USER")
                .antMatchers("/api/points/**").hasAnyRole("ADMIN", "USER")
                .antMatchers("/api/composite-functions/**").hasAnyRole("ADMIN", "USER")
                // Auth only
                .anyRequest().authenticated()
            .and()
            .httpBasic().authenticationEntryPoint(restAuthenticationEntryPoint())
            .and()
            .userDetailsService(userDetailsService);
        
        logger.info("Spring Security configuration completed");
    }

    @Bean
    public AuthenticationEntryPoint restAuthenticationEntryPoint() {
        return (request, response, authException) -> {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"success\":false,\"message\":\"Unauthorized\"}");
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // TODO Add password encoding to manual, then uncomment
        // logger.info("Creating BCryptPasswordEncoder bean");
        // return new BCryptPasswordEncoder();
        return NoOpPasswordEncoder.getInstance();
    }
}

