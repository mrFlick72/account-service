package repository

import (
	"github.com/mrflick72/account-service/src/internal/messaging"
	"github.com/mrflick72/account-service/src/model"
	"github.com/mrflick72/account-service/src/model/repository"
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
