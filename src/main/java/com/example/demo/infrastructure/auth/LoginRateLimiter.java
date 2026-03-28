package com.example.demo.infrastructure.auth;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Per-IP token bucket: 5 login attempts per minute.
 * Note: the bucket map is unbounded — replace with a cache (e.g. Caffeine)
 * if long-term memory growth is a concern.
 */
public class LoginRateLimiter {
    private final ConcurrentMap<String, Bucket> buckets = new ConcurrentHashMap<>();

    public boolean tryConsume(String clientIp) {
        return buckets.computeIfAbsent(clientIp, k -> newBucket()).tryConsume(1);
    }

    private Bucket newBucket() {
        return Bucket.builder()
                .addLimit(Bandwidth.builder()
                        .capacity(5)
                        .refillIntervally(5, Duration.ofMinutes(1))
                        .build())
                .build();
    }
}
