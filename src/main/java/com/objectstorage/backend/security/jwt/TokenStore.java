package com.objectstorage.backend.security.jwt;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenStore {

    private final StringRedisTemplate redisTemplate;

    private static final String REFRESH_PREFIX   = "refresh:";
    private static final String BLACKLIST_PREFIX = "blacklist:";

    // --- Refresh Token ---

    public void saveRefreshToken(String token, UUID userId, long ttlSeconds) {
        redisTemplate.opsForValue()
                .set(REFRESH_PREFIX + token, userId.toString(), ttlSeconds, TimeUnit.SECONDS);
    }

    /** Reads the remaining TTL in seconds. Returns null if the key does not exist. */
    public Long getRefreshTokenTtlSeconds(String token) {
        Long ttl = redisTemplate.getExpire(REFRESH_PREFIX + token, TimeUnit.SECONDS);
        return (ttl == null || ttl < 0) ? null : ttl;
    }

    /**
     * Atomically fetches and deletes the refresh token (Redis GETDEL).
     * Returns the userId, or null if the token did not exist.
     * Requires Redis 6.2+ and Spring Data Redis 2.6+.
     */
    public String consumeRefreshToken(String token) {
        return redisTemplate.opsForValue().getAndDelete(REFRESH_PREFIX + token);
    }

    public String getUserIdByRefreshToken(String token) {
        return redisTemplate.opsForValue().get(REFRESH_PREFIX + token);
    }

    public void deleteRefreshToken(String token) {
        redisTemplate.delete(REFRESH_PREFIX + token);
    }

    // --- Blacklist ---

    public void blacklistAccessToken(String jti, long ttlSeconds) {
        redisTemplate.opsForValue()
                .set(BLACKLIST_PREFIX + jti, "true", ttlSeconds, TimeUnit.SECONDS);
    }

    public boolean isBlacklisted(String jti) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(BLACKLIST_PREFIX + jti));
    }
}
