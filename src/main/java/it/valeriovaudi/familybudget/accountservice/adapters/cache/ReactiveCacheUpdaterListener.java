package it.valeriovaudi.familybudget.accountservice.adapters.cache;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import reactor.core.publisher.Flux;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;

import java.time.Duration;

import static reactor.core.publisher.Mono.fromCompletionStage;

public class ReactiveCacheUpdaterListener implements ApplicationRunner {

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
                .flatMap(req -> fromCompletionStage(sqsAsyncClient.receiveMessage(factory.makeAReceiveMessageRequest())))
                .log()
                .flatMap(response -> Flux.fromIterable(((ReceiveMessageResponse) response).messages()))
                .flatMap(message -> fromCompletionStage(sqsAsyncClient.deleteMessage(factory.makeADeleteMessageRequest(((Message) message).receiptHandle()))))
                .thenMany(reactiveCacheManager.evictCache());
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        listen().subscribe();
    }
}
