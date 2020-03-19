package it.valeriovaudi.familybudget.accountservice.adapters.repository;

import it.valeriovaudi.familybudget.accountservice.domain.repository.MessageRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class RestMessageRepository implements MessageRepository {

    private final String baseUrl;
    private final String applicationId;
    private final RestTemplate restTemplate;

    public RestMessageRepository(String baseUrl,
                                 String applicationId,
                                 RestTemplate restTemplate) {
        this.baseUrl = baseUrl;
        this.applicationId = applicationId;
        this.restTemplate = restTemplate;
    }

    @Override
    @Cacheable(value = "account-service.i18n.messages", key = "'i18n.messages'")
    public Map<String, String> messages() {
        return restTemplate.getForObject(baseUrl + "/messages/" + applicationId, HashMap.class);
    }
}
