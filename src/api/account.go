package api

import (
	"github.com/kataras/iris/v12"
	"github.com/mrflick72/account-service/src/internal/tracing"
)

type AccountEndpoints struct {
}

func (endpoint *AccountEndpoints) RegisterEndpoint(application *iris.Application) {
	application.Get("/user-account", endpoint.getAccountEndpoint)
	application.Put("/user-account", endpoint.updateAccountsEndpoint)
}

func (endpoint *AccountEndpoints) getAccountEndpoint(ctx iris.Context) {
	//context := tracingContextFrom(ctx)

	ctx.StatusCode(iris.StatusOK)
	return
}

func (endpoint *AccountEndpoints) updateAccountsEndpoint(ctx iris.Context) {
	//context := tracingContextFrom(ctx)

	ctx.StatusCode(iris.StatusOK)
	return
}

func tracingContextFrom(ctx iris.Context) map[string]string {
	return tracing.GetTracingHeadersFrom(ctx)
}
