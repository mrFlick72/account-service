package it.valeriovaudi.familybudget.accountservice.adapters.cache;

import org.springframework.data.redis.core.ReactiveRedisTemplate;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static it.valeriovaudi.familybudget.accountservice.adapters.string.StringSha256.toSha256;

public class ReactiveCacheManager {

    private static final String CACHE_REGION = "account-service.i18n.messages";

    private final Duration ttl;
    private final ReactiveRedisTemplate reactiveRedisTemplate;

    public ReactiveCacheManager(Duration ttl, ReactiveRedisTemplate reactiveRedisTemplate) {
        this.ttl = ttl;
        this.reactiveRedisTemplate = reactiveRedisTemplate;
    }

    public <T> Mono<T> getFromCache() {
        return reactiveRedisTemplate.opsForHash().get(CACHE_REGION, toSha256(CACHE_REGION));
    }

    public <T> Mono<T> updateCache(T o) {
        return reactiveRedisTemplate.opsForHash().put(CACHE_REGION, toSha256(CACHE_REGION), o)
                .then(reactiveRedisTemplate.expire(CACHE_REGION, ttl))
                .then(Mono.justOrEmpty(o));
    }
}
