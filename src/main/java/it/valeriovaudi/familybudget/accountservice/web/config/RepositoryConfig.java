package it.valeriovaudi.familybudget.accountservice.web.config;

import it.valeriovaudi.familybudget.accountservice.adapters.repository.JdbcAccountRepository;
import it.valeriovaudi.familybudget.accountservice.adapters.repository.RestMessageRepository;
import it.valeriovaudi.familybudget.accountservice.domain.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;

@Configuration
public class RepositoryConfig {

    @Bean
    public AccountRepository accountRepository(DataSource dataSource) {
        return new JdbcAccountRepository(new JdbcTemplate(dataSource));
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public RestMessageRepository messageRepository(
            @Value("${i18n-messages.base-url:http://i18n-messages}") String i18nBaseUrl,
            @Value("${spring.application.name}") String applicationId,
            RestTemplate restTemplate) {
        return new RestMessageRepository(i18nBaseUrl, applicationId, restTemplate);
    }
}
