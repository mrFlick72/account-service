package api

import (
	"github.com/kataras/iris/v12"
	"github.com/mrflick72/account-service/src/model/repository"
)

type AccountEndpoints struct {
	AccountRepository repository.AccountRepository
}

func (endpoint *AccountEndpoints) RegisterEndpoint(application *iris.Application) {
	application.Get("/user-account", endpoint.getAccountEndpoint)
	application.Put("/user-account", endpoint.updateAccountsEndpoint)
}

func (endpoint *AccountEndpoints) getAccountEndpoint(ctx iris.Context) {
	//context := tracingContextFrom(ctx)
	mail := ctx.Params().Get("mail")
	account, _ := endpoint.AccountRepository.Find(mail)
	ctx.JSON(account)
	ctx.StatusCode(iris.StatusOK)
	return
}

func (endpoint *AccountEndpoints) updateAccountsEndpoint(ctx iris.Context) {
	//context := tracingContextFrom(ctx)

	ctx.StatusCode(iris.StatusOK)
	return
}
