package com.bcopstein.gatewayauth.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;
import java.security.Key;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class JwtServiceTest {

    @Autowired
    private JwtService jwtService;

    @Test
    void shouldValidateValidJwtToken() {
        String secret = "my-very-strong-secret-key-that-is-long-enough";
        Key key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        String token = Jwts.builder()
            .setSubject("user")
            .signWith(key)
            .compact();

        assertTrue(jwtService.validateToken(token));
    }

    @Test
    void shouldRejectInvalidJwtToken() {
        assertFalse(jwtService.validateToken("invalid-token"));
    }
}
