package it.valeriovaudi.familybudget.accountservice.web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.valeriovaudi.familybudget.accountservice.adapters.listener.StoreAccountListener;
import it.valeriovaudi.familybudget.accountservice.domain.repository.AccountRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagingConfig {

    @Bean
    public StoreAccountListener storeAccountListener(ObjectMapper objectMapper,
                                                     AccountRepository accountRepository) {
        return new StoreAccountListener(objectMapper, accountRepository);
    }
}
