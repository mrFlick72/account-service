package it.valeriovaudi.familybudget.accountservice.adapters.cache;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class ReactiveCacheUpdaterListenerTest {

    @Mock(lenient = true)
    private ReactiveCacheManager reactiveCacheManager;

    @Mock(lenient = true)
    private SqsAsyncClient sqsAsyncClient;

    @Test
    void listenTo() throws Exception {
        String queueUrl = "http://queue-url";
        ReactiveCacheUpdaterListener reactiveCacheUpdaterListener =
                new ReactiveCacheUpdaterListener(Duration.ofSeconds(2), Flux.just(1, 1), queueUrl,
                        reactiveCacheManager, sqsAsyncClient);

        ReceiveMessageRequest request = ReceiveMessageRequest.builder().queueUrl(queueUrl).build();
        ReceiveMessageResponse sqsResponse = ReceiveMessageResponse.builder().build();

        given(sqsAsyncClient.receiveMessage(request))
                .willReturn(CompletableFuture.supplyAsync(() -> sqsResponse));

        given(reactiveCacheManager.evictCache())
                .willReturn(Mono.empty());

        StepVerifier.create(reactiveCacheUpdaterListener.listen())
//                .expectNoEvent(Duration.ofSeconds(2))
                .expectComplete()
                .verify();

//        verify(reactiveCacheManager).evictCache();
    }
}