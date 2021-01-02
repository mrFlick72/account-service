package it.valeriovaudi.familybudget.accountservice.adapters.cache;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.util.Arrays.asList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
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
        ReceiveMessageRequestFactory factory = new ReceiveMessageRequestFactory(queueUrl, 1, 1, 1);
        ReactiveCacheUpdaterListener reactiveCacheUpdaterListener =
                new ReactiveCacheUpdaterListener(Duration.ofSeconds(2), Flux.just(1,1),
                        factory, reactiveCacheManager, sqsAsyncClient);

        Message message = Message.builder().receiptHandle("receiptHandle").build();
        ReceiveMessageRequest request = factory.makeAReceiveMessageRequest();
        ReceiveMessageResponse sqsResponse = ReceiveMessageResponse.builder()
                        .messages(message).build();

        DeleteMessageRequest deleteMessageRequest = factory.makeADeleteMessageRequest("receiptHandle");
        DeleteMessageResponse sqsDeleteMessageResponse = DeleteMessageResponse.builder().build();

        given(sqsAsyncClient.receiveMessage(request))
                .willReturn(CompletableFuture.supplyAsync(() -> sqsResponse));

        given(sqsAsyncClient.deleteMessage(deleteMessageRequest))
                .willReturn(CompletableFuture.supplyAsync(() -> sqsDeleteMessageResponse));

        given(reactiveCacheManager.evictCache())
                .willReturn(Mono.empty());

        StepVerifier.create(reactiveCacheUpdaterListener.listen())
                .expectComplete()
                .verify();

        verify(reactiveCacheManager, times(2)).evictCache();
    }
}