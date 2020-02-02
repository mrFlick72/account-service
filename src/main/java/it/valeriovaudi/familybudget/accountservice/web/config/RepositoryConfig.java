package it.valeriovaudi.familybudget.accountservice.web.config;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import io.rsocket.transport.netty.client.TcpClientTransport;
import it.valeriovaudi.familybudget.accountservice.adapters.cache.ReactiveCacheManager;
import it.valeriovaudi.familybudget.accountservice.adapters.repository.R2dbcAccountRepository;
import it.valeriovaudi.familybudget.accountservice.adapters.repository.RSocketMessageRepository;
import it.valeriovaudi.familybudget.accountservice.domain.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;

import static io.r2dbc.spi.ConnectionFactoryOptions.*;


@Configuration
@EnableConfigurationProperties(R2dbcProperties.class)
public class RepositoryConfig {

    @Bean
    public ConnectionFactory connectionFactory(@Value("${spring.r2dbc.host}") String host,
                                               R2dbcProperties r2dbcProperties) {
        System.out.println("spring.r2dbc.host: " + host);
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
    public ReactiveCacheManager cacheManeger(ReactiveRedisTemplate reactiveRedisTemplate) {
        return new ReactiveCacheManager(reactiveRedisTemplate);
    }

    @Bean
    public RSocketMessageRepository messageRepository(RSocketStrategies rSocketStrategies,
                                                      @Value("${i18n-messages.rsocket.host}") String i18nHost,
                                                      @Value("${i18n-messages.rsocket.port}") int i18nPort,
                                                      RSocketRequester.Builder builder,
                                                      @Value("${spring.application.name}") String applicationId,
                                                      ReactiveCacheManager cacheManeger) {
        InetSocketAddress address = new InetSocketAddress(i18nHost, i18nPort);
        TcpClientTransport clientTransport = TcpClientTransport.create(address);
        Mono<RSocketRequester> requesterMono =
                builder.rsocketStrategies(rSocketStrategies)
                        .connect(clientTransport);

        return new RSocketMessageRepository(applicationId, requesterMono, cacheManeger);
    }
}
