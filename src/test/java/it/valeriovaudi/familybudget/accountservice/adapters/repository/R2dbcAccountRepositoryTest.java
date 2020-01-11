package it.valeriovaudi.familybudget.accountservice.adapters.repository;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import it.valeriovaudi.familybudget.accountservice.domain.model.Account;
import it.valeriovaudi.familybudget.accountservice.domain.model.Date;
import it.valeriovaudi.familybudget.accountservice.domain.model.Phone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.util.Locale;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;


@Testcontainers
public class R2dbcAccountRepositoryTest {

    private R2dbcAccountRepository accountRepository;


    @Container
    public static DockerComposeContainer postgres = new DockerComposeContainer(new File("src/test/resources/docker-compose.yml"))
            .withExposedService("postgres_1", 5432);


    @BeforeEach
    public void setUp() {
        var serviceHost = postgres.getServiceHost("postgres_1", 5432);
        var servicePort = postgres.getServicePort("postgres_1", 5432);

        String url = String.format("r2dbcs:postgess://root:root%s:%s/account_service", serviceHost, servicePort);
        ConnectionFactory connectionFactory = ConnectionFactories.get(url);
        DatabaseClient databaseClient = DatabaseClient.create(connectionFactory);
        accountRepository  = new R2dbcAccountRepository(databaseClient);
    }

    @Test
    public void saveAccount() {
        Account expected  = new Account("Valerio",
                "Vaudi",
                Date.dateFor("01/01/1970"),
                "valerio.vaudi@test.com",
                "secret",
                asList("ROLE_USER", "ROLE_ADMIN"),
                Phone.phoneFor("+39 333 2255112"),
                Boolean.TRUE,
                Locale.ENGLISH);

        accountRepository.save(expected);
        Account actual= accountRepository.findByMail("valerio.vaudi@test.com");

        assertThat(actual, equalTo(expected));
    }

/*
    @Test
    @Sql("classpath:account/find-by-mail-data-set.sql")
    public void findByMail() {
        Account actual = accountRepository.findByMail("valerio.vaudi@test.com");
        Account expected = new Account("Valerio", "Vaudi", Date.dateFor("01/01/1970"), "valerio.vaudi@test.com", "secret", asList("ROLE_USER", "ROLE_ADMIN"), Phone.nullValue(), Boolean.TRUE, Locale.ENGLISH.ENGLISH);
        assertThat(actual, is(expected));
    }

    @Test
    @Sql("classpath:account/find-by-mail-data-set.sql")
    public void findByMailWithPhone() {
        Account actual = jdbcBudgetExpenseRepository.findByMail("valerio.vaudi-with-phone@test.com");
        Account expected = new Account("Valerio", "Vaudi", Date.dateFor("01/01/1970"), "valerio.vaudi-with-phone@test.com", "secret", asList("ROLE_USER", "ROLE_ADMIN"), Phone.phoneFor("+39 333 2255112"), Boolean.TRUE, Locale.ENGLISH);
        assertThat(actual, is(expected));
    }



    @Test
    @Sql("classpath:account/find-by-mail-data-set.sql")
    public void updateAccount() {
        Account expected = new Account("Valerio", "Vaudi", Date.dateFor("01/01/1970"), "valerio.vaudi@test.com", "secret", asList("ROLE_USER", "ROLE_ADMIN"), Phone.phoneFor("+39 333 2255112"), Boolean.FALSE, Locale.ITALIAN);
        jdbcBudgetExpenseRepository.update(expected);

        Account actual = jdbcBudgetExpenseRepository.findByMail("valerio.vaudi@test.com");
        assertThat(actual, is(expected));
    }*/
}