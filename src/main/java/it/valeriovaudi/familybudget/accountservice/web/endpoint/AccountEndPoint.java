package it.valeriovaudi.familybudget.accountservice.web.endpoint;

import it.valeriovaudi.familybudget.accountservice.domain.repository.AccountRepository;
import it.valeriovaudi.familybudget.accountservice.web.adapter.AccountAdapter;
import it.valeriovaudi.familybudget.accountservice.web.model.AccountRepresentation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
public class AccountEndPoint {

    private final AccountRepository accountRepository;
    private final AccountAdapter accountAdapter;
    private final ContextPathProvider contextPathProvider;

    public AccountEndPoint(AccountRepository accountRepository,
                           AccountAdapter accountAdapter,
                           ContextPathProvider contextPathProvider) {
        this.accountRepository = accountRepository;
        this.accountAdapter = accountAdapter;
        this.contextPathProvider = contextPathProvider;
    }

    @Bean
    public RouterFunction accountEndPointRoute() {
        String uri = contextPathProvider.pathFor("/{mail}/mail");
        return RouterFunctions.route()

                .GET(uri, serverRequest ->
                        Mono.from(accountRepository.findByMail(serverRequest.pathVariable("mail")))
                                .map(accountAdapter::domainToRepresentationModel)
                                .flatMap(accountRepresentation ->
                                        ServerResponse.ok().body(BodyInserters.fromValue(accountRepresentation)))

                )

                .PUT(uri, serverRequest ->
                        serverRequest.bodyToMono(AccountRepresentation.class)
                                .map(accountRepresentation -> {
                                    accountRepresentation.setMail(serverRequest.pathVariable("mail"));
                                    return accountRepresentation;
                                })
                                .flatMap(accountAdapter::representationModelToDomainModel)
                                .flatMap(account -> Mono.from(accountRepository.update(account)))
                                .flatMap(ack -> ServerResponse.noContent().build())
                )
                .build();
    }

}