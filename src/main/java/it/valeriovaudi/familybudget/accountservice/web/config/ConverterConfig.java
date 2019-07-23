package it.valeriovaudi.familybudget.accountservice.web.config;

import it.valeriovaudi.familybudget.accountservice.domain.repository.AccountRepository;
import it.valeriovaudi.familybudget.accountservice.web.adapter.AccountAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ConverterConfig {

    @Bean
    public AccountAdapter accountAdapter(PasswordEncoder passwordEncoder, AccountRepository accountRepository){
        return new AccountAdapter(passwordEncoder, accountRepository);
    }
}
