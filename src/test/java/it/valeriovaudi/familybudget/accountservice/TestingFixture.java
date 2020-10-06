package it.valeriovaudi.familybudget.accountservice;

import org.springframework.data.r2dbc.core.DatabaseClient;

public class TestingFixture {

    public static final String POSTGRESS_HOST = System.getProperty("test.database.host", "localhost");
    public static final Integer POSTGRESS_PORT = Integer.valueOf(System.getProperty("test.database.port", "35432"));

    public static void initDatabase(DatabaseClient databaseClient) {
        databaseClient.execute("INSERT INTO ACCOUNT (first_Name,last_Name, birth_Date, mail, phone, locale) VALUES ('Valerio', 'Vaudi', '1970-01-01', 'valerio.vaudi-with-phone@test.com','+39 333 2255112' ,'en')")
                .fetch().rowsUpdated().block();
        databaseClient.execute("INSERT INTO ACCOUNT (first_Name,last_Name, birth_Date, mail, locale) VALUES ('Valerio', 'Vaudi', '1970-01-01', 'valerio.vaudi@test.com', 'en')")
                .fetch().rowsUpdated().block();
    }

    public static void cleanDatabase(DatabaseClient databaseClient) {
        databaseClient.execute("TRUNCATE ACCOUNT")
                .fetch().rowsUpdated().block();
    }

}