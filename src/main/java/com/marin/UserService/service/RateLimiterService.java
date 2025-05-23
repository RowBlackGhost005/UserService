package com.marin.UserService.service;

import io.github.bucket4j.Bucket;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service for managing the clients requests counts using their IPs as identifiers.
 */
@Service
public class RateLimiterService {

    private final Map<String , Bucket> cache = new ConcurrentHashMap<>();

    public Bucket resolveBucket(String identifier){
        return cache.computeIfAbsent(identifier , this::newBucket);
    }

    /**
     * Returns a bucket of the client identified by its ID
     *
     * @return Bucket of tokens of the client
     */
    private Bucket newBucket(String identifier){

        return Bucket.builder().addLimit(RateLimit.CLIENT.getLimit()).build();
    }
}
