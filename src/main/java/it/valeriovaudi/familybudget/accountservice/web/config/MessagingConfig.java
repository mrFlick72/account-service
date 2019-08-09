package it.valeriovaudi.familybudget.accountservice.web.config;

import it.valeriovaudi.familybudget.accountservice.web.security.AccountUserDetailsService;
import it.valeriovaudi.familybudget.accountservice.web.security.UserDetailsProcessorChannelHandler;
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
    public UserDetailsProcessorChannelHandler userDetailsProcessorChannelHandlrer(AccountUserDetailsService accountUserDetailsService){
        return new UserDetailsProcessorChannelHandler(accountUserDetailsService);
    }
}
