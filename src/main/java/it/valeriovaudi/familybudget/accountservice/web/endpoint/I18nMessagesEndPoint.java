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
    private final ContextPathProvider contextPathProvider;

    public I18nMessagesEndPoint(MessageRepository messageRepository, ContextPathProvider contextPathProvider) {
        this.messageRepository = messageRepository;
        this.contextPathProvider = contextPathProvider;
    }

    @Bean
    public RouterFunction i18nMessagesEndPointRoute() {
        return RouterFunctions.route()
                .GET(contextPathProvider.pathFor("/messages"),
                        serverRequest ->
                                Mono.from(messageRepository.messages())
                                        .flatMap(messages ->
                                                ok().body(fromValue(messages))
                                        )
                )
                .build();
    }

}
