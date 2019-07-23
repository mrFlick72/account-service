package it.valeriovaudi.familybudget.accountservice.web.security;

import it.valeriovaudi.familybudget.accountservice.domain.model.Account;
import it.valeriovaudi.familybudget.accountservice.domain.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Slf4j
public class AccountUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    public AccountUserDetailsService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Account account = accountRepository.findByMail(userName);
        log.info("account: " + account.toString());
        return User.withUsername(account.getMail())
                .password(account.getPassword())
                .accountExpired(!account.getEnable())
                .authorities("USER").build();
    }
}
