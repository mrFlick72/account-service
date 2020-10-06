package it.valeriovaudi.familybudget.accountservice.domain.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DateTest {
    static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Test
    public void dateIsFormattedWithCustomFormatter() {
        String expectedFormattedDate = "25-02-2018";
        String anotherExpectedFormattedDate = "25-03-2018";
        String anotherExpectedFormattedDate2 = "25-05-2018";

        Date date = new Date(LocalDate.of(2018, 02, 25), DATE_TIME_FORMATTER);
        Date anotherDate = new Date(LocalDate.of(2018, 03, 25), DATE_TIME_FORMATTER);
        Date anotherDate2 = new Date(LocalDate.of(2018, 05, 25), DATE_TIME_FORMATTER);

        assertEquals(expectedFormattedDate, date.formattedDate());
        assertEquals(anotherExpectedFormattedDate, anotherDate.formattedDate());
        assertEquals(anotherExpectedFormattedDate2, anotherDate2.formattedDate());
    }


    @Test
    public void dateIsFormattedWithDefaultFormatter() {
        String expectedFormattedDate = "25/02/2018";
        String anotherExpectedFormattedDate = "25/03/2018";
        String anotherExpectedFormattedDate2 = "25/05/2018";

        Date date = new Date(LocalDate.of(2018, 02, 25));
        Date anotherDate = new Date(LocalDate.of(2018, 03, 25));
        Date anotherDate2 = new Date(LocalDate.of(2018, 05, 25));

        assertEquals(expectedFormattedDate, date.formattedDate());
        assertEquals(anotherExpectedFormattedDate, anotherDate.formattedDate());
        assertEquals(anotherExpectedFormattedDate2, anotherDate2.formattedDate());
    }

    @Test
    public void dateFromString() {
        Date expectedDateForDateString = new Date(LocalDate.of(2018, 02, 25));
        Date actualDateForDateString = Date.dateFor("25/02/2018");
        assertEquals(expectedDateForDateString, actualDateForDateString);
    }

}
