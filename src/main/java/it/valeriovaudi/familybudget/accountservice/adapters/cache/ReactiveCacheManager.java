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
        return reactiveRedisTemplate.opsForHash().get(CACHE_REGION, CACHE_REGION.hashCode());
    }

    public <T> Mono<T> updateCache(T o) {
        return reactiveRedisTemplate.opsForHash().put(CACHE_REGION, CACHE_REGION.hashCode(), o)
                .then(reactiveRedisTemplate.expire(CACHE_REGION.hashCode(), ttl))
                .then(Mono.justOrEmpty(o));
    }
}
