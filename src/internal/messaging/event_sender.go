package messaging

import (
	"encoding/json"
	"github.com/aws/aws-sdk-go/aws"
	"github.com/aws/aws-sdk-go/service/sqs"
)

type EventSender interface {
	SendEvent(event interface{}) error
}

type SqsEventSender struct {
	client   *sqs.SQS
	queueURL string
}

func (receiver *SqsEventSender) SendEvent(event interface{}) error {
	body, err := json.Marshal(event)
	if err == nil {
		input := sqs.SendMessageInput{
			MessageBody: aws.String(string(body)),
			QueueUrl:    &receiver.queueURL,
		}
		_, err = receiver.client.SendMessage(&input)
	}
	return err
}
