package it.valeriovaudi.familybudget.accountservice.adapters.repository;

import org.springframework.cache.CacheManager;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.HashMap;

@Controller
public class I18nMessagesCacheRefresher {
    private static final String CACHE_REGION = "account-service.i18n.messages";
    private static final String CACHE_KEY = "i18n.messages";

    private final CacheManager manager;

    public I18nMessagesCacheRefresher(CacheManager manager) {
        this.manager = manager;
    }

    @MessageMapping("messages.account-service")
    public Mono<Void> refresh(HashMap<String, String> bundle) {
        return Mono.fromRunnable(
                () -> manager.getCache(CACHE_REGION).
                        put(CACHE_KEY, bundle)
        ).then();
    }
}
