package it.valeriovaudi.familybudget.accountservice.adapters.repository;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import it.valeriovaudi.familybudget.accountservice.TestingFixture;
import it.valeriovaudi.familybudget.accountservice.domain.model.Account;
import it.valeriovaudi.familybudget.accountservice.domain.model.Date;
import it.valeriovaudi.familybudget.accountservice.domain.model.Phone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.r2dbc.core.DatabaseClient;
import reactor.test.StepVerifier;

import java.util.Locale;

import static io.r2dbc.spi.ConnectionFactoryOptions.*;


public class R2dbcAccountRepositoryTest {

    private R2dbcAccountRepository accountRepository;

    @BeforeEach
    public void setUp() {
        var serviceHost = TestingFixture.POSTGRESS_HOST;
        var servicePort = TestingFixture.POSTGRESS_PORT;

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

        TestingFixture.cleanDatabase(databaseClient);
        TestingFixture.initDatabase(databaseClient);
    }

    @Test
    public void saveAccount() {
        var expected = new Account("Valerio",
                "Vaudi",
                Date.dateFor("01/01/1970"),
                "valerio.vaudi123@test.com",
                Phone.phoneFor("+39 333 2255112"),
                Locale.ENGLISH
        );

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
                Phone.nullValue(),
                Locale.ENGLISH
        );

        var accountPublisher = accountRepository.findByMail("valerio.vaudi@test.com");
        StepVerifier.create(accountPublisher)
                .expectNext(expected)
                .verifyComplete();
    }

    @Test
    public void findByMailWithPhone() {
        Account expected = new Account("Valerio", "Vaudi", Date.dateFor("01/01/1970"), "valerio.vaudi-with-phone@test.com", Phone.phoneFor("+39 333 2255112"), Locale.ENGLISH);

        var accountPublisher = accountRepository.findByMail("valerio.vaudi-with-phone@test.com");
        StepVerifier.create(accountPublisher)
                .expectNext(expected)
                .verifyComplete();
    }

    @Test
    public void updateAccount() {
        Account expected = new Account("Valerio", "Vaudi", Date.dateFor("01/01/1970"), "valerio.vaudi@test.com", Phone.phoneFor("+39 333 2255112"), Locale.ENGLISH);
        var accountPublisher = accountRepository.update(expected);

        StepVerifier.create(accountPublisher)
                .expectNext()
                .verifyComplete();
    }
}