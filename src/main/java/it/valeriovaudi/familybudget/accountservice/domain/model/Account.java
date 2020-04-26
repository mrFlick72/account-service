package it.valeriovaudi.familybudget.accountservice.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Locale;

@Getter
@ToString
@EqualsAndHashCode
public class Account {

    private final String firstName;
    private final String lastName;

    private final Date birthDate;

    private final String mail;

    private Phone phone;
    private Locale locale;


    public Account(String firstName,
                   String lastName,
                   Date birthDate,
                   String mail,
                   Phone phone,
                   Locale locale) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.mail = mail;
        this.phone = phone;
        this.locale = locale;
    }
}