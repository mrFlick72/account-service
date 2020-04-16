package it.valeriovaudi.familybudget.accountservice.web.config;

import it.valeriovaudi.familybudget.accountservice.domain.repository.AccountRepository;
import it.valeriovaudi.familybudget.accountservice.web.adapter.AccountAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConverterConfig {

    @Bean
    public AccountAdapter accountAdapter(AccountRepository accountRepository){
        return new AccountAdapter(accountRepository);
    }
}
