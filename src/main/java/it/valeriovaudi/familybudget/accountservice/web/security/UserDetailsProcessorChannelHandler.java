package it.valeriovaudi.familybudget.accountservice.web.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

@Slf4j
public class UserDetailsProcessorChannelHandler {

    private final AccountUserDetailsService accountUserDetailsService;

    public UserDetailsProcessorChannelHandler(AccountUserDetailsService accountUserDetailsService) {
        this.accountUserDetailsService = accountUserDetailsService;
    }

    @RabbitListener(queues = "authServerAccountServiceBridgeInboundQueue")
    public SecurityAccountDetails getUserDetails(String userName) {
        log.info("userName: " + userName);

        try {
            return accountUserDetailsService.loadUserByUsername(userName);
        } catch (Exception e) {
            log.error("user didn't found");
        }
        return null;
    }

}
