package it.valeriovaudi.familybudget.accountservice.config;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import it.valeriovaudi.familybudget.accountservice.adapters.cache.ReactiveCacheManager;
import it.valeriovaudi.familybudget.accountservice.adapters.cache.ReactiveCacheUpdaterListener;
import it.valeriovaudi.familybudget.accountservice.adapters.cache.ReceiveMessageRequestFactory;
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
import reactor.core.publisher.Flux;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

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
    public ReceiveMessageRequestFactory receiveMessageRequestFactory(@Value("${i18n-messages.cache.updater.listener.queueUrl}") String queueUrl,
                                                                     @Value("${i18n-messages.cache.updater.listener.maxNumberOfMessages}") Integer maxNumberOfMessages,
                                                                     @Value("${i18n-messages.cache.updater.listener.visibilityTimeout}") Integer visibilityTimeout,
                                                                     @Value("${i18n-messages.cache.updater.listener.waitTimeSeconds}") Integer waitTimeSeconds

                                                                     ) {
        return new ReceiveMessageRequestFactory(queueUrl, maxNumberOfMessages, visibilityTimeout, waitTimeSeconds);
    }

    @Bean
    public ReactiveCacheUpdaterListener reactiveCacheUpdaterListener(@Value("${i18n-messages.cache.updater.listener.sleeping:10m}") Duration sleeping,
                                                                     ReceiveMessageRequestFactory receiveMessageRequestFactory,
                                                                     ReactiveCacheManager reactiveCacheManager,
                                                                     SqsAsyncClient sqsAsyncClient) {
        return new ReactiveCacheUpdaterListener(sleeping, Flux.just(1).repeat(),
                receiveMessageRequestFactory, reactiveCacheManager, sqsAsyncClient);
    }

    @Bean
    public AwsCredentialsProvider awsCredentialsProvider(@Value("${aws.access-key}") String accessKey,
                                                         @Value("${aws.secret-key}") String awsSecretKey) {
        return StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, awsSecretKey));
    }


    @Bean
    public SqsAsyncClient sqsAsyncClient(@Value("${aws.region}") String awsRegion,
                                         AwsCredentialsProvider awsCredentialsProvider) {
        return SqsAsyncClient.builder()
                .credentialsProvider(awsCredentialsProvider)
                .region(Region.of(awsRegion))
                .build();
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
