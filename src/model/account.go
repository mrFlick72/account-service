package model

import (
	"github.com/mrflick72/cloud-native-golang-framework/date"
)

type Account struct {
	FirstName FirstName `json:"firstName"`
	LastName  LastName  `json:"lastName"`

	BirthDate *date.Date

	Mail   Mail `json:"mail"`
	Phone  *Phone
	Locale Locale
}

type FirstName = string
type LastName = string
type Mail = string
type Locale = string
