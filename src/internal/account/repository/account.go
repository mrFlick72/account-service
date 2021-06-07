package repository

import "github.com/mrflick72/account-service/src/model"

type Mail = string

type AccountRepository interface {
	Find(mail Mail) (*model.Account, error)
	Save(account *model.Account) error
}

type DynamoAccountRepository struct {
}

func (receiver *DynamoAccountRepository) Find(mail Mail) (*model.Account, error) {
	panic("TODO")

}
func (receiver *DynamoAccountRepository) Save(account *model.Account) error {
	panic("TODO")
}
