package it.valeriovaudi.familybudget.accountservice.domain;

import it.valeriovaudi.familybudget.accountservice.domain.model.Account;
import it.valeriovaudi.familybudget.accountservice.domain.repository.AccountRepository;
import org.reactivestreams.Publisher;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import reactor.core.publisher.Mono;

import java.util.Map;

public class UpdateAccount {
    private final AccountRepository accountRepository;
    private final RabbitTemplate template;

    public UpdateAccount(AccountRepository accountRepository,
                         RabbitTemplate template) {
        this.accountRepository = accountRepository;
        this.template = template;
    }

    public Publisher<Void> execute(Account account) {
        return Mono.from(accountRepository.save(account))
                .then(Mono.fromRunnable(() -> template.convertAndSend("account-sync",
                        "account-sync",
                        Map.of("firstName", account.getFirstName(), "lastName", account.getLastName()))
                ));
    }

}
