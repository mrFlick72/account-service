package it.valeriovaudi.familybudget.accountservice.adapters.cache;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.DefaultApplicationArguments;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;

import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class ReactiveCacheUpdaterListenerTest {

    @Mock
    private ReactiveCacheManager reactiveCacheManager;

    @Mock
    private SqsAsyncClient sqsAsyncClient;

    @Test
    void listenTo() throws Exception {
        String queueUrl = "http://queue-url";
        ReactiveCacheUpdaterListener reactiveCacheUpdaterListener =
                new ReactiveCacheUpdaterListener(queueUrl, reactiveCacheManager, sqsAsyncClient);

        ReceiveMessageRequest request = ReceiveMessageRequest.builder().queueUrl(queueUrl).build();
        ReceiveMessageResponse sqsResponse =  ReceiveMessageResponse.builder().build();

        given(sqsAsyncClient.receiveMessage(request))
                .willReturn(CompletableFuture.supplyAsync(() -> sqsResponse));

        given(reactiveCacheManager.evictCache())
                .willReturn(Mono.empty());

//        reactiveCacheUpdaterListener.run(new DefaultApplicationArguments());

        StepVerifier.create(reactiveCacheUpdaterListener.listen())
                .expectNext(sqsResponse)
                .expectComplete();

        verify(reactiveCacheManager).evictCache();
    }
}