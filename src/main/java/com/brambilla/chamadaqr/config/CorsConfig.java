package com.brambilla.chamadaqr.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // Allow credentials
        config.setAllowCredentials(true);

        // Allow specific origins (your frontend URLs)
        config.setAllowedOrigins(Arrays.asList(
                "http://localhost:4200",
                "https://localhost:4200",
                "http://192.168.56.103",
                "https://192.168.56.103",
                "http://192.168.56.103:8080",
                "https://192.168.56.103:8443",
                "http://192.168.56.102",
                "https://192.168.56.102",
                "https://sistema1.net",
                "http://sistema1.net"
        ));

        // Allow all headers
        config.addAllowedHeader("*");

        // Allow all methods
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));

        // Max age
        config.setMaxAge(3600L);

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}