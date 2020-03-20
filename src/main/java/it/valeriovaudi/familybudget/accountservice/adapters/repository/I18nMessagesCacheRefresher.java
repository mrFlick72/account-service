package it.valeriovaudi.familybudget.accountservice.adapters.repository;

import it.valeriovaudi.familybudget.accountservice.adapters.cache.ReactiveCacheManager;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.HashMap;

@Controller
public class I18nMessagesCacheRefresher {

    private final ReactiveCacheManager cacheManager;

    public I18nMessagesCacheRefresher(ReactiveCacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @MessageMapping("messages.account-service")
    public Mono<Void> refresh(HashMap<String, String> bundle) {
        return cacheManager.updateCache(bundle)
                .then();
    }
}
