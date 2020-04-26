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
                                Optional.ofNullable(getObject(resultSet, "phone", String.class))
                                        .map(Phone::phoneFor).orElse(Phone.nullValue()),
                                Locale.forLanguageTag(getObject(resultSet, "LOCALE", String.class))
                        )
                );
    }

    private <T> T getObject(Map<String, Object> resultSet, String key, Class<T> clazz) {
        return clazz.cast(resultSet.get(key));
    }

    @Override
    public Publisher<Void> save(Account account) {
        System.out.println("account: " + account);
        return databaseClient.execute("INSERT INTO ACCOUNT (first_Name,last_Name, birth_Date, mail,  phone, locale) VALUES (:first_Name, :last_Name, :birth_Date, :mail, :phone, :locale)")
                .bind("first_Name", account.getFirstName())
                .bind("last_Name", account.getLastName())
                .bind("birth_Date", account.getBirthDate().getLocalDate())
                .bind("mail", account.getMail())
                .bind("phone", account.getPhone().formattedPhone())
                .bind("locale", account.getLocale().toLanguageTag())

                .fetch()
                .rowsUpdated()
                .flatMap(result -> Mono.empty());

    }

    @Override
    public Publisher<Void> update(Account account) {
        return databaseClient.execute("UPDATE ACCOUNT SET first_Name=:first_Name, last_Name=:last_Name, birth_Date=:birth_Date,  phone=:phone, locale=:locale WHERE mail=:mail")
                .bind("first_Name", account.getFirstName())
                .bind("last_Name", account.getLastName())
                .bind("birth_Date", account.getBirthDate().getLocalDate())
                .bind("mail", account.getMail())
                .bind("phone", account.getPhone().formattedPhone())
                .bind("locale", account.getLocale().toLanguageTag())
                .fetch()
                .rowsUpdated()
                .flatMap(result -> Mono.empty());

    }

}
