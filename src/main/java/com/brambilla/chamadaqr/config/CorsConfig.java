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
                "http://localhost:4200",           // Angular dev server
                "http://192.168.56.103",           // Your Alpine server HTTP
                "https://192.168.56.103",          // Your Alpine server HTTPS
                "http://192.168.56.103:8080",      // Tomcat HTTP
                "https://192.168.56.103:8443",     // Tomcat HTTPS
                "https://sistema1.net",            // Your domain
                "http://sistema1.net"              // Your domain HTTP
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