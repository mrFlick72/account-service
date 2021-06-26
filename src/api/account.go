package api

import (
	"fmt"
	"github.com/kataras/iris/v12"
	"github.com/mrflick72/account-service/src/configuration"
	"github.com/mrflick72/account-service/src/middleware/security"
	"github.com/mrflick72/account-service/src/model/repository"
)

var manager = configuration.GetConfigurationManagerInstance()

type AccountEndpoints struct {
	AccountRepository repository.AccountRepository
}

func (endpoint *AccountEndpoints) RegisterEndpoint(application *iris.Application) {
	contextPath := manager.GetConfigFor("server.context-path")
	endpointPath := fmt.Sprintf("%v%v", contextPath, "/user-account")
	application.Get(endpointPath, endpoint.getAccountEndpoint)
	application.Put(endpointPath, endpoint.updateAccountsEndpoint)
}

func (endpoint *AccountEndpoints) getAccountEndpoint(ctx iris.Context) {
	ctx.Application().Logger().Info(ctx.Request().Context())
	user := ctx.Request().Context().Value("user").(security.OAuth2User)
	ctx.Application().Logger().Info(user)
	account, _ := endpoint.AccountRepository.Find(user.UserName)
	ctx.JSON(account)
	ctx.StatusCode(iris.StatusOK)
	return
}

func (endpoint *AccountEndpoints) updateAccountsEndpoint(ctx iris.Context) {
	//context := tracingContextFrom(ctx)

	ctx.StatusCode(iris.StatusOK)
	return
}
