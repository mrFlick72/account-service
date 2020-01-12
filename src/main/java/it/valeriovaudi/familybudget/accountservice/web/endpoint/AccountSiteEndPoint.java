package it.valeriovaudi.familybudget.accountservice.web.endpoint;

import it.valeriovaudi.familybudget.accountservice.domain.repository.AccountRepository;
import it.valeriovaudi.familybudget.accountservice.web.adapter.AccountAdapter;
import it.valeriovaudi.vauthenticator.security.clientsecuritystarter.user.VAuthenticatorUserNameResolver;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

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
                .build();
    }

/*    private Mono<Authentication> authenticationUserModo() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication);
    }*/

    //todo move to reactive
   /* @GetMapping
    public ResponseEntity account(@AuthenticationPrincipal Authentication principal) {
        Account account = Mono.from(accountRepository.findByMail(vAuthenticatorUserNameResolver.getUserNameFor(principal))).block();
        AccountRepresentation accountRepresentation = accountAdapter.domainToRepresentationModel(account);
        return ResponseEntity.ok(accountRepresentation);
    }*/

   /*

   TODO
    @PutMapping
    public ResponseEntity save(@AuthenticationPrincipal Authentication principal,
                               @RequestBody AccountRepresentation accountRepresentation) {
        accountRepresentation.setMail(vAuthenticatorUserNameResolver.getUserNameFor(principal));
        Account account = accountAdapter.siteRepresentationModelToDomainModel(accountRepresentation);
        accountRepository.update(account);
        return ResponseEntity.noContent().build();
    }*/

}
