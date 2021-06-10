package security

import (
	"github.com/kataras/iris/v12"
	"github.com/kataras/iris/v12/sessions"
	"time"
)

func SetUpOAuth2(app *iris.Application) {
	//oauth2Config, verifier := oauth2Configurer()
	//var middleware = NewOAuth2Middleware(oauth2Config)
	//app.Use(middleware)
}

/*
func oauth2Configurer() (*oauth2.Config, *oidc.IDTokenVerifier) {
	configURL := os.Getenv("OIDC_IDP_URL")
	ctx := context.Background()
	provider, err := oidc.NewProvider(ctx, configURL)
	if err != nil {
		panic(err)
	}

	clientID := os.Getenv("OIDC_CLIENT_ID")
	clientSecret := os.Getenv("OIDC_CLIENT_SECRET")
	redirectURL := os.Getenv("OIDC_REDIRECT_URL")

	// Configure an OpenID Connect aware OAuth2 client.
	oauth2Config := &oauth2.Config{
		ClientID:     clientID,
		ClientSecret: clientSecret,
		RedirectURL:  redirectURL,
		// Discovery returns the OAuth2 endpoints.
		Endpoint: provider.Endpoint(),
		// "openid" is a required scope for OpenID Connect flows.
		Scopes: []string{oidc.ScopeOpenID, "profile", "email"},
	}
	oidcConfig := &oidc.Config{
		ClientID: clientID,
	}
	verifier := provider.Verifier(oidcConfig)

	return oauth2Config, verifier
}*/

func NewOAuth2Middleware() func(ctx iris.Context) {
	return func(ctx iris.Context) {
		// put here the base midelware logic
	}
}

type OAuth2User struct {
	UserName    string
	AccessToken string
	IdToken     string
	Expire      time.Time
	Authorities []string
}

func (user *OAuth2User) IsExpired() bool {
	return time.Now().After(user.Expire)
}

func GetUserFromSession(context iris.Context) OAuth2User {
	session := sessions.Get(context)
	return session.Get("oauth2User").(OAuth2User)
}
