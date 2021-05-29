package it.valeriovaudi.familybudget.accountservice.adapters.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static it.valeriovaudi.familybudget.accountservice.TestingFixture.i18nsMessage;

class RestMessageRepositoryTest {

    private RestMessageRepository restMessageRepository;

    private ObjectMapper objectMapper = new ObjectMapper();
    private WireMockServer wireMockServer = new WireMockServer(WireMockConfiguration.options().dynamicPort());

    @BeforeEach
    void setUp() {
        wireMockServer.start();
        configureFor("localhost", wireMockServer.port());

        restMessageRepository = new RestMessageRepository(
                wireMockServer.baseUrl(),
                "account-service",
                WebClient.builder().build()
        );
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    @Test()
    void whenCacheIsEmpty() throws JsonProcessingException {
        String body = objectMapper.writeValueAsString(i18nsMessage());

        StubMapping stubMapping = stubFor(
                get(urlPathEqualTo("/messages/account-service"))
                        .willReturn(aResponse()
                                .withHeader("Content-Type", "application/json")
                                .withBody(body)));

        wireMockServer.addStubMapping(stubMapping);

        StepVerifier.create(restMessageRepository.messages())
                .expectNext(i18nsMessage())
                .verifyComplete();

        wireMockServer.verify(getRequestedFor(urlEqualTo("/messages/account-service")));
    }
}