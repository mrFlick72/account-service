package it.valeriovaudi.familybudget.accountservice.adapters.repository;

import it.valeriovaudi.familybudget.accountservice.domain.repository.MessageRepository;
import org.reactivestreams.Publisher;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

public class RestMessageRepository implements MessageRepository {

    private final String i18nBaseUrl;
    private final String applicationId;
    private final WebClient webClient;

    public RestMessageRepository(String i18nBaseUrl,
                                 String applicationId,
                                 WebClient webClient) {
        this.i18nBaseUrl = i18nBaseUrl;
        this.applicationId = applicationId;
        this.webClient = webClient;
    }

    @Override
    public Publisher<Map<String, String>> messages() {
        return webClient
                .get().uri(i18nBaseUrl + "/messages/" + applicationId)
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(HashMap.class))
                .map(hashMap -> hashMap);
    }

}
