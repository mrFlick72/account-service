package it.valeriovaudi.familybudget.accountservice.adapters.repository;

import it.valeriovaudi.familybudget.accountservice.domain.repository.MessageRepository;
import org.reactivestreams.Publisher;
import org.springframework.cache.CacheManager;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.cache.CacheMono;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Signal;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class RestMessageRepository implements MessageRepository {

    private static final String CACHE_REGION = "account-service.i18n.messages";
    private static final String CACHE_KEY = "i18n.messages";

    private final String baseUrl;
    private final String applicationId;
    private final WebClient.Builder restTemplate;
    private final CacheManager manager;

    public RestMessageRepository(String baseUrl,
                                 String applicationId,
                                 WebClient.Builder restTemplate, CacheManager manager) {
        this.baseUrl = baseUrl;
        this.applicationId = applicationId;
        this.restTemplate = restTemplate;
        this.manager = manager;
    }

    @Override
    public Publisher<Map<String, String>> messages() {
        Supplier supplier = () -> restTemplate.build().get()
                .uri(baseUrl + "/messages/" + applicationId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<HashMap<String, String>>() {
                })
                .map(stringStringHashMap -> stringStringHashMap);

        return CacheMono.lookup(key -> Mono.<Signal<Map<String, String>>>justOrEmpty((Signal) manager.getCache(CACHE_REGION).get(key, () -> null)), CACHE_KEY)
                .onCacheMissResume(supplier)
                .andWriteWith((key, value) -> Mono.fromRunnable(() -> manager.getCache(CACHE_REGION).put(key, value)));
    }
}
