package config

import (
	"github.com/aws/aws-sdk-go/aws/session"
	"github.com/aws/aws-sdk-go/service/dynamodb"
	"github.com/aws/aws-sdk-go/service/sqs"
	"github.com/kataras/iris/v12"
	"github.com/mrflick72/account-service/src/api"
	"github.com/mrflick72/account-service/src/model/repository"
	"github.com/mrflick72/account-service/src/model/usecase"
	"github.com/mrflick72/cloud-native-golang-framework"
	"github.com/mrflick72/cloud-native-golang-framework/configuration"
	awssqs "github.com/mrflick72/cloud-native-golang-framework/messaging/aws/sqs"
	"sync"
)

var manager = configuration.GetConfigurationManagerInstance()

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

func ConfigureAccountUpdater(repository repository.AccountRepository) *usecase.UpdateAccount {
	sess := session.Must(session.NewSessionWithOptions(session.Options{
		SharedConfigState: session.SharedConfigEnable,
	}))
	svc := sqs.New(sess)

	sender := awssqs.SqsEventSender{
		Client:   svc,
		QueueURL: manager.GetConfigFor("account.event.updates.sqs-queue-url"),
	}

	return &usecase.UpdateAccount{
		Repository:  repository,
		EventSender: &sender,
	}
}

func ConfigureAccountEndpoints(repository repository.AccountRepository,
	accountUpdate *usecase.UpdateAccount,
	app *iris.Application) {
	endpoints := api.AccountEndpoints{
		AccountRepository: repository,
		AccountUpdate:     accountUpdate,
	}
	endpoints.RegisterEndpoint(app)
}

func NewApplicationServer(wg *sync.WaitGroup) {
	app := application.NewApplicationServer()
	repository := ConfigureAccountRepository()
	updater := ConfigureAccountUpdater(repository)
	ConfigureAccountEndpoints(repository, updater, app)
	application.StartApplicationServer(wg, app)
}
