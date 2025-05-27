package com.brambilla.chamadaqr.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.context.annotation.Bean;

@Configuration
@Profile("dev") // ou "test", dependendo do seu uso
public class NoSecurityConfig {

    @Bean
    public SecurityFilterChain noSecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests().anyRequest().permitAll();
        return http.build();
    }
}
