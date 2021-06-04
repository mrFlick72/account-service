package model

import "time"

const DEFAULT_DATE_TIME_FORMATTER = "02/01/2006"

type Date struct {
	Day time.Time
}

func (d *Date) formattedDate() string {
	return d.Day.Format(DEFAULT_DATE_TIME_FORMATTER)
}

func DateFrom(date string) (*Date, error) {
	parse, err := time.Parse(DEFAULT_DATE_TIME_FORMATTER, date)
	return &Date{Day: parse}, err
}
