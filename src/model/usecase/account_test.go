package usecase

import (
	"github.com/mrflick72/account-service/src/model"
	"github.com/stretchr/testify/mock"
	"testing"
)

func TestUpdateAccount_Save(t *testing.T) {
	repository := new(MockedAccountRepositoryObject)
	sender := new(MockedEventSenderObject)
	account := &model.Account{
		FirstName: "A_NAME",
		LastName:  "A_LAST_NAME",
		BirthDate: nil,
		Mail:      "mail@mail.com",
		Phone:     model.PhoneFor("+1 333 1122333"),
		Locale:    "it",
	}

	accountUpdate := UpdateAccount{
		Repository:  repository,
		EventSender: sender,
	}

	repository.On("Save", account)
	sender.On("SendEvent", account)

	accountUpdate.Save(account)

	sender.AssertCalled(t, "SendEvent", account)
}

type MockedAccountRepositoryObject struct {
	mock.Mock
}
type MockedEventSenderObject struct {
	mock.Mock
}

func (mock *MockedAccountRepositoryObject) Find(mail model.Mail) (*model.Account, error) {
	return nil, nil
}

func (mock *MockedAccountRepositoryObject) Save(account *model.Account) error {
	mock.Called(account)
	return nil
}

func (mock *MockedEventSenderObject) SendEvent(event interface{}) error {
	mock.Called(event)
	return nil
}
