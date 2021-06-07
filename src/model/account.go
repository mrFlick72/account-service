package model

type Account struct {
	FirstName FirstName
	LastName  LastName

	BirthDate *Date

	Mail   Mail
	Phone  *Phone
	Locale Locale
}

type FirstName = string
type LastName = string
type Mail = string
type Locale = string
