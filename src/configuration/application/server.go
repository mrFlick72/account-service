package application

import (
	"fmt"
	"github.com/aws/aws-sdk-go/aws/session"
	"github.com/aws/aws-sdk-go/service/dynamodb"
	"github.com/kataras/iris/v12"
	"github.com/kataras/iris/v12/middleware/logger"
	"github.com/kataras/iris/v12/middleware/recover"
	"github.com/mrflick72/account-service/src/api"
	"github.com/mrflick72/account-service/src/configuration"
	"github.com/mrflick72/account-service/src/internal/web"
	"github.com/mrflick72/account-service/src/middleware/security"
	"github.com/mrflick72/account-service/src/model/repository"
	"sync"
)

var manager = configuration.GetConfigurationManagerInstance()

func newWebServer() *iris.Application {
	app := iris.New()
	app.Use(recover.New())
	app.Use(logger.New())

	security.SetUpOAuth2(app, security.Jwk{
		Url:    manager.GetConfigFor("security.jwk-uri"),
		Client: web.New(),
	}, manager.GetConfigFor("security.allowed-authority"))

	return app
}

func NewApplicationServer(wg *sync.WaitGroup) {
	app := newWebServer()

	ConfigureAccountEndpoints(ConfigureAccountRepository(), app)
	configFor := manager.GetConfigFor("server.port")
	fmt.Printf("port %v", configFor)
	app.Listen(fmt.Sprintf("0.0.0.0:%v", configFor))
	wg.Done()
}

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
