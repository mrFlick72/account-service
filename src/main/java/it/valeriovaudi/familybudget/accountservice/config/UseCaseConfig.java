package it.valeriovaudi.familybudget.accountservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.valeriovaudi.familybudget.accountservice.domain.UpdateAccount;
import it.valeriovaudi.familybudget.accountservice.domain.repository.AccountRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public UpdateAccount updateAccount(AccountRepository accountRepository,
                                       ObjectMapper objectMapper) {
        return new UpdateAccount(accountRepository, objectMapper);
    }
}
