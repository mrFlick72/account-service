package security

import (
	"github.com/kataras/iris/v12"
	"github.com/lestrrat-go/jwx/jwk"
	"github.com/lestrrat-go/jwx/jwt"
	"time"
)

func SetUpOAuth2(app *iris.Application, jwk Jwk) {
	sets, _ := jwk.JwkSets()
	var middleware = NewOAuth2Middleware(sets)
	app.Use(middleware)
}

func NewOAuth2Middleware(keySet jwk.Set) func(ctx iris.Context) {
	return func(ctx iris.Context) {
		authorization := authorizationHeaderFor(ctx)

		jwt, _ := jwt.ParseString(authorization, jwt.WithKeySet(keySet))
		//ctx.Request().Context().Value("")
		if time.Now().After(jwt.Expiration()) {
			ctx.StatusCode(401)
			return
		}

	}
}

func authorizationHeaderFor(ctx iris.Context) string {
	authorization := ctx.GetHeader("Authorization")
	authorization = authorization[7 : len(authorization)-1]
	return authorization
}

type OAuth2User struct {
	UserName    string
	AccessToken string
	IdToken     string
	Expire      time.Time
	Authorities []string
}
