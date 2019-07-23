package it.valeriovaudi.familybudget.accountservice.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static java.time.format.DateTimeFormatter.ofPattern;


@ToString
@EqualsAndHashCode
public final class Date implements Comparable<Date> {

    static final DateTimeFormatter DEFAULT_DATE_TIME_FORMATTER = ofPattern("dd/MM/yyyy");

    @Getter
    private final LocalDate localDate;
    private final DateTimeFormatter dateTimeFormatter;

    public Date(LocalDate localDate, DateTimeFormatter dateTimeFormatter) {
        this.localDate = localDate;
        this.dateTimeFormatter = dateTimeFormatter;
    }

    public Date(LocalDate localDate) {
        this.localDate = localDate;
        this.dateTimeFormatter = DEFAULT_DATE_TIME_FORMATTER;
    }

    public String formattedDate(){
        return dateTimeFormatter.format(localDate);
    }

    public static Date dateFor(String date) {
        return new Date(LocalDate.parse(date, DEFAULT_DATE_TIME_FORMATTER));
    }


    @Override
    public int compareTo(Date o) {
        return this.getLocalDate().compareTo(o.getLocalDate());
    }
}
