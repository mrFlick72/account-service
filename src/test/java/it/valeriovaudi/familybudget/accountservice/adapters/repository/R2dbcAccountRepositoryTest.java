package it.valeriovaudi.familybudget.accountservice.adapters.repository;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import it.valeriovaudi.familybudget.accountservice.domain.model.Account;
import it.valeriovaudi.familybudget.accountservice.domain.model.Date;
import it.valeriovaudi.familybudget.accountservice.domain.model.Phone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.test.StepVerifier;

import java.io.File;
import java.util.Locale;

import static io.r2dbc.spi.ConnectionFactoryOptions.*;
import static java.util.Arrays.asList;


@Testcontainers
public class R2dbcAccountRepositoryTest {

    private R2dbcAccountRepository accountRepository;


    @Container
    private static final DockerComposeContainer postgres = new DockerComposeContainer(new File("src/test/resources/docker-compose.yml"))
            .withExposedService("postgres_1", 5432);


    @BeforeEach
    public void setUp() {
        var serviceHost = postgres.getServiceHost("postgres_1", 5432);
        var servicePort = postgres.getServicePort("postgres_1", 5432);

        ConnectionFactory connectionFactory = ConnectionFactories.get(ConnectionFactoryOptions.builder()
                .option(DRIVER, "postgresql")
                .option(HOST, serviceHost)
                .option(PORT, servicePort)  // optional, defaults to 5432
                .option(USER, "root")
                .option(PASSWORD, "root")
                .option(DATABASE, "account_service")  // optional
                .build());

        DatabaseClient databaseClient = DatabaseClient.create(connectionFactory);
        accountRepository = new R2dbcAccountRepository(databaseClient);
    }

    @Test
    public void saveAccount() {
        var expected = new Account("Valerio",
                "Vaudi",
                Date.dateFor("01/01/1970"),
                "valerio.vaudi123@test.com",
                "secret",
                asList("ROLE_USER", "ROLE_ADMIN"),
                Phone.phoneFor("+39 333 2255112"),
                Boolean.TRUE,
                Locale.ENGLISH);

        var save = accountRepository.save(expected);

        StepVerifier.create(save)
                .expectNext()
                .verifyComplete();
    }


    @Test
    public void findByMail() {
        var expected = new Account("Valerio",
                "Vaudi", Date.dateFor("01/01/1970"),
                "valerio.vaudi@test.com",
                "secret",
                asList("ROLE_USER", "ROLE_ADMIN"),
                Phone.nullValue(),
                Boolean.TRUE,
                Locale.ENGLISH);

        var accountPublisher = accountRepository.findByMail("valerio.vaudi@test.com");
        StepVerifier.create(accountPublisher)
                .expectNext(expected)
                .verifyComplete();
    }

    @Test
    public void findByMailWithPhone() {
        Account expected = new Account("Valerio", "Vaudi", Date.dateFor("01/01/1970"), "valerio.vaudi-with-phone@test.com", "secret", asList("ROLE_USER", "ROLE_ADMIN"), Phone.phoneFor("+39 333 2255112"), Boolean.TRUE, Locale.ENGLISH);

        var accountPublisher = accountRepository.findByMail("valerio.vaudi-with-phone@test.com");
        StepVerifier.create(accountPublisher)
                .expectNext(expected)
                .verifyComplete();
    }

    @Test
    public void updateAccount() {
        Account expected = new Account("Valerio", "Vaudi", Date.dateFor("01/01/1970"), "valerio.vaudi@test.com", "secret", asList("ROLE_USER", "ROLE_ADMIN"), Phone.phoneFor("+39 333 2255112"), Boolean.FALSE, Locale.ITALIAN);
        var accountPublisher = accountRepository.update(expected);

        StepVerifier.create(accountPublisher)
                .expectNext()
                .verifyComplete();
    }
}