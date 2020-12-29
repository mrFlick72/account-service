package it.valeriovaudi.familybudget.accountservice.web.endpoint;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.valeriovaudi.familybudget.accountservice.domain.repository.MessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockUser;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.springSecurity;

@WebFluxTest(value = {I18nMessagesEndPoint.class})
class I18nMessagesEndPointTest {

    @Autowired
    private ApplicationContext context;

    private WebTestClient webTestClient;

    @MockBean
    private MessageRepository messageRepository;

    @BeforeEach
    public void setUp() {
        webTestClient = WebTestClient.bindToApplicationContext(context)
                .apply(springSecurity())
                .apply(mockUser())
                .build();
    }


    @Test
    void whenI18nAreFound() throws JsonProcessingException {
        var objectMapper = new ObjectMapper();
        Map<String, String> expected = Map.of("key1", "prop1");

        given(messageRepository.messages())
                .willReturn(Mono.just(expected));

        webTestClient.get().uri("/messages")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody().json(objectMapper.writeValueAsString(expected));
    }
}