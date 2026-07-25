package com.objectstorage.backend.security.config.jwt;

import com.objectstorage.backend.modules.user.model.Role;
import com.objectstorage.backend.modules.user.model.User;
import com.objectstorage.backend.security.config.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import com.objectstorage.backend.security.jwt.JwtService;

import java.util.Base64;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;
    private CustomUserDetails user;
    private User userEntity;

    private static final String SECRET =
            Base64.getEncoder().encodeToString(
                    "my-super-secret-key-that-is-long-enough-for-hs256".getBytes()
            );

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();

        ReflectionTestUtils.setField(jwtService, "secretKey", SECRET);
        ReflectionTestUtils.setField(jwtService, "accessTokenExpiry", 60000L);
        ReflectionTestUtils.setField(jwtService, "refreshTokenExpiry", 120000L);

        userEntity = new User();
        userEntity.setId(UUID.randomUUID());
        userEntity.setEmail("test@example.com");
        userEntity.setPassword("password");
        userEntity.setRole(Role.USER);

        user = new CustomUserDetails(userEntity);
    }


    // should generate access token with username and roles
    @Test
    void shouldGenerateAccessTokenWithUsernameAndRoles() {

        String token = jwtService.generateAccessToken(user);

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(Base64.getDecoder().decode(SECRET)))
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertEquals("test@example.com", claims.getSubject());
        assertEquals(List.of("USER"), claims.get("roles"));
    }


    @Test
    void shouldGenerateRefreshTokenWithUsername() {

        String token = jwtService.generateRefreshToken(user);

        assertEquals(
                "test@example.com",
                jwtService.extractUsername(token)
        );
    }


    @Test
    void shouldExtractUsernameFromToken() {

        String token = jwtService.generateAccessToken(user);

        String username = jwtService.extractUsername(token);

        assertEquals("test@example.com", username);
    }


    @Test
    void shouldExtractClaimsFromToken() {

        String token = jwtService.generateAccessToken(user);

        List<?> roles = jwtService.extractClaim(
                token,
                claims -> claims.get("roles", List.class)
        );

        assertEquals(List.of("USER"), roles);
    }


    @Test
    void shouldReturnTrueWhenTokenIsValid() {

        String token = jwtService.generateAccessToken(user);

        boolean result = jwtService.isTokenValid(token, user);

        assertTrue(result);
    }


    @Test
    void shouldReturnFalseWhenTokenUsernameDoesNotMatchUser() {

        String token = jwtService.generateAccessToken(user);

        User anotherUserEntity = new User();
        userEntity.setId(UUID.randomUUID());
        anotherUserEntity.setEmail("another@example.com");
        anotherUserEntity.setPassword("password");
        anotherUserEntity.setRole(Role.USER);

        CustomUserDetails anotherUser = new CustomUserDetails(anotherUserEntity);

        boolean result = jwtService.isTokenValid(token, anotherUser);

        assertFalse(result);
    }


    @Test
    void shouldThrowExceptionWhenTokenIsExpired() {

        ReflectionTestUtils.setField(jwtService, "accessTokenExpiry", -1000L);

        String token = jwtService.generateAccessToken(user);

        assertThrows(
                Exception.class,
                () -> jwtService.isTokenValid(token, user)
        );
    }


    @Test
    void shouldThrowExceptionWhenTokenIsInvalid() {

        assertThrows(
                Exception.class,
                () -> jwtService.extractUsername("invalid.token")
        );
    }


    @Test
    void shouldGenerateDifferentTokensForDifferentUsers() {

        User anotherUserEntity = new User();
        anotherUserEntity.setId(UUID.randomUUID());
        anotherUserEntity.setEmail("another@example.com");
        anotherUserEntity.setPassword("password");
        anotherUserEntity.setRole(Role.USER);

        CustomUserDetails anotherUser = new CustomUserDetails(anotherUserEntity);


        String firstToken = jwtService.generateAccessToken(user);
        String secondToken = jwtService.generateAccessToken(anotherUser);

        assertNotEquals(firstToken, secondToken);
    }
}
