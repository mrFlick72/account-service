package it.valeriovaudi.familybudget.accountservice.adapters.repository;

import it.valeriovaudi.familybudget.accountservice.domain.model.Account;
import it.valeriovaudi.familybudget.accountservice.domain.repository.AccountRepository;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class R2dbcAccountRepository implements AccountRepository {

    private final DatabaseClient client;

    public R2dbcAccountRepository(DatabaseClient client) {
        this.client = client;
    }

    @Override
    public Account findByMail(String mail) {
        return null;
    }

    @Override
    public void save(Account account) {

    }

    @Override
    public void update(Account account) {

    }
    /*
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

    }*/

}
