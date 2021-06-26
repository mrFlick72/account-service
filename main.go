package main

import (
	"github.com/mrflick72/account-service/src/configuration"
	"github.com/mrflick72/account-service/src/configuration/application"
	"sync"
)

func main() {
	initConfigurationManager()
	initApplicationServer()
}

func initApplicationServer() {
	wg := &sync.WaitGroup{}
	wg.Add(2)
	go application.NewApplicationServer(wg)
	go application.NewActuatorServer(wg)
	wg.Wait()
}

func initConfigurationManager() {
	configurationWg := &sync.WaitGroup{}
	configurationWg.Add(1)
	manager := configuration.GetConfigurationManagerInstance()

	go manager.Init(configurationWg)
	configurationWg.Wait()
}
