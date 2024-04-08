package org.example.springsecurityauth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class SecurityBeansConfig {
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
// (Create a PasswordEncoder bean in a separate class to avoid a circular dependency problem.
// SecurityConfig - UserDetailsService - UserDao
// Ensure that only one global bean is generated and used.)