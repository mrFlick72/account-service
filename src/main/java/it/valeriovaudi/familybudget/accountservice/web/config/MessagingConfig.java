package it.valeriovaudi.familybudget.accountservice.web.config;

import it.valeriovaudi.familybudget.accountservice.web.security.AccountUserDetailsService;
import it.valeriovaudi.familybudget.accountservice.web.security.UserDetailsProcessorChannelHandlrer;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagingConfig {

    @Bean("authServerAccountServiceBridgeInboundQueue")
    public Queue authServerAccountServiceBridgeInboundQueue(){
        return new Queue("authServerAccountServiceBridgeInboundQueue", false, false, false);
    }

    @Bean
    public UserDetailsProcessorChannelHandlrer userDetailsProcessorChannelHandlrer(AccountUserDetailsService accountUserDetailsService){
        return new UserDetailsProcessorChannelHandlrer(accountUserDetailsService);
    }
}
