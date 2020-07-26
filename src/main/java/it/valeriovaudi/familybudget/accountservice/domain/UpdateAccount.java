package it.valeriovaudi.familybudget.accountservice.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.valeriovaudi.familybudget.accountservice.domain.model.Account;
import it.valeriovaudi.familybudget.accountservice.domain.repository.AccountRepository;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import reactor.core.publisher.Mono;

import java.util.Map;

public class UpdateAccount {
    private final static Logger LOGGER = LoggerFactory.getLogger(UpdateAccount.class);

    private final AccountRepository accountRepository;
    private final RabbitTemplate template;
    private final ObjectMapper objectMapper;

    public UpdateAccount(AccountRepository accountRepository,
                         RabbitTemplate template, ObjectMapper objectMapper) {
        this.accountRepository = accountRepository;
        this.template = template;
        this.objectMapper = objectMapper;
    }

    public Publisher<Void> execute(Account account) {
        LOGGER.info("fire account update event use case level");
        return Mono.from(accountRepository.update(account))
                .then(Mono.fromRunnable(() -> {
                            LOGGER.info("fire account update event");
                            String payload = payloadFor(account);
                            template.convertAndSend("account-sync", "account-sync", payload);
                            LOGGER.info("account update event fired");
                        }
                ));
    }

    private String payloadFor(Account account) {
        Map<String, String> payload = Map.of(
                "email", account.getMail(),
                "firstName", account.getFirstName(),
                "lastName", account.getLastName()
        );
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

}
