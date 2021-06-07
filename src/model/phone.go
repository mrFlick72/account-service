package model

import "strings"

type Phone struct {
	CountryPrefix string `json:"countryPrefix"`
	Prefix        string `json:"prefix"`
	PhoneNumber   string `json:"phoneNumber"`
}

func PhoneFor(phoneNumber string) *Phone {
	phone := nullPhoneValue()

	split := strings.Split(phoneNumber, " ")
	if len(split) == 3 {
		phone = &Phone{
			CountryPrefix: split[0],
			Prefix:        split[1],
			PhoneNumber:   split[2],
		}
	} else if len(split) == 2 {
		phone = &Phone{
			CountryPrefix: "",
			Prefix:        split[0],
			PhoneNumber:   split[1],
		}
	}
	return phone
}
func nullPhoneValue() *Phone {
	return &Phone{
		CountryPrefix: "",
		Prefix:        "",
		PhoneNumber:   "",
	}
}
