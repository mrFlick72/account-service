package it.valeriovaudi.familybudget.accountservice.web.adapter;

import it.valeriovaudi.familybudget.accountservice.domain.model.Account;
import it.valeriovaudi.familybudget.accountservice.domain.model.Date;
import it.valeriovaudi.familybudget.accountservice.domain.model.Phone;
import it.valeriovaudi.familybudget.accountservice.domain.repository.AccountRepository;
import it.valeriovaudi.familybudget.accountservice.web.model.AccountRepresentation;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;

import java.util.Locale;
import java.util.Optional;

public class AccountAdapter {

    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;

    public AccountAdapter(PasswordEncoder passwordEncoder, AccountRepository accountRepository) {
        this.passwordEncoder = passwordEncoder;
        this.accountRepository = accountRepository;
    }

    public AccountRepresentation domainToRepresentationModel(Account account) {
        return new AccountRepresentation(account.getFirstName(),
                account.getLastName(),
                account.getBirthDate().formattedDate(),
                account.getMail(),
                null,
                account.getUserRoles(),
                account.getPhone().formattedPhone());
    }

    //todo move to reactive
    public Account representationModelToDomainModel(AccountRepresentation accountRepresentation) {
        return new Account(accountRepresentation.getFirstName(), accountRepresentation.getLastName(),
                Date.dateFor(accountRepresentation.getBirthDate()), accountRepresentation.getMail(),
                Optional.ofNullable(accountRepresentation.getPassword()).map(passwordEncoder::encode)
                        .orElseGet(() ->{
                            var account = Mono.from(accountRepository.findByMail(accountRepresentation.getMail())).block();
                            return account.getPassword();
                        }),
                accountRepresentation.getUserRoles(), Phone.phoneFor(accountRepresentation.getPhone()), true, Locale.ENGLISH);
    }

    //todo move to reactive
    public Account siteRepresentationModelToDomainModel(AccountRepresentation accountRepresentation) {

        var account = Mono.from(accountRepository.findByMail(accountRepresentation.getMail())).block();
        return new Account(accountRepresentation.getFirstName(), accountRepresentation.getLastName(),
                Date.dateFor(accountRepresentation.getBirthDate()), accountRepresentation.getMail(),
                Optional.ofNullable(accountRepresentation.getPassword()).map(passwordEncoder::encode)
                        .orElseGet(account::getPassword),
                account.getUserRoles(), Phone.phoneFor(accountRepresentation.getPhone()), true, Locale.ENGLISH);
    }

}
