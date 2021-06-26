package api

import (
	"encoding/json"
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
	userName := userNameFrom(ctx)
	account, _ := endpoint.AccountRepository.Find(userName)
	ctx.JSON(fromDomainToRepresentationFor(account))
	ctx.StatusCode(iris.StatusOK)
}

func (endpoint *AccountEndpoints) updateAccountsEndpoint(ctx iris.Context) {
	representation := AccountRepresentation{}
	body, _ := ctx.GetBody()
	json.Unmarshal(body, &representation)

	account := fromRepresentationToDomainFor(representation)
	account.Mail = userNameFrom(ctx)
	endpoint.AccountRepository.Save(account)

	ctx.StatusCode(iris.StatusNoContent)
}

func userNameFrom(ctx iris.Context) string {
	return ctx.Request().Context().Value("user").(security.OAuth2User).UserName
}

type AccountRepresentation struct {
	FirstName string `json:"firstName"`
	LastName  string `json:"lastName"`

	BirthDate string `json:"birthDate"`

	Mail  string `json:"mail"`
	Phone string `json:"phone"`
}

func fromDomainToRepresentationFor(account *model.Account) AccountRepresentation {
	pattern := stringsUtils.AsPointer(model.REPRESENTATION_DATE_TIME_FORMATTER)
	return AccountRepresentation{
		FirstName: account.FirstName,
		LastName:  account.LastName,
		BirthDate: account.BirthDate.FormattedDate(pattern),
		Mail:      account.Mail,
		Phone:     account.Phone.FormattedPhone(),
	}
}

func fromRepresentationToDomainFor(account AccountRepresentation) *model.Account {
	date, _ := model.DateFrom(account.BirthDate, stringsUtils.AsPointer(model.REPRESENTATION_DATE_TIME_FORMATTER))
	return &model.Account{
		FirstName: account.FirstName,
		LastName:  account.LastName,
		BirthDate: date,
		Mail:      account.Mail,
		Phone:     model.PhoneFor(account.Phone),
		Locale:    "en",
	}
}
