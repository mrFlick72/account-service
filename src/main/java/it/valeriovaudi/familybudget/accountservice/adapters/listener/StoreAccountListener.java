package it.valeriovaudi.familybudget.accountservice.adapters.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.valeriovaudi.familybudget.accountservice.domain.model.Account;
import it.valeriovaudi.familybudget.accountservice.domain.model.Date;
import it.valeriovaudi.familybudget.accountservice.domain.model.Phone;
import it.valeriovaudi.familybudget.accountservice.domain.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.SendTo;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Locale;


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
    @SendTo("vauthenticator-registration/account-stored")
    public String getUserDetails(String accountRepresentation) {
        log.debug("mail: " + accountRepresentation);

        try {
            HashMap<String, String> hashMap = objectMapper.readValue(accountRepresentation, HashMap.class);
            System.out.println(hashMap);
            String email = hashMap.getOrDefault("email", "");
            Account account = new Account(
                    hashMap.getOrDefault("firstname", ""),
                    hashMap.getOrDefault("lastname", ""),
                    Date.dateFor("01/01/1970"),
                    email,
                    Phone.nullValue(),
                    Locale.ENGLISH
            );
            Mono.from(accountRepository.save(account)).subscribe();
            return accountRepresentation;
        } catch (RuntimeException | JsonProcessingException e) {
            log.error("user didn't found");
            throw new RuntimeException();
        }
    }

}
