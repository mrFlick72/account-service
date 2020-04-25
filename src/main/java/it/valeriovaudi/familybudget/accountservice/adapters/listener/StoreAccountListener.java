package it.valeriovaudi.familybudget.accountservice.adapters.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.valeriovaudi.familybudget.accountservice.domain.model.Account;
import it.valeriovaudi.familybudget.accountservice.domain.model.Date;
import it.valeriovaudi.familybudget.accountservice.domain.model.Phone;
import it.valeriovaudi.familybudget.accountservice.domain.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.SendTo;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Locale;


@Slf4j
public class StoreAccountListener {
    private final ObjectMapper objectMapper;
    private final AccountRepository accountRepository;

    public StoreAccountListener(ObjectMapper objectMapper, AccountRepository accountRepository) {
        this.objectMapper = objectMapper;
        this.accountRepository = accountRepository;
    }

    @RabbitListener(queues = "account-registration")
    @SendTo("vauthenticator-registration/account-stored")
    public Mono<String> getUserDetails(String accountRepresentation) {
        return extractDataFor(accountRepresentation)
                .map(representation -> new Account(
                        representation.getOrDefault("firstname", ""),
                        representation.getOrDefault("lastname", ""),
                        Date.dateFor("01/01/1970"),
                        representation.getOrDefault("email", ""),
                        Phone.nullValue(),
                        Locale.ENGLISH
                ))
                .flatMap(account -> Mono.from(accountRepository.save(account)))
                .then(Mono.just(accountRepresentation));
    }

    private Mono<HashMap<String, String>> extractDataFor(String accountRepresentation) {
        return Mono.fromCallable(() -> objectMapper.readValue(accountRepresentation, HashMap.class));
    }

}