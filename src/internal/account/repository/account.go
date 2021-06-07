package repository

import (
	"github.com/aws/aws-sdk-go/aws"
	"github.com/aws/aws-sdk-go/service/dynamodb"
	"github.com/aws/aws-sdk-go/service/dynamodb/dynamodbattribute"
	"github.com/mrflick72/account-service/src/model"
	"log"
)

type AccountRepository interface {
	Find(mail model.Mail) (*model.Account, error)
	Save(account *model.Account) error
}

type DynamoAccountRepository struct {
	Client    *dynamodb.DynamoDB
	TableName string
}

func (receiver *DynamoAccountRepository) Find(mail model.Mail) (*model.Account, error) {
	getItemRequest := &dynamodb.GetItemInput{
		TableName: aws.String(receiver.TableName),
		Key: map[string]*dynamodb.AttributeValue{
			"mail": {
				S: aws.String(mail),
			},
		},
	}
	resposne, err := receiver.Client.GetItem(getItemRequest)
	item := resposne.Item
	date, _ := model.DateFrom(*item["BirthDate"].S)
	return &model.Account{
		FirstName: *item["FirstName"].S,
		LastName:  *item["LastName"].S,
		BirthDate: date,
		Mail:      *item["Mail"].S,
		Phone:     model.PhoneFor(*item["Phone"].S),
		Locale:    *item["Locale"].S,
	}, err
}

func (receiver *DynamoAccountRepository) Save(account *model.Account) error {
	accountAttributes, err := dynamodbattribute.MarshalMap(account)
	if err != nil {
		log.Fatalf("Got error marshalling new movie item: %s", err)
	}
	input := &dynamodb.PutItemInput{
		Item:      accountAttributes,
		TableName: aws.String(receiver.TableName),
	}
	_, err = receiver.Client.PutItem(input)
	if err != nil {
		log.Fatalf("Got error calling PutItem: %s", err)
	}
	return err
}
