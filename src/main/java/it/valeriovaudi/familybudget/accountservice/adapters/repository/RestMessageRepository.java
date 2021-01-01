package it.valeriovaudi.familybudget.accountservice.adapters.repository;

import it.valeriovaudi.familybudget.accountservice.adapters.cache.ReactiveCacheManager;
import it.valeriovaudi.familybudget.accountservice.domain.repository.MessageRepository;
import org.reactivestreams.Publisher;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

public class RestMessageRepository implements MessageRepository {

    private final String i18nBaseUrl;
    private final String applicationId;
    private final WebClient webClient;
    private final ReactiveCacheManager cacheManager;

    public RestMessageRepository(String i18nBaseUrl,
                                 String applicationId,
                                 WebClient webClient,
                                 ReactiveCacheManager cacheManager) {
        this.i18nBaseUrl = i18nBaseUrl;
        this.cacheManager = cacheManager;
        this.applicationId = applicationId;
        this.webClient = webClient;
    }

    @Override
    public Publisher<Map<String, String>> messages() {
        System.out.println("applicationId: " + applicationId);
        System.out.println("/messages/" + applicationId);
        return cacheManager.getFromCache().log()
                .switchIfEmpty(loader()).log()
                .flatMap(o -> cacheManager.updateCache((Map<String, String>) o));
    }

    private Mono loader() {
        return webClient
                .get().uri(i18nBaseUrl + "/messages/" + applicationId)
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(HashMap.class))
                .map(hashMap -> hashMap);
    }
}
