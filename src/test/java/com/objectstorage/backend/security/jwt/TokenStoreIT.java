package com.objectstorage.backend.security.jwt;

import com.objectstorage.backend.security.config.AbstractTest;
import com.objectstorage.backend.security.jwt.TokenStore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import static org.junit.jupiter.api.Assertions.*;


import java.util.UUID;
@SpringBootTest
public class TokenStoreIT extends AbstractTest {

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private StringRedisTemplate redisTemplate;

    String token = "refresh-token-test";
    UUID userId = UUID.randomUUID();

    @AfterEach
    void cleanup() {
        redisTemplate.delete("refresh:" + token);
    }

    @Test
    void shouldSaveAndRetrieveRefreshToken() {

        UUID userId = UUID.randomUUID();
        String token = "refresh-token";
        long ttlSeconds = 300;

        tokenStore.saveRefreshToken(token, userId, ttlSeconds);

        String storedUserId = tokenStore.getUserIdByRefreshToken(token);

        assertEquals(userId.toString(), storedUserId);
    }

    @Test
    void shouldReturnRefreshTokenTtl() {
//    should return refresh token TTL in seconds
        UUID userId = UUID.randomUUID();
        String token = "refresh-token";
        long ttlSeconds = 60;

        tokenStore.saveRefreshToken(token, userId, ttlSeconds);

        // Act
        Long remainingTtl = tokenStore.getRefreshTokenTtlSeconds(token);

        // Assert
        assertNotNull(remainingTtl);
        assertTrue(remainingTtl > 0);
        assertTrue(remainingTtl <= ttlSeconds);
    }


    @Test
    void shouldConsumeRefreshToken() {
        // Verifies that consuming a refresh token returns the user ID and removes the token from Redis

        String token = "refresh-token-test";
        UUID userId = UUID.randomUUID();

        tokenStore.saveRefreshToken(token, userId, 3600);

        String result = tokenStore.consumeRefreshToken(token);

        assertEquals(userId.toString(), result);
        assertNull(tokenStore.getUserIdByRefreshToken(token));
    }

    // Verifies that a refresh token is removed from Redis after deletion
    @Test
    void shouldDeleteRefreshToken() {

        String token = UUID.randomUUID().toString();
        UUID userId = UUID.randomUUID();

        tokenStore.saveRefreshToken(token, userId, 3600);
        tokenStore.deleteRefreshToken(token);
        assertNull(tokenStore.getUserIdByRefreshToken(token));
    }

    // Verifies that a JWT access token JTI is stored in Redis blacklist and can be detected
    @Test
    void shouldBlacklistAccessToken() {

        String jti = UUID.randomUUID().toString();
        long ttlSeconds = 300;
        tokenStore.blacklistAccessToken(jti, ttlSeconds);

        assertTrue(tokenStore.isBlacklisted(jti));
    }

    @Test
    void shouldReturnFalseForNonBlacklistedToken() {
        String jti = UUID.randomUUID().toString();

        boolean result = tokenStore.isBlacklisted(jti);

        assertFalse(result);
    }

    @Test
    void shouldReturnNullTtlWhenTokenDoesNotExist() {
        String nonExistentToken = "non-existent-token-" + UUID.randomUUID();

        Long ttl = tokenStore.getRefreshTokenTtlSeconds(nonExistentToken);

        assertNull(ttl);
    }

    @Test
    void shouldReturnNullTtlWhenTokenHasNoExpirationOrExpired() {
        String tokenWithoutTtl = "no-ttl-token-" + UUID.randomUUID();

        // Set a key directly in Redis without a TTL (TTL = -1)
        redisTemplate.opsForValue().set("refresh:" + tokenWithoutTtl, userId.toString());

        Long ttl = tokenStore.getRefreshTokenTtlSeconds(tokenWithoutTtl);

        assertNull(ttl);


        redisTemplate.delete("refresh:" + tokenWithoutTtl);
    }

    @Test
    void shouldReturnNullTtlWhenKeyHasNoExpiration() {
        String token = "no-ttl-token-" + UUID.randomUUID();

        redisTemplate.opsForValue().set("refresh:" + token, userId.toString());

        Long remainingTtl = tokenStore.getRefreshTokenTtlSeconds(token);

        assertNull(remainingTtl);

        redisTemplate.delete("refresh:" + token);
    }
}