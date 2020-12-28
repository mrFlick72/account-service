package it.valeriovaudi.familybudget.accountservice.adapters.cache;

import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

public class ReceiveMessageRequestFactory {

    private final String queueUrl;
    private final Integer maxNumberOfMessages;
    private final Integer visibilityTimeout;
    private final Integer waitTimeSeconds;

    public ReceiveMessageRequestFactory(String queueUrl,
                                        Integer maxNumberOfMessages,
                                        Integer visibilityTimeout,
                                        Integer waitTimeSeconds) {
        this.queueUrl = queueUrl;
        this.maxNumberOfMessages = maxNumberOfMessages;
        this.visibilityTimeout = visibilityTimeout;
        this.waitTimeSeconds = waitTimeSeconds;
    }

    public ReceiveMessageRequest makeARequest() {
        return ReceiveMessageRequest.builder()
                .maxNumberOfMessages(maxNumberOfMessages)
                .visibilityTimeout(visibilityTimeout)
                .waitTimeSeconds(waitTimeSeconds)
                .queueUrl(queueUrl)
                .build();
    }
}