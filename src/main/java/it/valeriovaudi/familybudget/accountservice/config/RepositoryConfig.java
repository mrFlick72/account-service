package it.valeriovaudi.familybudget.accountservice.config;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import it.valeriovaudi.familybudget.accountservice.adapters.repository.R2dbcAccountRepository;
import it.valeriovaudi.familybudget.accountservice.domain.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.r2dbc.core.DatabaseClient;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

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

}
