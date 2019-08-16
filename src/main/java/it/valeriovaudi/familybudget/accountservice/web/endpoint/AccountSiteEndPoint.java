package it.valeriovaudi.familybudget.accountservice.web.endpoint;

import it.valeriovaudi.familybudget.accountservice.domain.model.Account;
import it.valeriovaudi.familybudget.accountservice.domain.repository.AccountRepository;
import it.valeriovaudi.familybudget.accountservice.web.adapter.AccountAdapter;
import it.valeriovaudi.familybudget.accountservice.web.model.AccountRepresentation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/site/user-info")
public class AccountSiteEndPoint {

    private final AccountRepository accountRepository;
    private final AccountAdapter accountAdapter;

    public AccountSiteEndPoint(AccountRepository accountRepository,
                               AccountAdapter accountAdapter) {
        this.accountRepository = accountRepository;
        this.accountAdapter = accountAdapter;
    }

    @GetMapping
    public ResponseEntity account(@AuthenticationPrincipal Principal principal) {
        Account account = accountRepository.findByMail(principal.getName());
        AccountRepresentation accountRepresentation = accountAdapter.domainToRepresentationModel(account);
        return ResponseEntity.ok(accountRepresentation);
    }

    @PutMapping
    public ResponseEntity save(@AuthenticationPrincipal Principal principal,
                               @RequestBody AccountRepresentation accountRepresentation) {
        accountRepresentation.setMail(principal.getName());
        Account account = accountAdapter.siteRepresentationModelToDomainModel(accountRepresentation);
        accountRepository.update(account);
        return ResponseEntity.noContent().build();
    }
}
