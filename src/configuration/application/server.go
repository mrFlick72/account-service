package application

import (
	"fmt"
	"github.com/kataras/iris/v12"
	"github.com/kataras/iris/v12/middleware/logger"
	"github.com/kataras/iris/v12/middleware/recover"
	"github.com/mrflick72/account-service/src/configuration"
	"github.com/mrflick72/account-service/src/internal/heath"
	"github.com/mrflick72/account-service/src/internal/web"
	"github.com/mrflick72/account-service/src/middleware/security"
	"sync"
)

var manager = configuration.GetConfigurationManagerInstance()

func newWebServer() *iris.Application {
	app := iris.New()
	app.Use(recover.New())
	app.Use(logger.New())

	return app
}

func NewApplicationServer(wg *sync.WaitGroup) {
	app := newWebServer()

	security.SetUpOAuth2(app, security.Jwk{
		Url:    manager.GetConfigFor("security.jwk-uri"),
		Client: web.New(),
	}, manager.GetConfigFor("security.allowed-authority"))

	ConfigureAccountEndpoints(ConfigureAccountRepository(), app)
	app.Listen(fmt.Sprintf(":%v", manager.GetConfigFor("server.port")))
	wg.Done()
}

func NewActuatorServer(wg *sync.WaitGroup) {
	app := newWebServer()
	endpoints := heath.HealthEndpoint{}
	endpoints.ResgisterEndpoints(app)
	app.Listen(fmt.Sprintf(":%v", manager.GetConfigFor("management.port")))
	wg.Done()
}
