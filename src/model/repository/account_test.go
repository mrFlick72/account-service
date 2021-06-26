package repository

import (
	"github.com/aws/aws-sdk-go/aws"
	"github.com/aws/aws-sdk-go/aws/session"
	"github.com/aws/aws-sdk-go/service/dynamodb"
	"github.com/mrflick72/account-service/src/model"
	"github.com/stretchr/testify/assert"
	"testing"
)

func TestDynamoAccountRepository_Save(t *testing.T) {
	sess := session.Must(session.NewSessionWithOptions(session.Options{
		SharedConfigState: session.SharedConfigEnable,
	}))
	svc := dynamodb.New(sess, &aws.Config{Region: aws.String("us-east-1"), Endpoint: aws.String("http://localhost:8000")})

	repository := DynamoAccountRepository{
		Client:    svc,
		TableName: "TESTING_Account_Service",
	}
	date, _ := model.DateFrom("05/05/1985")
	expected := &model.Account{
		FirstName: "A_NAME",
		LastName:  "A_LAST_NAME",
		BirthDate: date,
		Mail:      "mail@mail.com",
		Phone:     model.PhoneFor("+1 333 1122333"),
		Locale:    "it",
	}
	err := repository.Save(expected)

	if err != nil {
		t.Error(err)
	}

	actual, err := repository.Find("mail@mail.com")
	if err != nil {
		t.Error(err)
	}

	assert.EqualValues(t, actual, expected)
}
