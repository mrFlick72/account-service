package it.valeriovaudi.familybudget.accountservice.adapters.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.valeriovaudi.familybudget.accountservice.domain.model.Account;
import it.valeriovaudi.familybudget.accountservice.domain.model.Date;
import it.valeriovaudi.familybudget.accountservice.domain.model.Phone;
import it.valeriovaudi.familybudget.accountservice.domain.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


@Slf4j
public class StoreAccountListener {
    private final ObjectMapper objectMapper;
    private final AccountRepository accountRepository;
    private final RabbitTemplate rabbitTemplate;

    public StoreAccountListener(ObjectMapper objectMapper, AccountRepository accountRepository, RabbitTemplate rabbitTemplate) {
        this.objectMapper = objectMapper;
        this.accountRepository = accountRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = "account-registration")
    public void getUserDetails(String accountRepresentation) {
        log.debug("mail: " + accountRepresentation);

        try {
            HashMap<String, String> hashMap = objectMapper.readValue(accountRepresentation, HashMap.class);
            System.out.println(hashMap);
            String email = hashMap.getOrDefault("email", "");
            String emailBody = objectMapper.writeValueAsString(Map.of("email", email));
            Account account = new Account(
                    hashMap.getOrDefault("firstname", ""),
                    hashMap.getOrDefault("lastname", ""),
                    Date.dateFor("01/01/1970"),
                    email,
                    Phone.nullValue(),
                    Locale.ENGLISH
            );
            Mono<Object> rabbitSender =
                    Mono.fromRunnable(
                            () -> rabbitTemplate.convertAndSend("123", "vauthenticator-registration",emailBody)
                    );

            Mono.from(accountRepository.save(account))
                    .flatMap(aVoid -> rabbitSender)
                    .subscribe();

        } catch (Exception e) {
            log.error("user didn't found");
        }
    }

}
