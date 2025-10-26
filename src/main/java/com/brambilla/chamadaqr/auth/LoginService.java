package com.brambilla.chamadaqr.auth;

import com.brambilla.chamadaqr.config.KeycloakProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;

@Service
public class LoginService {

    @Autowired
    private KeycloakProperties keycloakProperties;

    private final WebClient webClient;

    public LoginService() {
        this.webClient = WebClient.builder().build();
    }

    public LoginResponse logar(LoginRequest loginRequest) {
        try {
            System.out.println("=== DEBUG: Tentando autenticar ===");
            System.out.println("Token URI: " + keycloakProperties.getTokenUri());
            System.out.println("Client ID: " + keycloakProperties.getClientId());
            System.out.println("Username: " + loginRequest.getUsername());

            // Preparar dados para enviar ao Keycloak
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("grant_type", "password");
            formData.add("client_id", keycloakProperties.getClientId());
            formData.add("client_secret", keycloakProperties.getClientSecret());
            formData.add("username", loginRequest.getUsername());
            formData.add("password", loginRequest.getPassword());

            // Fazer requisição ao Keycloak
            LoginResponse response = webClient.post()
                    .uri(keycloakProperties.getTokenUri())
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData(formData))
                    .retrieve()
                    .bodyToMono(LoginResponse.class)
                    .block();

            System.out.println("=== DEBUG: Autenticação bem-sucedida! ===");
            return response;

        } catch (WebClientResponseException e) {
            System.err.println("=== ERRO DO KEYCLOAK ===");
            System.err.println("Status: " + e.getStatusCode());
            System.err.println("Body: " + e.getResponseBodyAsString());

            if (e.getStatusCode().value() == 401) {
                throw new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Usuário ou senha inválidos"
                );
            }

            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Erro ao autenticar com Keycloak: " + e.getStatusCode() + " - " + e.getResponseBodyAsString()
            );
        } catch (Exception e) {
            System.err.println("=== ERRO GERAL ===");
            e.printStackTrace();
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Erro ao autenticar: " + e.getMessage()
            );
        }
    }
}