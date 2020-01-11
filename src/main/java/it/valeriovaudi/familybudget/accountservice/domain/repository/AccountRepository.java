package it.valeriovaudi.familybudget.accountservice.domain.repository;

import it.valeriovaudi.familybudget.accountservice.domain.model.Account;
import org.reactivestreams.Publisher;


public interface AccountRepository {

    Account findByMail(String mail);

    Publisher<Void> save(Account account);

    void update(Account account);
}
