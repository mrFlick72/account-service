package it.valeriovaudi.familybudget.accountservice.web.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.valeriovaudi.familybudget.accountservice.domain.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
public class AccountDetailsListener {

    private final ObjectMapper objectMapper;
    private final AccountRepository accountRepository;

    public AccountDetailsListener(ObjectMapper objectMapper, AccountRepository accountRepository) {
        this.objectMapper = objectMapper;
        this.accountRepository = accountRepository;
    }

    //todo move to rective
    @RabbitListener(queues = "getAccountInboundQueue")
    public String getUserDetails(String mail) {
        log.info("mail: " + mail);

        try {
            var account = Mono.from(accountRepository.findByMail(mail)).blockOptional(Duration.ofSeconds(10));
            System.out.println(account);
            return objectMapper.writeValueAsString(account);
        } catch (Exception e) {
            log.error("user didn't found");
        }
        return null;
    }

}
