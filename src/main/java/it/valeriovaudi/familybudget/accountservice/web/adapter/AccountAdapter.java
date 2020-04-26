package it.valeriovaudi.familybudget.accountservice.web.adapter;

import it.valeriovaudi.familybudget.accountservice.domain.model.Account;
import it.valeriovaudi.familybudget.accountservice.domain.model.Date;
import it.valeriovaudi.familybudget.accountservice.domain.model.Phone;
import it.valeriovaudi.familybudget.accountservice.domain.repository.AccountRepository;
import it.valeriovaudi.familybudget.accountservice.web.model.AccountRepresentation;
import reactor.core.publisher.Mono;

import java.util.Locale;

public class AccountAdapter {
    private final AccountRepository accountRepository;

    public AccountAdapter(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public AccountRepresentation domainToRepresentationModel(Account account) {
        return new AccountRepresentation(account.getFirstName(),
                account.getLastName(),
                account.getBirthDate().formattedDate(),
                account.getMail(),
                account.getPhone().formattedPhone());
    }

    public Mono<Account> representationModelToDomainModel(AccountRepresentation accountRepresentation) {
        return Mono.from(accountRepository.findByMail(accountRepresentation.getMail()))
                .map(account -> new Account(accountRepresentation.getFirstName(),
                        accountRepresentation.getLastName(),
                        Date.dateFor(accountRepresentation.getBirthDate()),
                        accountRepresentation.getMail(),
                        Phone.phoneFor(accountRepresentation.getPhone()),
                        Locale.ENGLISH));
    }

    public Mono<Account> siteRepresentationModelToDomainModel(AccountRepresentation accountRepresentation) {
        return Mono.from(accountRepository.findByMail(accountRepresentation.getMail()))
                .map(account -> new Account(accountRepresentation.getFirstName(),
                        accountRepresentation.getLastName(),
                        Date.dateFor(accountRepresentation.getBirthDate()),
                        accountRepresentation.getMail(),
                        Phone.phoneFor(accountRepresentation.getPhone()),
                        Locale.ENGLISH));

    }

}
