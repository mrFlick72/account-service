package model

import (
	"fmt"
	"testing"
)

func Test_DateFrom_Parsing(t *testing.T) {
	date, err := DateFrom("14/12/1985")

	if err != nil {
		panic(err)
	}

	fmt.Println(date)
}

func Test_Date_Formatting(t *testing.T) {
	date, err := DateFrom("14/12/1985")

	if err != nil {
		panic(err)
	}

	fmt.Println(date)
}
