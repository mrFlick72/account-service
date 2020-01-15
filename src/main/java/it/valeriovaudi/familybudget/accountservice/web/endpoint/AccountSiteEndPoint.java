package it.valeriovaudi.familybudget.accountservice.web.endpoint;

import it.valeriovaudi.familybudget.accountservice.domain.repository.AccountRepository;
import it.valeriovaudi.familybudget.accountservice.web.adapter.AccountAdapter;
import it.valeriovaudi.familybudget.accountservice.web.model.AccountRepresentation;
import it.valeriovaudi.vauthenticator.security.clientsecuritystarter.user.VAuthenticatorUserNameResolver;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.security.Principal;
import java.util.function.Function;

@Configuration
public class AccountSiteEndPoint {

    private final static String ENDPOINT_PREFIX = "/site/user-info";

    private final VAuthenticatorUserNameResolver vAuthenticatorUserNameResolver;
    private final AccountRepository accountRepository;
    private final AccountAdapter accountAdapter;

    public AccountSiteEndPoint(VAuthenticatorUserNameResolver vAuthenticatorUserNameResolver,
                               AccountRepository accountRepository,
                               AccountAdapter accountAdapter) {
        this.vAuthenticatorUserNameResolver = vAuthenticatorUserNameResolver;
        this.accountRepository = accountRepository;
        this.accountAdapter = accountAdapter;
    }

    @Bean
    public RouterFunction accountSiteEndPointRoute(ServerProperties serverProperties) {
        return RouterFunctions.route()
                .GET(serverProperties.getServlet().getContextPath() + ENDPOINT_PREFIX,
                        serverRequest -> serverRequest.principal()
                                .flatMap(vAuthenticatorUserNameResolver::getUserNameFor)
                                .flatMap(username -> Mono.from(accountRepository.findByMail(username)))
                                .map(accountAdapter::domainToRepresentationModel)
                                .flatMap(accountRepresentation -> ServerResponse.ok().body(BodyInserters.fromValue(accountRepresentation)))
                )

                .PUT(serverProperties.getServlet().getContextPath() + ENDPOINT_PREFIX,
                        serverRequest -> serverRequest.principal()
                                .flatMap(vAuthenticatorUserNameResolver::getUserNameFor)
                                .zipWith(serverRequest.bodyToMono(AccountRepresentation.class))
                                .map(setMailFrom())
                                .map(accountAdapter::siteRepresentationModelToDomainModel)
                                .flatMap(account -> Mono.from(accountRepository.update(account)))
                                .flatMap(account -> ServerResponse.noContent().build())
                )

                .build();
    }

    private Function<Tuple2<String, AccountRepresentation>, AccountRepresentation> setMailFrom() {
        return tuple -> {
            tuple.getT2().setMail(tuple.getT1());
            return tuple.getT2();
        };
    }
}

@RestController
class USerEndpoint {

    @GetMapping("/account/authentication")
    public Principal authentication(Principal authentication) {
        return authentication;

    }
}