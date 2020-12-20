package it.valeriovaudi.familybudget.accountservice.adapters.repository;

import it.valeriovaudi.familybudget.accountservice.adapters.cache.ReactiveCacheManager;
import it.valeriovaudi.familybudget.accountservice.domain.repository.MessageRepository;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

public class RestMessageRepository implements MessageRepository {


    private final String i18nBaseUrl;
    private final String applicationId;
    private final WebClient.Builder webClient;
    private final ReactiveCacheManager cacheManager;

    public RestMessageRepository(String i18nBaseUrl,
                                 String applicationId,
                                 WebClient.Builder webClient,
                                 ReactiveCacheManager cacheManager) {
        this.i18nBaseUrl = i18nBaseUrl;
        this.cacheManager = cacheManager;
        this.applicationId = applicationId;
        this.webClient = webClient;
    }

    @Override
    public Publisher<Map<String, String>> messages() {
        return cacheManager.getFromCache()
                .switchIfEmpty(loader())
                .flatMap(o -> cacheManager.updateCache((Map<String, String>) o));
    }

    private Mono<Map<String, String>> loader() {
        return webClient.build()
                .get().uri(i18nBaseUrl + "/messages/" + applicationId)
                .exchange()
                .flatMap(clientResponse -> clientResponse.bodyToMono(HashMap.class))
                .map(hashMap -> hashMap);
    }
}
