package it.valeriovaudi.familybudget.accountservice;


import it.valeriovaudi.familybudget.accountservice.domain.model.Account;
import it.valeriovaudi.familybudget.accountservice.domain.model.Date;
import it.valeriovaudi.familybudget.accountservice.domain.model.Phone;
import org.springframework.r2dbc.core.DatabaseClient;

import java.util.Locale;

public class TestingFixture {

    public static final String POSTGRESS_HOST = System.getProperty("test.database.host", "localhost");
    public static final Integer POSTGRESS_PORT = Integer.valueOf(System.getProperty("test.database.port", "35432"));

    public static void initDatabase(DatabaseClient databaseClient) {
        databaseClient.sql("INSERT INTO ACCOUNT (first_Name,last_Name, birth_Date, mail, phone, locale) VALUES ('Valerio', 'Vaudi', '1970-01-01', 'valerio.vaudi-with-phone@test.com','+39 333 2255112' ,'en')")
                .fetch().rowsUpdated().block();
        databaseClient.sql("INSERT INTO ACCOUNT (first_Name,last_Name, birth_Date, mail, locale) VALUES ('Valerio', 'Vaudi', '1970-01-01', 'valerio.vaudi@test.com', 'en')")
                .fetch().rowsUpdated().block();
    }

    public static void cleanDatabase(DatabaseClient databaseClient) {
        databaseClient.sql("TRUNCATE ACCOUNT")
                .fetch().rowsUpdated().block();
    }

    public static Account anAccount() {
        return new Account("FIRST_NAME",
                "LAST_NAME",
                Date.dateFor("01/01/1970"),
                "user.mail@mail.com",
                Phone.nullValue(),
                Locale.ENGLISH
        );
    }

    public static String anAccountAsJsonString() {
        return "{\"firstName\":\"FIRST_NAME\",\"lastName\":\"LAST_NAME\",\"birthDate\":\"01/01/1970\",\"mail\":\"user.mail@mail.com\",\"phone\":\"\"}";
    }

    public static String ACCOUNT_MAIL = "user.mail@mail.com";
}