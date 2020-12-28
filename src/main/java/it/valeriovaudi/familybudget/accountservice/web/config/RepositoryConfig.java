package it.valeriovaudi.familybudget.accountservice.web.config;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import it.valeriovaudi.familybudget.accountservice.adapters.cache.ReactiveCacheManager;
import it.valeriovaudi.familybudget.accountservice.adapters.repository.R2dbcAccountRepository;
import it.valeriovaudi.familybudget.accountservice.adapters.repository.RestMessageRepository;
import it.valeriovaudi.familybudget.accountservice.domain.repository.AccountRepository;
import it.valeriovaudi.familybudget.accountservice.domain.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.Optional;

import static io.r2dbc.spi.ConnectionFactoryOptions.*;


@Configuration
@EnableConfigurationProperties(R2dbcProperties.class)
public class RepositoryConfig {

    @Bean
    public ConnectionFactory connectionFactory(@Value("${spring.r2dbc.host}") String host,
                                               R2dbcProperties r2dbcProperties) {
        return ConnectionFactories.get(ConnectionFactoryOptions.builder()
                .option(DRIVER, "postgresql")
                .option(HOST, host)
                .option(PORT, 5432)
                .option(USER, r2dbcProperties.getUsername())
                .option(PASSWORD, r2dbcProperties.getPassword())
                .option(DATABASE, "account_service")
                .build());

    }

    @Bean
    public AccountRepository accountRepository(DatabaseClient databaseClient) {
        return new R2dbcAccountRepository(databaseClient);
    }

    @Bean
    public ReactiveCacheManager cacheManager(@Value("${i18n-messages.ttl:10m}") Duration ttl, ReactiveRedisTemplate reactiveRedisTemplate) {
        return new ReactiveCacheManager(ttl, reactiveRedisTemplate);
    }

    @Bean
    public MessageRepository messageRepository(Optional<WebClient.Builder> optional,
                                               @Value("${i18n-messages.base-url:http://i18n-messages}") String i18nBaseUrl,
                                               @Value("${spring.application.name}") String applicationId,
                                               ReactiveCacheManager cacheManager) {
        WebClient.Builder builder = optional.orElse(WebClient.builder());
        WebClient template = builder.build();
        return new RestMessageRepository(i18nBaseUrl, applicationId, template, cacheManager);
    }
}
