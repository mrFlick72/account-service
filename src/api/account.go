package api

import (
	"fmt"
	"github.com/kataras/iris/v12"
	"github.com/mrflick72/account-service/src/configuration"
	stringsUtils "github.com/mrflick72/account-service/src/internal/strings"
	"github.com/mrflick72/account-service/src/middleware/security"
	"github.com/mrflick72/account-service/src/model"
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
	user := ctx.Request().Context().Value("user").(security.OAuth2User)
	account, _ := endpoint.AccountRepository.Find(user.UserName)
	ctx.JSON(convert(account))
	ctx.StatusCode(iris.StatusOK)
}

func (endpoint *AccountEndpoints) updateAccountsEndpoint(ctx iris.Context) {
	ctx.StatusCode(iris.StatusNoContent)
	return
}

type AccountRepresentation struct {
	FirstName string `json:"firstName"`
	LastName  string `json:"lastName"`

	BirthDate string `json:"birthDate"`

	Mail  string `json:"mail"`
	Phone string `json:"phone"`
}

func convert(account *model.Account) AccountRepresentation {
	pattern := stringsUtils.AsPointer(model.REPRESENTATION_DATE_TIME_FORMATTER)
	return AccountRepresentation{
		FirstName: account.FirstName,
		LastName:  account.LastName,
		BirthDate: account.BirthDate.FormattedDate(pattern),
		Mail:      account.Mail,
		Phone:     account.Phone.FormattedPhone(),
	}
}
