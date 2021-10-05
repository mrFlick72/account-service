package config

import (
	application "github.com/mrflick72/cloud-native-golang-framework"
	"sync"
)

func NewApplicationServer(wg *sync.WaitGroup) {
	app := application.NewApplicationServer()
	repository := ConfigureAccountRepository()
	updater := ConfigureAccountUpdater(repository)
	ConfigureAccountEndpoints(repository, updater, app)
	application.StartApplicationServer(wg, app)
}