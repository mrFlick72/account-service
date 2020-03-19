package it.valeriovaudi.familybudget.accountservice;

import it.valeriovaudi.vauthenticator.security.clientsecuritystarter.security.RedisOAuth2AuthorizedClientService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

@EnableCaching
@EnableIntegration
@SpringBootApplication
public class AccountServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountServiceApplication.class, args);
    }

    @Bean
    public RedisOAuth2AuthorizedClientService redisOAuth2AuthorizedClientService(RedisTemplate redisTemplate,
                                                                                 ClientRegistrationRepository clientRegistrationRepository) {
        return new RedisOAuth2AuthorizedClientService(redisTemplate, clientRegistrationRepository);
    }
}
