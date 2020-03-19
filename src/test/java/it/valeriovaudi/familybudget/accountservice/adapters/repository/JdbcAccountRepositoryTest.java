package it.valeriovaudi.familybudget.accountservice.adapters.repository;

import it.valeriovaudi.familybudget.accountservice.domain.model.Account;
import it.valeriovaudi.familybudget.accountservice.domain.model.Date;
import it.valeriovaudi.familybudget.accountservice.domain.model.Phone;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Locale;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


@JdbcTest
@RunWith(SpringRunner.class)
public class JdbcAccountRepositoryTest {


    @Autowired
    JdbcTemplate jdbcTemplate;

    private JdbcAccountRepository jdbcBudgetExpenseRepository;

    @Before
    public void setUp() {
        jdbcBudgetExpenseRepository = new JdbcAccountRepository(jdbcTemplate);
    }

    @Test
    @Sql("classpath:account/find-by-mail-data-set.sql")
    public void findByMail() {
        Account actual = jdbcBudgetExpenseRepository.findByMail("valerio.vaudi@test.com");
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
    public void saveAccount() {
        Account expected  = new Account("Valerio", "Vaudi", Date.dateFor("01/01/1970"), "valerio.vaudi@test.com", "secret", asList("ROLE_USER", "ROLE_ADMIN"), Phone.phoneFor("+39 333 2255112"), Boolean.TRUE, Locale.ENGLISH);
        jdbcBudgetExpenseRepository.save(expected);
        Account actual= jdbcBudgetExpenseRepository.findByMail("valerio.vaudi@test.com");

        assertThat(actual, is(expected));
    }

    @Test
    @Sql("classpath:account/find-by-mail-data-set.sql")
    public void updateAccount() {
        Account expected = new Account("Valerio", "Vaudi", Date.dateFor("01/01/1970"), "valerio.vaudi@test.com", "secret", asList("ROLE_USER", "ROLE_ADMIN"), Phone.phoneFor("+39 333 2255112"), Boolean.FALSE, Locale.ITALIAN);
        jdbcBudgetExpenseRepository.update(expected);

        Account actual = jdbcBudgetExpenseRepository.findByMail("valerio.vaudi@test.com");
        assertThat(actual, is(expected));
    }
}