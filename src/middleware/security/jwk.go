package security

import (
	"context"
	"crypto/rsa"
	"errors"
	"github.com/mrflick72/account-service/src/internal/web"
	"log"

	"github.com/lestrrat-go/jwx/jwk"
)

/*
type JwkDelegate interface {
	RsaPublicKey() (*rsa.PublicKey, error)
}

type JwkWebDelegate struct {
	client *web.RestWebClient
}
func (receiver *Jwk) RsaPublicKey() (*rsa.PublicKey, error) {
	return receiver.delegate.RsaPublicKey()
}
*/

type Jwk struct {
	url    string
	client web.Client
}

func (receiver *Jwk) RsaPublicKey() ([]*rsa.PublicKey, error) {
	get, err := receiver.client.Get(&web.Request{
		Url:    receiver.url,
		Header: nil,
	})
	if err != nil {
		return nil, err
	}
	return extractKeys(get.Body)
}

func extractKeys(jwkPayload string) ([]*rsa.PublicKey, error) {
	set, err := jwk.Parse([]byte(jwkPayload))

	if err != nil {
		return nil, err
	}
	keys := make([]*rsa.PublicKey, 0)
	for it := set.Iterate(context.Background()); it.Next(context.Background()); {
		pair := it.Pair()
		key := pair.Value.(jwk.Key)

		var rawkey interface{} // This is the raw key, like *rsa.PrivateKey or *ecdsa.PrivateKey
		if err := key.Raw(&rawkey); err != nil {
			log.Printf("failed to create public key: %s", err)
			return nil, err
		}

		// We know this is an RSA Key so...
		publicKey, ok := rawkey.(*rsa.PublicKey)
		if !ok {
			return nil, errors.New("public key cast error")
		}
		// As this is a demo just dump the key to the console
		keys = append(keys, publicKey)
	}
	return keys, nil
}
