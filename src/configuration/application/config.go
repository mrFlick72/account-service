package application

import (
	"github.com/aws/aws-sdk-go/aws/session"
	"github.com/aws/aws-sdk-go/service/dynamodb"
	"github.com/kataras/iris/v12"
	"github.com/mrflick72/account-service/src/api"
	"github.com/mrflick72/account-service/src/model/repository"
)

func ConfigureAccountRepository() repository.AccountRepository {
	sess := session.Must(session.NewSessionWithOptions(session.Options{
		SharedConfigState: session.SharedConfigEnable,
	}))
	svc := dynamodb.New(sess)

	return &repository.DynamoAccountRepository{
		Client:    svc,
		TableName: manager.GetConfigFor("datasource.table-name"),
	}
}

func ConfigureAccountEndpoints(repository repository.AccountRepository, app *iris.Application) {
	endpoints := api.AccountEndpoints{
		AccountRepository: repository,
	}
	endpoints.RegisterEndpoint(app)
}
