package it.valeriovaudi.familybudget.accountservice.web.endpoint;

import it.valeriovaudi.familybudget.accountservice.domain.model.Account;
import it.valeriovaudi.familybudget.accountservice.domain.repository.AccountRepository;
import it.valeriovaudi.familybudget.accountservice.web.adapter.AccountAdapter;
import it.valeriovaudi.familybudget.accountservice.web.model.AccountRepresentation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/account")
public class AccountEndPoint {

    private final AccountRepository accountRepository;
    private final AccountAdapter accountAdapter;

    public AccountEndPoint(AccountRepository accountRepository,
                           AccountAdapter accountAdapter) {
        this.accountRepository = accountRepository;
        this.accountAdapter = accountAdapter;
    }

    @GetMapping("/{mail}/mail")
    public ResponseEntity getAccountData(@PathVariable("mail") String mail) {
        return ResponseEntity.ok(accountAdapter.domainToRepresentationModel(accountRepository.findByMail(mail)));
    }

    @PutMapping("/{mail}/mail")
    public ResponseEntity updateAccountData(@PathVariable("mail") String mail,
                                            @RequestBody AccountRepresentation accountRepresentation) {
        accountRepresentation.setMail(mail);
        Account account = accountAdapter.representationModelToDomainModel(accountRepresentation);
        accountRepository.update(account);
        return ResponseEntity.noContent().build();
    }

}