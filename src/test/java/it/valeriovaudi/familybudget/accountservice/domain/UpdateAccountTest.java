package it.valeriovaudi.familybudget.accountservice.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.valeriovaudi.familybudget.accountservice.domain.model.Account;
import it.valeriovaudi.familybudget.accountservice.domain.model.Date;
import it.valeriovaudi.familybudget.accountservice.domain.model.Phone;
import it.valeriovaudi.familybudget.accountservice.domain.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Locale;
import java.util.Map;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateAccountTest {

    @Mock
    AccountRepository accountRepository;

    @Test
    void execute() throws JsonProcessingException {
        var account = new Account("Valerio",
                "Vaudi",
                Date.dateFor("01/01/1970"),
                "valerio.vaudi123@test.com",
                Phone.phoneFor("+39 333 2255112"),
                Locale.ENGLISH
        );
        ObjectMapper objectMapper = new ObjectMapper();
        var accountUpdate = new UpdateAccount(accountRepository, objectMapper);
        var payload = objectMapper.writeValueAsString(
                Map.of("email", "valerio.vaudi123@test.com",
                        "firstName", "Valerio",
                        "lastName", "Vaudi")
        );

        when(accountRepository.update(account))
                .thenReturn(Mono.empty());

        var expected = accountUpdate.execute(account);

        StepVerifier.create(expected)
                .expectNext()
                .expectNext()
                .verifyComplete();

        verify(accountRepository, times(1))
                .update(account);
    }
}