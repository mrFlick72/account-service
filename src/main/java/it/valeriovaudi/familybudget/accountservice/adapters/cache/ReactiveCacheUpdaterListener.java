package it.valeriovaudi.familybudget.accountservice.adapters.cache;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.Slf4JLoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageResponse;

import java.time.Duration;
import java.util.List;

import static reactor.core.publisher.Mono.fromCompletionStage;

public class ReactiveCacheUpdaterListener implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(ReactiveCacheUpdaterListener.class);

    private final ReceiveMessageRequestFactory factory;
    private final Duration sleepingTime;
    private final Flux whileLoopFluxProvider;
    private final ReactiveCacheManager reactiveCacheManager;
    private final SqsAsyncClient sqsAsyncClient;

    public ReactiveCacheUpdaterListener(
            Duration sleepingTime,
            Flux whileLoopFluxProvider,
            ReceiveMessageRequestFactory factory,
            ReactiveCacheManager reactiveCacheManager,
            SqsAsyncClient sqsAsyncClient) {
        this.sleepingTime = sleepingTime;
        this.whileLoopFluxProvider = whileLoopFluxProvider;
        this.factory = factory;
        this.reactiveCacheManager = reactiveCacheManager;
        this.sqsAsyncClient = sqsAsyncClient;
    }

    public Flux listen() {
        return whileLoopFluxProvider
                .delayElements(sleepingTime)
                .log()
                .flatMap(req -> handleMessage())
                .flatMap(signal -> reactiveCacheManager.evictCache())
                .doOnComplete(() -> logger.info("subscription completed"))
                .doOnCancel(() -> logger.info("subscription cancelled"))
                .doOnSubscribe((s) -> logger.info("subscription started"))
                .doOnError(Exception.class, (e) -> logger.error("subscription error: ", e));
    }

    private Mono<List<DeleteMessageResponse>> handleMessage() {
        return Flux.from(fromCompletionStage(sqsAsyncClient.receiveMessage(factory.makeAReceiveMessageRequest())))
                .flatMap(response -> Flux.fromIterable(response.messages()))
                .flatMap(message -> fromCompletionStage(sqsAsyncClient.deleteMessage(factory.makeADeleteMessageRequest(message.receiptHandle()))))
                .collectList();
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        listen().subscribe();
    }
}
