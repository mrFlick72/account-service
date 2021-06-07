package repository

import (
	"github.com/aws/aws-sdk-go/aws"
	"github.com/aws/aws-sdk-go/aws/session"
	"github.com/aws/aws-sdk-go/service/dynamodb"
	"github.com/mrflick72/account-service/src/model"
	"testing"
)

func TestDynamoAccountRepository_Save(t *testing.T) {
	sess := session.Must(session.NewSessionWithOptions(session.Options{
		SharedConfigState: session.SharedConfigEnable,
	}))
	svc := dynamodb.New(sess, &aws.Config{Region: aws.String("us-east-1"), Endpoint: aws.String("http://localhost:8000")})

	repository := DynamoAccountRepository{
		Client:    svc,
		TableName: "",
	}
	date, _ := model.DateFrom("01/01/1970")
	err := repository.Save(&model.Account{
		FirstName: "A_NAME",
		LastName:  "A_LAST_NAME",
		BirthDate: date,
		Mail:      "mail@mail.com",
		Phone:     model.PhoneFor("3331122333"),
		Locale:    "it",
	})

	if err != nil {
		t.Error(err)
	}

	account, err := repository.Find("mail@mail.com")
	if err != nil {
		t.Error(err)
	}

	print(account)
}
