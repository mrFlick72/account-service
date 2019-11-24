package it.valeriovaudi.familybudget.accountservice.web.endpoint;

import it.valeriovaudi.familybudget.accountservice.domain.model.Account;
import it.valeriovaudi.familybudget.accountservice.domain.repository.AccountRepository;
import it.valeriovaudi.familybudget.accountservice.web.adapter.AccountAdapter;
import it.valeriovaudi.familybudget.accountservice.web.model.AccountRepresentation;
import it.valeriovaudi.vauthenticator.security.VAuthenticatorUserNameResolver;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/site/user-info")
public class AccountSiteEndPoint {

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

    @GetMapping
    public ResponseEntity account(@AuthenticationPrincipal Authentication principal) {
        Account account = accountRepository.findByMail(vAuthenticatorUserNameResolver.getUserNameFor(principal));
        AccountRepresentation accountRepresentation = accountAdapter.domainToRepresentationModel(account);
        return ResponseEntity.ok(accountRepresentation);
    }

    @PutMapping
    public ResponseEntity save(@AuthenticationPrincipal Authentication principal,
                               @RequestBody AccountRepresentation accountRepresentation) {
        accountRepresentation.setMail(vAuthenticatorUserNameResolver.getUserNameFor(principal));
        Account account = accountAdapter.siteRepresentationModelToDomainModel(accountRepresentation);
        accountRepository.update(account);
        return ResponseEntity.noContent().build();
    }
}
