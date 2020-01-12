package it.valeriovaudi.familybudget.accountservice.adapters.repository;

import it.valeriovaudi.familybudget.accountservice.domain.repository.MessageRepository;
import org.reactivestreams.Publisher;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

public class RestMessageRepository implements MessageRepository {

    private final String baseUrl;
    private final String applicationId;
    private final WebClient.Builder restTemplate;

    public RestMessageRepository(String baseUrl,
                                 String applicationId,
                                 WebClient.Builder restTemplate) {
        this.baseUrl = baseUrl;
        this.applicationId = applicationId;
        this.restTemplate = restTemplate;
    }

    @Override
    @Cacheable("account-service.i18n.messages")
    public Publisher<Map<String, String>> messages() {
        return restTemplate.build().get()
                .uri(baseUrl + "/messages/" + applicationId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<HashMap<String, String>>() {})
                .map(stringStringHashMap -> stringStringHashMap);

    }
}
