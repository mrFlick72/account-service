package it.valeriovaudi.familybudget.accountservice.adapters.repository;

import it.valeriovaudi.familybudget.accountservice.domain.repository.MessageRepository;
import org.reactivestreams.Publisher;
import org.springframework.cache.CacheManager;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.messaging.rsocket.RSocketRequester;
import reactor.cache.CacheMono;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Signal;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class RSocketMessageRepository implements MessageRepository {

    private static final String CACHE_REGION = "account-service.i18n.messages";
    private static final String CACHE_KEY = "i18n.messages";

    private final String applicationId;
    private final Mono<RSocketRequester> requester;
    private final CacheManager manager;

    public RSocketMessageRepository(String applicationId,
                                    Mono<RSocketRequester> requester,
                                    CacheManager manager) {
        this.applicationId = applicationId;
        this.requester = requester;
        this.manager = manager;
    }

    @Override
    public Publisher<Map<String, String>> messages() {
        Supplier supplier = () -> requester.flatMap(req ->
                req.route("messages." + applicationId)
                        .retrieveMono(new ParameterizedTypeReference<HashMap<String, String>>() {
                        }).map(stringStringHashMap -> stringStringHashMap));


        CacheMono.lookup(key -> Mono.<Signal<Map<String, String>>>justOrEmpty((Signal) manager.getCache(CACHE_REGION).get(key, () -> null)), CACHE_KEY)
                .onCacheMissResume(supplier)
                .andWriteWith((key, value) -> Mono.fromRunnable(() -> manager.getCache(CACHE_REGION).put(key, value)));


        return (Publisher<Map<String, String>>) supplier.get();
    }
}
