package it.valeriovaudi.familybudget.accountservice.web.endpoint;

import it.valeriovaudi.familybudget.accountservice.domain.UpdateAccount;
import it.valeriovaudi.familybudget.accountservice.domain.model.Account;
import it.valeriovaudi.familybudget.accountservice.domain.model.Date;
import it.valeriovaudi.familybudget.accountservice.domain.model.Phone;
import it.valeriovaudi.familybudget.accountservice.domain.repository.AccountRepository;
import it.valeriovaudi.familybudget.accountservice.web.adapter.AccountAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import java.util.Locale;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.*;

@WebFluxTest(value = {AccountEndPoint.class, AdapterTestConfig.class})
class AccountEndPointTest {

    @Autowired
    private ApplicationContext context;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private UpdateAccount updateAccount;

    private WebTestClient webTestClient;

    @BeforeEach
    public void setUp() {
        webTestClient = WebTestClient.bindToApplicationContext(context)
                .apply(springSecurity())
                .apply(mockUser())
                .build();
    }

    @Test
    void getUserInfoFromEmail() {
        Account account = new Account("FIRST_NAME",
                "LAST_NAME",
                Date.dateFor("01/01/1970"),
                "user.mail@mail.com",
                Phone.nullValue(),
                Locale.ITALIAN
        );

        given(accountRepository.findByMail("user.mail@mail.com"))
                .willReturn(Mono.just(account));


        String expected = "{\"firstName\":\"FIRST_NAME\",\"lastName\":\"LAST_NAME\",\"birthDate\":\"01/01/1970\",\"mail\":\"user.mail@mail.com\",\"phone\":\"\"}";
        webTestClient.get()
                .uri("/user.mail@mail.com/mail")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody().json(expected);
    }

    @Test
    void updateAccount() {
        Account account = new Account("FIRST_NAME",
                "LAST_NAME",
                Date.dateFor("01/01/1970"),
                "user.mail@mail.com",
                Phone.nullValue(),
                Locale.ENGLISH
        );

        System.out.println(account);
        given(updateAccount.execute(account))
                .willReturn(Mono.fromRunnable(() -> {
                }));


        String content = "{\"firstName\":\"FIRST_NAME\",\"lastName\":\"LAST_NAME\",\"birthDate\":\"01/01/1970\",\"mail\":\"user.mail@mail.com\",\"phone\":\"\"}";
        webTestClient.mutateWith(csrf()).put()
                .uri("/user.mail@mail.com/mail")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(content))
                .exchange()
                .expectStatus().is2xxSuccessful();
    }
}

@Configuration
class AdapterTestConfig {

    @Bean
    public AccountAdapter accountAdapter() {
        return new AccountAdapter();
    }

}