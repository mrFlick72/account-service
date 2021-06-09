package application

import (
	"github.com/aws/aws-sdk-go/aws"
	"github.com/aws/aws-sdk-go/aws/session"
	"github.com/aws/aws-sdk-go/service/dynamodb"
	"github.com/kataras/iris/v12"
	"github.com/kataras/iris/v12/middleware/logger"
	"github.com/kataras/iris/v12/middleware/recover"
	"github.com/mrflick72/account-service/src/api"
	"github.com/mrflick72/account-service/src/model/repository"
	"sync"
)

func newWebServer() *iris.Application {
	app := iris.New()
	app.Use(recover.New())
	app.Use(logger.New())
	return app
}

func NewApplicationServer(wg *sync.WaitGroup) {
	app := newWebServer()

	ConfigureAccountEndpoints(ConfigureAccountRepository(), app)

	// Listen and serve on 0.0.0.0:8080
	app.Listen("0.0.0.0:9000")
	wg.Done()
}

func ConfigureAccountRepository() repository.AccountRepository {
	sess := session.Must(session.NewSessionWithOptions(session.Options{
		SharedConfigState: session.SharedConfigEnable,
	}))
	svc := dynamodb.New(sess, &aws.Config{Region: aws.String("us-east-1"), Endpoint: aws.String("http://localhost:8000")})

	return &repository.DynamoAccountRepository{
		Client:    svc,
		TableName: "TESTING_Account_Service",
	}
}

func ConfigureAccountEndpoints(repository repository.AccountRepository, app *iris.Application) {
	endpoints := api.AccountEndpoints{
		AccountRepository: repository,
	}
	endpoints.RegisterEndpoint(app)
}
