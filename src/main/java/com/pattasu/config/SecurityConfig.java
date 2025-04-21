package com.pattasu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF for testing (enable later if needed)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()  // ✅ Allow login/register/OTP
                .anyRequest().authenticated()                 // 🔒 All other endpoints are protected
            )
            .httpBasic(Customizer.withDefaults()); // Enables basic auth (temporary fallback)

        return http.build();
    }
}
