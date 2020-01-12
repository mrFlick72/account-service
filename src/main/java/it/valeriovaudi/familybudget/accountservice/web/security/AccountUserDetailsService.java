package it.valeriovaudi.familybudget.accountservice.web.security;

import it.valeriovaudi.familybudget.accountservice.domain.model.Account;
import it.valeriovaudi.familybudget.accountservice.domain.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import reactor.core.publisher.Mono;

@Slf4j
public class AccountUserDetailsService {

    private final AccountRepository accountRepository;

    public AccountUserDetailsService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    //todo move to reactive
    public SecurityAccountDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Account account = Mono.from(accountRepository.findByMail(userName)).block();
        log.info("account: " + account.toString());

        return new SecurityAccountDetails(account.getMail(),
                account.getPassword(),
                true,
                true,
                true,
                account.getEnable(),
                account.getUserRoles());
    }
}
