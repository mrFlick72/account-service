package main

import (
	"github.com/mrflick72/account-service/src/configuration/application"
	"sync"
)

func main() {
	wg := &sync.WaitGroup{}
	wg.Add(1)
	go application.NewApplicationServer(wg)
	wg.Wait()
}
