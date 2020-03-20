package it.valeriovaudi.familybudget.accountservice.domain.repository;

import it.valeriovaudi.familybudget.accountservice.domain.model.Account;
import org.reactivestreams.Publisher;


public interface AccountRepository {

    Publisher<Account> findByMail(String mail);

    Publisher<Void> save(Account account);

    Publisher<Void>  update(Account account);
}
