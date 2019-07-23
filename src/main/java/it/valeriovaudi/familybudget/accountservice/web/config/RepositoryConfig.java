package it.valeriovaudi.familybudget.accountservice.web.config;

import it.valeriovaudi.familybudget.accountservice.adapters.repository.JdbcAccountRepository;
import it.valeriovaudi.familybudget.accountservice.domain.repository.AccountRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class RepositoryConfig {

    @Bean
    public AccountRepository accountRepository(DataSource dataSource){
        return new JdbcAccountRepository(new JdbcTemplate(dataSource));
    }
}
