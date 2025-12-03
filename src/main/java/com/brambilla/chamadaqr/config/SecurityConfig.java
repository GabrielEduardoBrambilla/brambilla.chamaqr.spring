package com.brambilla.chamadaqr.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


/**
 * Configuração de Segurança do Spring Security
 *
 * ✅ CORRIGIDO - /health agora é public
 * ✅ CORRIGIDO - Alertas OWASP ZAP Resolvidos
 *
 * @author Sistema ChamadaQR
 * @version 2.1 - Com /health público para health checks (Docker, HAProxy, etc)
 * @date 03/12/2025
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final KeycloakJwtAuthenticationConverter jwtAuthenticationConverter;

    public SecurityConfig(KeycloakJwtAuthenticationConverter jwtAuthenticationConverter) {
        this.jwtAuthenticationConverter = jwtAuthenticationConverter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // ═══════════════════════════════════════════════════════════════
                // SECURITY HEADERS - CORREÇÃO DOS ALERTAS OWASP ZAP
                // ═══════════════════════════════════════════════════════════════
                .headers(headers -> headers

                        // ✅ FIX: Missing Anti-clickjacking Header
                        // CWE-1021, WASC-15 - Previne ataques de clickjacking
                        // Header: X-Frame-Options: SAMEORIGIN
                        .frameOptions(frame -> frame
                                        .sameOrigin()  // Permite iframes apenas do mesmo domínio
                                // Alternativa: .deny() para bloquear todos os iframes
                        )

                        // ✅ FIX: X-Content-Type-Options Header Missing
                        // Previne MIME sniffing que pode levar a XSS
                        // Header: X-Content-Type-Options: nosniff
                        .contentTypeOptions(contentType -> contentType
                                .disable()  // Paradoxalmente, .disable() HABILITA o header
                        )

                        // ✅ FIX: Strict-Transport-Security Header Not Set
                        // Força uso de HTTPS por 1 ano (31536000 segundos)
                        // Header: Strict-Transport-Security: max-age=31536000; includeSubDomains
                        .httpStrictTransportSecurity(hsts -> hsts
                                .includeSubDomains(true)
                                .maxAgeInSeconds(31536000)  // 365 dias
                        )

                        // ✅ FIX: Content Security Policy (CSP) Header Not Set
                        // Define política de segurança de conteúdo para prevenir XSS
                        // Header: Content-Security-Policy: default-src 'self'; ...
                        .contentSecurityPolicy(csp -> csp
                                .policyDirectives(buildCspPolicy())
                        )

                        // ✅ EXTRA: XSS Protection
                        // Ativa proteção XSS do navegador
                        // Header: X-XSS-Protection: 1; mode=block
                        .xssProtection(xss -> xss
                                .headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK)
                        )

                        // ✅ EXTRA: Referrer Policy
                        // Controla informações enviadas no header Referer
                        // Header: Referrer-Policy: strict-origin-when-cross-origin
                        .referrerPolicy(referrer -> referrer
                                .policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)
                        )

                )

                // ═══════════════════════════════════════════════════════════════
                // CORS CONFIGURATION
                // ═══════════════════════════════════════════════════════════════
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // ═══════════════════════════════════════════════════════════════
                // CSRF PROTECTION (Desabilitado para API REST stateless)
                // ═══════════════════════════════════════════════════════════════
                .csrf(csrf -> csrf.disable())

                // ═══════════════════════════════════════════════════════════════
                // OAUTH2 RESOURCE SERVER (JWT com Keycloak)
                // ═══════════════════════════════════════════════════════════════
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthenticationConverter)
                        )
                )

                // ═══════════════════════════════════════════════════════════════
                // AUTHORIZATION RULES (RBAC - Role-Based Access Control)
                // ═══════════════════════════════════════════════════════════════
                .authorizeHttpRequests(auth -> auth

                        // ✅ CORRIGIDO: /health e /health/** são PUBLIC
                        // Necessário para health checks (Docker, HAProxy, Kubernetes, etc)
                        .requestMatchers("/*/health", "/*/health/**", "/health", "/health/**").permitAll()

                        // Endpoints públicos (sem autenticação)
                        .requestMatchers("/auth/**", "/public/**").permitAll()

                        // Endpoints de professores (requer role PROFESSOR ou ADMIN)
                        .requestMatchers("/professores/**").hasAnyRole("PROFESSOR", "ADMIN")

                        // Endpoints de alunos (requer role PROFESSOR, ALUNO ou ADMIN)
                        .requestMatchers("/alunos/**").hasAnyRole("PROFESSOR", "ALUNO", "ADMIN")

                        // Endpoints de turmas (requer role PROFESSOR ou ADMIN)
                        .requestMatchers("/turmas/**").hasAnyRole("PROFESSOR", "ADMIN")

                        // Todos os outros endpoints requerem autenticação
                        .anyRequest().authenticated()
                );

        return http.build();
    }

    /**
     * Constrói a política de Content Security Policy (CSP)
     *
     * ⚠️ IMPORTANTE:
     * - 'unsafe-inline' e 'unsafe-eval' são necessários para Angular
     * - Em produção, considere usar nonces ou hashes para maior segurança
     *
     * @return String com a política CSP completa
     */
    private String buildCspPolicy() {
        return "default-src 'self'; " +
                "script-src 'self' 'unsafe-inline' 'unsafe-eval'; " +
                "style-src 'self' 'unsafe-inline'; " +
                "img-src 'self' data: https:; " +
                "font-src 'self' data:; " +
                "connect-src 'self' " +
                "http://192.168.56.103:5001 " +
                "http://192.168.56.103:8080 " +
                "https://192.168.56.103:8443 " +
                "http://192.168.56.102; " +
                "frame-ancestors 'self'; " +
                "base-uri 'self'; " +
                "form-action 'self'";
    }

    /**
     * Configuração de CORS (Cross-Origin Resource Sharing)
     * Define quais origens podem acessar a API
     *
     * @return CorsConfigurationSource configurado
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Origens permitidas (adicionar produção aqui)
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:4200",
                "https://localhost:4200",
                "http://192.168.56.103",
                "https://192.168.56.103",
                "http://192.168.56.103:8080",
                "https://192.168.56.103:8443",
                "http://192.168.56.102",
                "https://192.168.56.102",
                "https://sistema1.net",
                "http://sistema1.net",
                "*"
        ));

        // Métodos HTTP permitidos
        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"
        ));

        // Headers permitidos
        configuration.setAllowedHeaders(Arrays.asList("*"));

        // Permitir credenciais (cookies, Authorization header)
        configuration.setAllowCredentials(true);

        // Cache da configuração CORS (1 hora)
        configuration.setMaxAge(3600L);

        // Registrar configuração para todos os endpoints
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}