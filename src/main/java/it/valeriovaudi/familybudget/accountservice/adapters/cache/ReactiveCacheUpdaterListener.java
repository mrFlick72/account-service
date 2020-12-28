package it.valeriovaudi.familybudget.accountservice.adapters.cache;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import reactor.core.publisher.Flux;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

import java.time.Duration;

import static reactor.core.publisher.Mono.fromCompletionStage;

public class ReactiveCacheUpdaterListener implements ApplicationRunner {

    private final Duration sleepingTime;
    private final Flux whileLoopFluxProvider;
    private final String queueUrl;
    private final ReactiveCacheManager reactiveCacheManager;
    private final SqsAsyncClient sqsAsyncClient;

    public ReactiveCacheUpdaterListener(
            Duration sleepingTime,
            Flux whileLoopFluxProvider, String queueUrl,
            ReactiveCacheManager reactiveCacheManager,
            SqsAsyncClient sqsAsyncClient) {
        this.sleepingTime = sleepingTime;
        this.whileLoopFluxProvider = whileLoopFluxProvider;
        this.queueUrl = queueUrl;
        this.reactiveCacheManager = reactiveCacheManager;
        this.sqsAsyncClient = sqsAsyncClient;
    }

    public Flux listen() {
        return whileLoopFluxProvider
                .delayElements(sleepingTime)
                .log()
                .flatMap(tick -> fromCompletionStage(sqsAsyncClient.receiveMessage(makeARequest())))
                .flatMap(response -> reactiveCacheManager.evictCache());
    }

    private ReceiveMessageRequest makeARequest() {
        return ReceiveMessageRequest.builder().queueUrl(queueUrl).build();
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        listen().subscribe();
    }
}
