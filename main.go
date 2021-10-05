package main

import (
	"github.com/mrflick72/account-service/src/config"
	"github.com/mrflick72/cloud-native-golang-framework"
	"github.com/mrflick72/cloud-native-golang-framework/configuration"
	"sync"
)

func main() {
	initConfigurationManager()
	initApplicationServer()
}

func initApplicationServer() {
	wg := &sync.WaitGroup{}
	wg.Add(2)
	go config.NewApplicationServer(wg)
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
