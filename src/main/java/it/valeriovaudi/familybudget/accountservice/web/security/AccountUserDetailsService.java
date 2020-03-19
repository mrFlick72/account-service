package it.valeriovaudi.familybudget.accountservice.web.security;

import it.valeriovaudi.familybudget.accountservice.domain.model.Account;
import it.valeriovaudi.familybudget.accountservice.domain.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Slf4j
public class AccountUserDetailsService {

    private final AccountRepository accountRepository;

    public AccountUserDetailsService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public SecurityAccountDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Account account = accountRepository.findByMail(userName);
        return new SecurityAccountDetails(account.getMail(),
                account.getPassword(),
                true,
                true,
                true,
                account.getEnable(),
                account.getUserRoles());

    }
}
