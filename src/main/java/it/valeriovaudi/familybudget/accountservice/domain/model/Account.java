package it.valeriovaudi.familybudget.accountservice.domain.model;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Locale;

@Getter
@ToString
@EqualsAndHashCode
public class Account {

    private final String firstName;
    private final String lastName;

    private final Date birthDate;

    private final String mail;
    private final String password;

    private final List<String>  userRoles;

    private Phone phone;
    private Boolean enable;
    private Locale locale;

    public Account(String firstName, String lastName,
                   Date birthDate, String mail, String password,
                   List<String> userRoles, Phone phone,
                   Boolean enable, Locale locale) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.mail = mail;
        this.password = password;
        this.userRoles = userRoles;
        this.phone = phone;
        this.enable = enable;
        this.locale = locale;
    }

}