package it.valeriovaudi.familybudget.accountservice.adapters.cache;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import static org.junit.jupiter.api.Assertions.*;

class ReactiveCacheManagerTest {

    @BeforeEach
    void setUp() {
        RedisSerializationContext<Object, Object> serializationContextBuilder = RedisSerializationContext.newSerializationContext().build();
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration("localhost", 36379);
        LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(configuration);
        new ReactiveRedisTemplate(connectionFactory, serializationContextBuilder);

    }
}