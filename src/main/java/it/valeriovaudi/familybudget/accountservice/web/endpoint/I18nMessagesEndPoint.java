package it.valeriovaudi.familybudget.accountservice.web.endpoint;

import it.valeriovaudi.familybudget.accountservice.domain.repository.MessageRepository;
import org.springframework.boot.autoconfigure.web.ServerProperties;
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
    private final ServerProperties serverProperties;

    public I18nMessagesEndPoint(MessageRepository messageRepository, ServerProperties serverProperties) {
        this.messageRepository = messageRepository;
        this.serverProperties = serverProperties;
    }

    @Bean
    public RouterFunction i18nMessagesEndPointRoute() {
        return RouterFunctions.route()
                .GET(serverProperties.getServlet().getContextPath() + "/messages",
                        serverRequest ->
                                Mono.from(messageRepository.messages())
                                        .flatMap(messages ->
                                                ok().body(fromValue(messages))
                                        )
                )
                .build();
    }

}
