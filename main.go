package main

import (
	"github.com/mrflick72/account-service/src/configuration"
	"github.com/mrflick72/account-service/src/configuration/application"
	"sync"
)

func main() {
	initConfigurationManager()
	wg := &sync.WaitGroup{}
	wg.Add(1)
	go application.NewApplicationServer(wg)
	wg.Wait()
}

func initConfigurationManager() {
	configurationWg := &sync.WaitGroup{}
	configurationWg.Add(1)
	manager := configuration.GetConfigurationManagerInstance()

	go manager.Init(configurationWg)
	configurationWg.Wait()
}
