package it.valeriovaudi.familybudget.accountservice.domain.repository;

import it.valeriovaudi.familybudget.accountservice.domain.model.Account;

public interface AccountRepository {

    Account findByMail(String mail);

    void save(Account account);

    void update(Account account);
}
