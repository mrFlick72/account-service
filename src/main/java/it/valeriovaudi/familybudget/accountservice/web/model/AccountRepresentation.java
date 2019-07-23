package it.valeriovaudi.familybudget.accountservice.web.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@EqualsAndHashCode
public class AccountRepresentation {

    private String firstName;
    private String lastName;

    private String birthDate;

    private String mail;
    private String password;
    private List<String> userRoles;

    private String phone;

    public AccountRepresentation() { }

    public AccountRepresentation(String firstName, String lastName, String birthDate, String mail, String password, List<String> userRoles, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.mail = mail;
        this.password = password;
        this.userRoles = userRoles;
        this.phone = phone;
    }
}
