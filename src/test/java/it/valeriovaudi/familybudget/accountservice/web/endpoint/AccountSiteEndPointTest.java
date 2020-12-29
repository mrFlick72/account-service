package it.valeriovaudi.familybudget.accountservice.web.endpoint;

import it.valeriovaudi.familybudget.accountservice.TestingFixture;
import it.valeriovaudi.familybudget.accountservice.domain.UpdateAccount;
import it.valeriovaudi.familybudget.accountservice.domain.model.Account;
import it.valeriovaudi.familybudget.accountservice.domain.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.*;

@WebFluxTest(value = {AccountSiteEndPoint.class, AdapterTestConfig.class})
class AccountSiteEndPointTest {
    private final static String ENDPOINT_PREFIX = "/site/user-info";

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
                .apply(mockOidcLogin().userInfoToken(builder -> builder.email(TestingFixture.ACCOUNT_MAIL)))
                .build();
    }

    @Test
    void whenGetAUserAccountDetails() {
        given(accountRepository.findByMail("user.mail@mail.com"))
                .willReturn(Mono.just(TestingFixture.anAccount()));

        webTestClient.get().uri(ENDPOINT_PREFIX)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody().json(TestingFixture.anAccountAsJsonString());
    }

    @Test
    void whenUpdateAUserAccountDetails() {
        Account account = TestingFixture.anAccount();

        given(updateAccount.execute(account))
                .willReturn(Mono.empty());

        String content = TestingFixture.anAccountAsJsonString();
        webTestClient.mutateWith(csrf()).put()
                .uri(ENDPOINT_PREFIX)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(content))
                .exchange()
                .expectStatus().is2xxSuccessful();

    }
}