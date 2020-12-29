package it.valeriovaudi.familybudget.accountservice.adapters.cache;

import org.junit.jupiter.api.*;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Map;

import static it.valeriovaudi.familybudget.accountservice.TestingFixture.i18nsMessage;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ReactiveCacheManagerTest {
    ReactiveRedisTemplate reactiveRedisTemplate;
    ReactiveCacheManager reactiveCacheManager;

    @BeforeEach
    void setUp() {
        RedisSerializationContext<Object, Object> serializationContextBuilder = RedisSerializationContext.java();
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration("localhost", 36379);
        LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(configuration);
        connectionFactory.afterPropertiesSet();

        reactiveRedisTemplate = new ReactiveRedisTemplate(connectionFactory, serializationContextBuilder);
        reactiveCacheManager = new ReactiveCacheManager(Duration.ofSeconds(10), reactiveRedisTemplate);
    }

    @Test
    @Order(1)
    void whenCacheIsEmpty() {
        StepVerifier.create(reactiveCacheManager.getFromCache())
                .expectComplete()
                .verify();
    }

    @Test
    @Order(2)
    void whenFillCache() {
        StepVerifier.create(reactiveCacheManager.updateCache(i18nsMessage()))
                .expectNext(i18nsMessage())
                .expectComplete()
                .verify();

        StepVerifier.create(reactiveCacheManager.getFromCache())
                .expectNext(i18nsMessage())
                .expectComplete()
                .verify();
    }

    @Test
    @Order(3)
    void whenEvictAFilledCache() {
        StepVerifier.create(reactiveCacheManager.updateCache(i18nsMessage()))
                .expectNext(i18nsMessage())
                .expectComplete()
                .verify();

        StepVerifier.create(reactiveCacheManager.getFromCache())
                .expectNext(i18nsMessage())
                .expectComplete()
                .verify();

        StepVerifier.create(reactiveCacheManager.evictCache())
                .expectComplete()
                .verify();

        StepVerifier.create(reactiveCacheManager.getFromCache())
                .expectComplete()
                .verify();
    }
}