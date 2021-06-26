package messaging

import (
	"encoding/json"
	"github.com/aws/aws-sdk-go/aws"
	"github.com/aws/aws-sdk-go/aws/session"
	"github.com/aws/aws-sdk-go/service/sqs"
	"github.com/mrflick72/account-service/src/model"
	"github.com/stretchr/testify/assert"
	"testing"
	"time"
)

func TestSqsEventSender_SendEvent(t *testing.T) {

	sess := session.Must(session.NewSessionWithOptions(session.Options{
		SharedConfigState: session.SharedConfigEnable,
	}))

	client := sqs.New(sess, &aws.Config{Region: aws.String("us-east-1"), Endpoint: aws.String("http://localhost:4566")})
	queueURL := "http://localhost:4566/000000000000/TESTING_Account_Service_Sync_queue"
	sender := SqsEventSender{
		Client:   client,
		QueueURL: queueURL,
	}
	date, _ := model.DateFrom("05/05/1985")
	account := &model.Account{
		FirstName: "A_NAME",
		LastName:  "A_LAST_NAME",
		BirthDate: date,
		Mail:      "mail@mail.com",
		Phone:     model.PhoneFor("+1 333 1122333"),
		Locale:    "it",
	}
	err := sender.SendEvent(account)
	if err != nil {
		t.Error(err)
	}

	time.Sleep(time.Second * 2)
	output, receivedError := client.ReceiveMessage(&sqs.ReceiveMessageInput{
		MessageAttributeNames: []*string{
			aws.String(sqs.QueueAttributeNameAll),
		},
		QueueUrl:            aws.String(queueURL),
		MaxNumberOfMessages: aws.Int64(10),
		VisibilityTimeout:   aws.Int64(10),
		WaitTimeSeconds:     aws.Int64(10),
	})
	if receivedError != nil {
		t.Error(receivedError)
	}

	var receivedAccount = new(model.Account)
	json.Unmarshal([]byte(*output.Messages[0].Body), receivedAccount)
	assert.EqualValues(t, account, receivedAccount)
}
