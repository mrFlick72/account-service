package it.valeriovaudi.familybudget.accountservice.web.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

@Slf4j
public class UserDetailsProcessorChannelHandler {

    private final ObjectMapper objectMapper;
    private final AccountUserDetailsService accountUserDetailsService;

    public UserDetailsProcessorChannelHandler(ObjectMapper objectMapper, AccountUserDetailsService accountUserDetailsService) {
        this.objectMapper = objectMapper;
        this.accountUserDetailsService = accountUserDetailsService;
    }

    @RabbitListener(queues = "authServerAccountServiceBridgeInboundQueue")
    public String getUserDetails(String userName) {
        log.info("userName: " + userName);

        try {
            return objectMapper.writeValueAsString(accountUserDetailsService.loadUserByUsername(userName));
        } catch (Exception e) {
            log.error("user didn't found");
        }
        return null;
    }

}
