package it.valeriovaudi.familybudget.accountservice.web.endpoint;

import it.valeriovaudi.familybudget.accountservice.web.adapter.AccountAdapter;
import it.valeriovaudi.vauthenticator.security.clientsecuritystarter.user.VAuthenticatorUserNameResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class AdapterTestConfig {

    @Bean
    public AccountAdapter accountAdapter() {
        return new AccountAdapter();
    }

    @Bean
    public VAuthenticatorUserNameResolver vAuthenticatorUserNameResolver(){
        return new VAuthenticatorUserNameResolver("email");
    }
}
