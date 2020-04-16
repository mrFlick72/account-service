package it.valeriovaudi.familybudget.accountservice.web.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
public class AccountRepresentation {

    private String firstName;
    private String lastName;

    private String birthDate;

    private String mail;

    private String phone;

    public AccountRepresentation() { }

    public AccountRepresentation(String firstName, String lastName, String birthDate, String mail, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.mail = mail;
        this.phone = phone;
    }
}
