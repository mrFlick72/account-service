package it.valeriovaudi.familybudget.accountservice.adapters.repository;

import it.valeriovaudi.familybudget.accountservice.domain.model.Account;
import it.valeriovaudi.familybudget.accountservice.domain.model.Date;
import it.valeriovaudi.familybudget.accountservice.domain.model.Phone;
import it.valeriovaudi.familybudget.accountservice.domain.repository.AccountRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.springframework.util.StringUtils.collectionToCommaDelimitedString;

@Transactional
public class JdbcAccountRepository implements AccountRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Account findByMail(String mail) {
        return jdbcTemplate.queryForObject("SELECT * FROM ACCOUNT WHERE mail=?", (resultSet, rowNum) ->
                new Account(resultSet.getString("FIRST_NAME"),
                        resultSet.getString("LAST_NAME"),
                        new Date(resultSet.getObject("BIRTH_DATE", LocalDate.class)),
                        resultSet.getString("MAIL"),
                        resultSet.getString("PASSWORD"),
                        asList(resultSet.getString("USER_ROLES").split(",")),
                        Optional.ofNullable(resultSet.getString("phone"))
                                .map(Phone::phoneFor).orElse(Phone.nullValue()), resultSet.getBoolean("ENABLE"),
                        Locale.forLanguageTag(resultSet.getString("LOCALE"))
                ), mail);
    }

    @Override
    public void save(Account account) {
        jdbcTemplate.update("INSERT INTO ACCOUNT (first_Name,last_Name, birth_Date, mail, password, user_roles, phone, enable, locale) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                account.getFirstName(), account.getLastName(), account.getBirthDate().getLocalDate(), account.getMail(), account.getPassword(), collectionToCommaDelimitedString(account.getUserRoles()), account.getPhone().formattedPhone(), account.getEnable(), account.getLocale().toLanguageTag());
    }

    @Override
    public void update(Account account) {
        jdbcTemplate.update("UPDATE ACCOUNT SET first_Name=?, last_Name=?, birth_Date=?, password=?, user_roles=?, phone=?, enable=?, locale=? WHERE mail=?",
                account.getFirstName(), account.getLastName(), account.getBirthDate().getLocalDate(), account.getPassword(), collectionToCommaDelimitedString(account.getUserRoles()), account.getPhone().formattedPhone(), account.getEnable(), account.getLocale().toLanguageTag(), account.getMail());

    }

}
