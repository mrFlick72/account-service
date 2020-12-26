package it.valeriovaudi.familybudget.accountservice.adapters.cache;

import org.springframework.data.redis.core.ReactiveRedisTemplate;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class ReactiveCacheManager {

    private static final String CACHE_REGION = "account-service.i18n.messages";

    private final Duration ttl;
    private final ReactiveRedisTemplate reactiveRedisTemplate;

    public ReactiveCacheManager(Duration ttl, ReactiveRedisTemplate reactiveRedisTemplate) {
        this.ttl = ttl;
        this.reactiveRedisTemplate = reactiveRedisTemplate;
    }

    public <T> Mono<T> getFromCache() {
        return reactiveRedisTemplate.opsForValue().get(CACHE_REGION);
    }

    public <T> Mono<T> updateCache(T o) {
        System.out.println("ttl " + ttl);
        return reactiveRedisTemplate.opsForValue().set(CACHE_REGION, o)
                .then(reactiveRedisTemplate.expire(CACHE_REGION, ttl))
                .then(Mono.justOrEmpty(o));
    }
}
