package it.valeriovaudi.familybudget.accountservice.adapters.repository;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.common.Json;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import it.valeriovaudi.familybudget.accountservice.adapters.cache.ReactiveCacheManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.HashMap;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static it.valeriovaudi.familybudget.accountservice.TestingFixture.i18nsMessage;
import static it.valeriovaudi.familybudget.accountservice.TestingFixture.newReactiveRedisTemplate;

class RestMessageRepositoryTest {

    private RestMessageRepository restMessageRepository;
    private ReactiveCacheManager cacheManager;

    private ObjectMapper objectMapper = new ObjectMapper();
    private WireMockServer wireMockServer = new WireMockServer(WireMockConfiguration.options().dynamicPort());

    @BeforeEach
    void setUp() {
        wireMockServer.start();
        configureFor("localhost", wireMockServer.port());

        cacheManager = new ReactiveCacheManager(Duration.ofSeconds(1), newReactiveRedisTemplate());
        restMessageRepository = new RestMessageRepository(
                wireMockServer.baseUrl(),
                "account-service",
                WebClient.builder().build(),
                cacheManager
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

    @Test
    void whenCacheIsFull() {
        // fill cache
        cacheManager.updateCache(i18nsMessage()).block();
        StepVerifier.create(cacheManager.updateCache(i18nsMessage()))
                .expectNext(i18nsMessage())
                .verifyComplete();

        StepVerifier.create(restMessageRepository.messages())
                .expectNext(i18nsMessage())
                .verifyComplete();
    }
}