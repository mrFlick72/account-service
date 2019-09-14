package it.valeriovaudi.familybudget.accountservice.web.config;

import it.valeriovaudi.familybudget.accountservice.domain.repository.AccountRepository;
import it.valeriovaudi.familybudget.accountservice.web.security.AccountUserDetailsService;
import it.valeriovaudi.familybudget.accountservice.web.security.GlobalFrontChannelLogoutProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

@Configuration
public class SecurityConfig {

    @Bean
    public AccountUserDetailsService accountUserDetailsService(AccountRepository accountRepository) {
        return new AccountUserDetailsService(accountRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public GlobalFrontChannelLogoutProvider globalFrontChannelLogoutProvider(@Value("${postLogoutRedirectUri}") String postLogoutRedirectUri,
                                                                             @Value("${auth.oidcIss}") String oidConnectDiscoveryEndPoint) {
        return new GlobalFrontChannelLogoutProvider(postLogoutRedirectUri,
                oidConnectDiscoveryEndPoint + "/.well-known/openid-configuration",
                new RestTemplate());
    }
}
