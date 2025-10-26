package com.brambilla.chamadaqr.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Conversor customizado para extrair roles do JWT do Keycloak.
 * O Keycloak armazena as roles em realm_access.roles e resource_access.
 */
@Component
public class KeycloakJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = extractAuthorities(jwt);
        return new JwtAuthenticationToken(jwt, authorities);
    }

    /**
     * Extrai as authorities do JWT do Keycloak.
     * Busca em realm_access.roles e resource_access.[client].roles
     */
    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        // Extrair roles do realm_access
        Collection<String> realmRoles = extractRealmRoles(jwt);

        // Extrair roles do resource_access (específicas do cliente)
        Collection<String> resourceRoles = extractResourceRoles(jwt);

        // Combinar todas as roles
        List<String> allRoles = new java.util.ArrayList<>();
        allRoles.addAll(realmRoles);
        allRoles.addAll(resourceRoles);

        // Converter para GrantedAuthority
        // Adiciona as roles COM e SEM o prefixo ROLE_ para máxima compatibilidade
        return allRoles.stream()
                .flatMap(role -> {
                    List<GrantedAuthority> auths = new java.util.ArrayList<>();
                    auths.add(new SimpleGrantedAuthority(role));
                    if (!role.startsWith("ROLE_")) {
                        auths.add(new SimpleGrantedAuthority("ROLE_" + role));
                    }
                    return auths.stream();
                })
                .collect(Collectors.toList());
    }

    /**
     * Extrai roles do claim realm_access.roles
     */
    @SuppressWarnings("unchecked")
    private Collection<String> extractRealmRoles(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        if (realmAccess != null && realmAccess.containsKey("roles")) {
            return (Collection<String>) realmAccess.get("roles");
        }
        return Collections.emptyList();
    }

    /**
     * Extrai roles do claim resource_access.[client_id].roles
     */
    @SuppressWarnings("unchecked")
    private Collection<String> extractResourceRoles(Jwt jwt) {
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        if (resourceAccess == null) {
            return Collections.emptyList();
        }

        List<String> roles = new java.util.ArrayList<>();

        // Iterar sobre cada cliente no resource_access
        for (Map.Entry<String, Object> entry : resourceAccess.entrySet()) {
            if (entry.getValue() instanceof Map) {
                Map<String, Object> client = (Map<String, Object>) entry.getValue();
                if (client.containsKey("roles")) {
                    Collection<String> clientRoles = (Collection<String>) client.get("roles");
                    roles.addAll(clientRoles);
                }
            }
        }

        return roles;
    }
}