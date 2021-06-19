package main

import (
	"context"
	"crypto/rsa"
	"fmt"
	"github.com/lestrrat-go/jwx/jwk"
	"github.com/mrflick72/account-service/src/config/application"
	"log"
	"sync"
)

func main() {
	main2()
	wg := &sync.WaitGroup{}
	wg.Add(1)
	go application.NewApplicationServer(wg)
	wg.Wait()
}

func main2() {
	// Example jwk from https://www.googleapis.com/oauth2/v3/certs (but with only one cert for simplicity)
	jwkJSON := `{
  "keys": [ 
    {
      "kty": "RSA",
      "n": "o76AudS2rsCvlz_3D47sFkpuz3NJxgLbXr1cHdmbo9xOMttPMJI97f0rHiSl9stltMi87KIOEEVQWUgMLaWQNaIZThgI1seWDAGRw59AO5sctgM1wPVZYt40fj2Qw4KT7m4RLMsZV1M5NYyXSd1lAAywM4FT25N0RLhkm3u8Hehw2Szj_2lm-rmcbDXzvjeXkodOUszFiOqzqBIS0Bv3c2zj2sytnozaG7aXa14OiUMSwJb4gmBC7I0BjPv5T85CH88VOcFDV51sO9zPJaBQnNBRUWNLh1vQUbkmspIANTzj2sN62cTSoxRhSdnjZQ9E_jraKYEW5oizE9Dtow4EvQ",
      "use": "sig",
      "alg": "RS256",
      "e": "AQAB",
      "kid": "6a8ba5652a7044121d4fedac8f14d14c54e4895b"
    }
  ]
}
`

	set, err := jwk.Parse([]byte(jwkJSON))
	if err != nil {
		panic(err)
	}
	fmt.Println(set)
	for it := set.Iterate(context.Background()); it.Next(context.Background()); {
		pair := it.Pair()
		key := pair.Value.(jwk.Key)

		var rawkey interface{} // This is the raw key, like *rsa.PrivateKey or *ecdsa.PrivateKey
		if err := key.Raw(&rawkey); err != nil {
			log.Printf("failed to create public key: %s", err)
			return
		}

		// We know this is an RSA Key so...
		rsa, ok := rawkey.(*rsa.PublicKey)
		if !ok {
			panic(fmt.Sprintf("expected ras key, got %T", rawkey))
		}
		// As this is a demo just dump the key to the console
		fmt.Println(rsa)
	}
}
