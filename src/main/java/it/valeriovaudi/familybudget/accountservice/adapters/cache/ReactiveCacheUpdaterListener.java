package it.valeriovaudi.familybudget.accountservice.adapters.cache;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationListener;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

import java.time.Duration;

import static reactor.core.publisher.Mono.fromCompletionStage;

public class ReactiveCacheUpdaterListener implements ApplicationRunner {


    private final String queueUrl;
    private final ReactiveCacheManager reactiveCacheManager;
    private final SqsAsyncClient sqsAsyncClient;

    public ReactiveCacheUpdaterListener(String queueUrl,
                                        ReactiveCacheManager reactiveCacheManager,
                                        SqsAsyncClient sqsAsyncClient) {
        this.queueUrl = queueUrl;
        this.reactiveCacheManager = reactiveCacheManager;
        this.sqsAsyncClient = sqsAsyncClient;
    }

    public Mono listen() {
        return fromCompletionStage(sqsAsyncClient.receiveMessage(ReceiveMessageRequest.builder().queueUrl(queueUrl).build()))
                .then(reactiveCacheManager.evictCache());
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

    }
}
