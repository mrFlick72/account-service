package it.valeriovaudi.familybudget.accountservice.adapters.cache;

import org.springframework.cache.CacheManager;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Objects;

@Controller
public class I18nMessagesCacheRefresher {

    private final CacheManager cacheManager;

    public I18nMessagesCacheRefresher(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @MessageMapping("messages.account-service")
    public Mono<Void> refresh(HashMap<String, String> bundle) {
        return Mono.fromCallable(() -> Objects.requireNonNull(cacheManager.getCache("messages.account-service.i18n.messages")).invalidate())
                .map(tick -> Mono.fromCallable(() -> cacheManager.getCache("messages.account-service.messages").putIfAbsent("i18n.messages", bundle)))
                .then();
    }
}
