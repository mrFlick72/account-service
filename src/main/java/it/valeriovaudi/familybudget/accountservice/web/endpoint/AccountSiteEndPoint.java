package it.valeriovaudi.familybudget.accountservice.web.endpoint;

import it.valeriovaudi.familybudget.accountservice.domain.UpdateAccount;
import it.valeriovaudi.familybudget.accountservice.domain.repository.AccountRepository;
import it.valeriovaudi.familybudget.accountservice.web.adapter.AccountAdapter;
import it.valeriovaudi.familybudget.accountservice.web.model.AccountRepresentation;
import it.valeriovaudi.vauthenticator.security.clientsecuritystarter.user.VAuthenticatorUserNameResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.function.Function;

@Configuration
public class AccountSiteEndPoint {


    private final static String ENDPOINT_PREFIX = "/user-account";
    private final VAuthenticatorUserNameResolver vAuthenticatorUserNameResolver;
    private final AccountRepository accountRepository;
    private final UpdateAccount updateAccount;
    private final AccountAdapter accountAdapter;

    public AccountSiteEndPoint(VAuthenticatorUserNameResolver vAuthenticatorUserNameResolver,
                               AccountRepository accountRepository,
                               UpdateAccount updateAccount, AccountAdapter accountAdapter) {
        this.vAuthenticatorUserNameResolver = vAuthenticatorUserNameResolver;
        this.accountRepository = accountRepository;
        this.updateAccount = updateAccount;
        this.accountAdapter = accountAdapter;
    }

    @Bean
    public RouterFunction accountSiteEndPointRoute() {
        return RouterFunctions.route()
                .GET(ENDPOINT_PREFIX ,
                        serverRequest -> serverRequest.principal()
                                .flatMap(vAuthenticatorUserNameResolver::getUserNameFor)
                                .flatMap(username -> Mono.from(accountRepository.findByMail(username)))
                                .map(accountAdapter::domainToRepresentationModel)
                                .flatMap(accountRepresentation -> ServerResponse.ok().body(BodyInserters.fromValue(accountRepresentation)))
                )

                .PUT(ENDPOINT_PREFIX,
                        serverRequest -> serverRequest.principal()
                                .flatMap(vAuthenticatorUserNameResolver::getUserNameFor)
                                .zipWith(serverRequest.bodyToMono(AccountRepresentation.class))
                                .map(setMailFrom())
                                .flatMap(accountAdapter::siteRepresentationModelToDomainModel)
                                .flatMap(account -> Mono.from(updateAccount.execute(account)))
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