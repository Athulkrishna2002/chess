package com.sachu.chessgame.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())          // ✅ correct lambda syntax
                .formLogin(form -> form.disable())     // ✅ disable login form
                .httpBasic(basic -> basic.disable())   // optional: disable HTTP Basic
                .build();
    }
}
