package it.valeriovaudi.familybudget.accountservice.web.security;

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

    public Mono<SecurityAccountDetails> loadUserByUsername(String userName) throws UsernameNotFoundException {
        return Mono.from(accountRepository.findByMail(userName))
                .log()
                .map(account -> new SecurityAccountDetails(account.getMail(),
                        account.getPassword(),
                        true,
                        true,
                        true,
                        account.getEnable(),
                        account.getUserRoles()));

    }
}
