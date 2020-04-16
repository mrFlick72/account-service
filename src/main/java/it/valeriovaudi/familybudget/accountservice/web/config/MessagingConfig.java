package it.valeriovaudi.familybudget.accountservice.web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.valeriovaudi.familybudget.accountservice.domain.repository.AccountRepository;
import it.valeriovaudi.familybudget.accountservice.web.security.AccountDetailsListener;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagingConfig {

    @Bean("getAccountInboundQueue")
    public Queue getAccountInboundQueue() {
        return new Queue("getAccountInboundQueue", false, false, false);
    }

    @Bean
    public AccountDetailsListener accountDetailsListener(ObjectMapper objectMapper, AccountRepository accountRepository) {
        return new AccountDetailsListener(objectMapper, accountRepository);
    }
}
