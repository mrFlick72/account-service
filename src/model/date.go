package model

import "time"

const DEFAULT_DATE_TIME_FORMATTER = "2006-01-02"
const REPRESENTATION_DATE_TIME_FORMATTER = "02/01/2006"

type Date struct {
	Day time.Time
}

func (d *Date) FormattedDate(pattern *string) string {
	actualPattern := DEFAULT_DATE_TIME_FORMATTER
	if pattern != nil {
		actualPattern = *pattern
	}
	return d.Day.Format(actualPattern)
}

func DateFrom(date string) (*Date, error) {
	parse, err := time.Parse(DEFAULT_DATE_TIME_FORMATTER, date)
	return &Date{Day: parse}, err
}
