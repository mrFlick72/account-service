package it.valeriovaudi.familybudget.accountservice.adapters.repository;

import it.valeriovaudi.familybudget.accountservice.adapters.cache.ReactiveCacheManager;
import it.valeriovaudi.familybudget.accountservice.domain.repository.MessageRepository;
import org.reactivestreams.Publisher;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.messaging.rsocket.RSocketRequester;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

public class RSocketMessageRepository implements MessageRepository {


    private final String applicationId;
    private final Mono<RSocketRequester> requester;
    private final ReactiveCacheManager cacheManeger;

    public RSocketMessageRepository(String applicationId,
                                    Mono<RSocketRequester> requester,
                                    ReactiveCacheManager cacheManeger) {
        this.cacheManeger = cacheManeger;
        this.applicationId = applicationId;
        this.requester = requester;
    }

    @Override
    public Publisher<Map<String, String>> messages() {
        return cacheManeger.getFromCache()
                .switchIfEmpty(loader())
                .flatMap(o -> cacheManeger.updateCache((Map<String, String>) o));
    }

    private Mono<Map<String, String>> loader() {
        return requester.flatMap(req ->
                    req.route("/messages." + applicationId)
                            .retrieveMono(new ParameterizedTypeReference<HashMap<String, String>>() {
                            }).map(stringStringHashMap -> stringStringHashMap));
    }
}
