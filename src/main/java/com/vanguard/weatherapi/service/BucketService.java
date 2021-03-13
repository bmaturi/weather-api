package com.vanguard.weatherapi.service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;

@Service
public class BucketService {

    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    public BucketService() {
        Bucket b = Bucket4j.builder().addLimit(Bandwidth.classic(5, Refill.intervally(5, Duration.ofHours(1)))).build();
        cache.put("token-1", b);
        b = Bucket4j.builder().addLimit(Bandwidth.classic(5, Refill.intervally(5, Duration.ofHours(1)))).build();
        cache.put("token-2", b);
        b = Bucket4j.builder().addLimit(Bandwidth.classic(5, Refill.intervally(5, Duration.ofHours(1)))).build();
        cache.put("token-3", b);
        b = Bucket4j.builder().addLimit(Bandwidth.classic(5, Refill.intervally(5, Duration.ofHours(1)))).build();
        cache.put("token-4", b);
        b = Bucket4j.builder().addLimit(Bandwidth.classic(5, Refill.intervally(5, Duration.ofHours(1)))).build();
        cache.put("token-5", b);
    }

    public Bucket resolveBucket(String token) {
        return cache.get(token);
    }
}
