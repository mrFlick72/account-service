package security

import (
	"context"
	"github.com/kataras/iris/v12"
	"github.com/lestrrat-go/jwx/jwk"
	"github.com/lestrrat-go/jwx/jwt"
	"time"
)

func SetUpOAuth2(app *iris.Application, jwk Jwk, role string) {
	sets, _ := jwk.JwkSets()
	var middleware = NewOAuth2Middleware(sets, role)
	app.Use(middleware)
}

func NewOAuth2Middleware(keySet jwk.Set, allowedAuthority string) func(ctx iris.Context) {
	return func(ctx iris.Context) {
		authorization := authorizationHeaderFor(ctx)

		jwt, _ := jwt.ParseString(authorization, jwt.WithKeySet(keySet))
		if time.Now().After(jwt.Expiration()) {
			ctx.StatusCode(401)
			return
		}
		userName, _ := jwt.PrivateClaims()["email"].(string)
		authorities, _ := jwt.PrivateClaims()["authorities"].([]string)

		if ok := contains(authorities, allowedAuthority); !ok {
			ctx.StatusCode(403)
			return
		}

		newContext := context.WithValue(ctx.Request().Context(), "user", OAuth2User{
			UserName:    userName,
			Authorities: authorities,
		})
		ctx.Request().WithContext(newContext)
	}
}

func contains(slice []string, item string) bool {
	set := make(map[string]struct{}, len(slice))
	for _, s := range slice {
		set[s] = struct{}{}
	}

	_, ok := set[item]
	return ok
}

func authorizationHeaderFor(ctx iris.Context) string {
	authorization := ctx.GetHeader("Authorization")
	authorization = authorization[7 : len(authorization)-1]
	return authorization
}

type OAuth2User struct {
	UserName    string
	Authorities []string
}
