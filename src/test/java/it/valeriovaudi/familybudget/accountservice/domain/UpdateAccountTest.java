package it.valeriovaudi.familybudget.accountservice.domain;

import it.valeriovaudi.familybudget.accountservice.domain.model.Account;
import it.valeriovaudi.familybudget.accountservice.domain.model.Date;
import it.valeriovaudi.familybudget.accountservice.domain.model.Phone;
import it.valeriovaudi.familybudget.accountservice.domain.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Locale;
import java.util.Map;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateAccountTest {

    @Mock
    AccountRepository accountRepository;

    @Mock
    RabbitTemplate template;

    @Test
    void execute() {
        var account = new Account("Valerio",
                "Vaudi",
                Date.dateFor("01/01/1970"),
                "valerio.vaudi123@test.com",
                Phone.phoneFor("+39 333 2255112"),
                Locale.ENGLISH
        );
        var accountUpdate = new UpdateAccount(accountRepository, template);

        when(accountRepository.save(account))
                .thenReturn(Mono.empty());

        var expected = accountUpdate.execute(account);

        StepVerifier.create(expected)
                .expectNext()
                .expectNext()
                .verifyComplete();

        verify(accountRepository, times(1))
                .save(account);
        verify(template, times(1))
                .convertAndSend("account-sync", "account-sync", Map.of("firstName", "Valerio", "lastName", "Vaudi"));
    }
}