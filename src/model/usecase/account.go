package usecase

import (
	"github.com/mrflick72/account-service/src/model"
	"github.com/mrflick72/account-service/src/model/repository"
	"github.com/mrflick72/cloud-native-golang-framework/messaging"
)

type UpdateAccount struct {
	Repository  repository.AccountRepository
	EventSender messaging.EventSender
}

func (receiver *UpdateAccount) Save(account *model.Account) error {
	err := receiver.Repository.Save(account)
	if err == nil {
		receiver.EventSender.SendEvent(account)
	}
	return err
}
