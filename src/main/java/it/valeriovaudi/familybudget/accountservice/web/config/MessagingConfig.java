package it.valeriovaudi.familybudget.accountservice.web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.valeriovaudi.familybudget.accountservice.adapters.listener.AccountDetailsListener;
import it.valeriovaudi.familybudget.accountservice.adapters.listener.StoreAccountListener;
import it.valeriovaudi.familybudget.accountservice.domain.repository.AccountRepository;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagingConfig {

    @Bean("getAccountInboundQueue")
    public Queue getAccountInboundQueue() {
        return new Queue("getAccountInboundQueue", false, false, false);
    }

    @Bean("account-registration")
    public Queue accountRegistrationQueue() {
        return new Queue("account-registration", false, false, false);
    }

    @Bean
    public AccountDetailsListener accountDetailsListener(ObjectMapper objectMapper, AccountRepository accountRepository) {
        return new AccountDetailsListener(objectMapper, accountRepository);
    }
    @Bean
    public StoreAccountListener storeAccountListener(ObjectMapper objectMapper, AccountRepository accountRepository) {
        return new StoreAccountListener(objectMapper, accountRepository);
    }
}
