package it.valeriovaudi.familybudget.accountservice.web.endpoint;

import it.valeriovaudi.familybudget.accountservice.domain.repository.MessageRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class I18nMessagesEndPoint {

    private final MessageRepository messageRepository;

    public I18nMessagesEndPoint(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Bean
    public RouterFunction i18nMessagesEndPointRoute() {
        return RouterFunctions.route()
                .GET("/messages",
                        serverRequest ->
                                Mono.from(messageRepository.messages())
                                        .flatMap(messages ->
                                                ok().body(fromValue(messages))
                                        )
                )
                .build();
    }

/*    @GetMapping("/messages")
    public ResponseEntity messages() {
        return ResponseEntity.ok(messageRepository.messages());
    }*/
}
