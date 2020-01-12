package it.valeriovaudi.familybudget.accountservice.adapters.repository;

import it.valeriovaudi.familybudget.accountservice.domain.model.Account;
import it.valeriovaudi.familybudget.accountservice.domain.model.Date;
import it.valeriovaudi.familybudget.accountservice.domain.model.Phone;
import it.valeriovaudi.familybudget.accountservice.domain.repository.AccountRepository;
import org.reactivestreams.Publisher;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.springframework.util.StringUtils.collectionToCommaDelimitedString;

@Transactional
public class R2dbcAccountRepository implements AccountRepository {

    private final DatabaseClient databaseClient;

    public R2dbcAccountRepository(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    @Override
    public Publisher<Account> findByMail(String mail) {
        return databaseClient.execute("SELECT * FROM ACCOUNT WHERE mail=:mail")
                .bind("mail", mail)
                .fetch()
                .all()
                .map(resultSet ->
                        new Account(getObject(resultSet, "FIRST_NAME", String.class),
                                getObject(resultSet, "LAST_NAME", String.class),
                                new Date(getObject(resultSet, "BIRTH_DATE", LocalDate.class)),
                                getObject(resultSet, "MAIL", String.class),
                                getObject(resultSet, "PASSWORD", String.class),
                                asList(getObject(resultSet, "USER_ROLES", String.class).split(",")),
                                Optional.ofNullable(getObject(resultSet, "phone", String.class))
                                        .map(Phone::phoneFor).orElse(Phone.nullValue()), getObject(resultSet, "ENABLE", Boolean.class),
                                Locale.forLanguageTag(getObject(resultSet, "LOCALE", String.class))
                        )
                );
    }

    private <T> T getObject(Map<String, Object> resultSet, String key, Class<T> clazz) {
        return clazz.cast(resultSet.get(key));
    }

    @Override
    public Publisher<Void> save(Account account) {
        return databaseClient.execute("INSERT INTO ACCOUNT (first_Name,last_Name, birth_Date, mail, password, user_roles, phone, enable, locale) VALUES (:first_Name, :last_Name, :birth_Date, :mail, :password, :user_roles, :phone, :enable, :locale)")
                .bind("first_Name", account.getFirstName())
                .bind("last_Name", account.getLastName())
                .bind("birth_Date", account.getBirthDate().getLocalDate())
                .bind("mail", account.getMail())
                .bind("password", account.getPassword())
                .bind("user_roles", collectionToCommaDelimitedString(account.getUserRoles()))
                .bind("phone", account.getPhone().formattedPhone())
                .bind("enable", account.getEnable())
                .bind("locale", account.getLocale().toLanguageTag())

                .fetch()
                .rowsUpdated()
                .flatMap(result -> Mono.empty());

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
