package io.castelo.main_server.integration_tests.test_classes;

import io.castelo.main_server.auth.jwt.AuthTokenResponse;
import io.castelo.main_server.auth.jwt.JWTService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Objects;

public class AuthenticationIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private ApplicationContext context;

    @Test
    void loginWithInvalidCredentialsShouldReturnUnauthorized() {
        ResponseEntity<AuthTokenResponse> response = restTemplate.withBasicAuth("invalidUsername", "invalidPassword").postForEntity("/login", null, AuthTokenResponse.class);
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void logoutShouldReturnNoContent() {
        ResponseEntity<Void> response = restTemplate.postForEntity("/logout", null, Void.class);
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void loginWithValidCredentialsShouldReturnValidJWToken() {
        ResponseEntity<AuthTokenResponse> response = restTemplate.withBasicAuth(USERNAME, PASSWORD).postForEntity("/login", null, AuthTokenResponse.class);
        String accessToken = Objects.requireNonNull(response.getBody()).accessToken();
        String username = jwtService.extractUserName(accessToken);
        UserDetails userDetails = context.getBean(UserDetailsService.class).loadUserByUsername(username);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertTrue(jwtService.validateToken(accessToken, userDetails));
    }
}