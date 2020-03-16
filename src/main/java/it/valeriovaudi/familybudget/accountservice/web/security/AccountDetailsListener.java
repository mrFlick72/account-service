package it.valeriovaudi.familybudget.accountservice.web.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.valeriovaudi.familybudget.accountservice.domain.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

@Slf4j
public class AccountDetailsListener {

    private final ObjectMapper objectMapper;
    private final AccountRepository accountRepository;

    public AccountDetailsListener(ObjectMapper objectMapper, AccountRepository accountRepository) {
        this.objectMapper = objectMapper;
        this.accountRepository = accountRepository;
    }

    @RabbitListener(queues = "getAccountInboundQueue")
    public String getUserDetails(String mail) {
        log.info("mail: " + mail);

        try {
            var account = accountRepository.findByMail(mail);
            System.out.println(account);
            return objectMapper.writeValueAsString(account);
        } catch (Exception e) {
            log.error("user didn't found");
        }
        return null;
    }

}
