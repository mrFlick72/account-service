package it.valeriovaudi.familybudget.accountservice.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;


@Getter
@ToString
@EqualsAndHashCode
public class Phone {

    private final String countryPrefix;
    private final String prefix;
    private final String phoneNumber;

    public Phone(String countryPrefix, String prefix, String phoneNumber) {
        this.countryPrefix = countryPrefix;
        this.prefix = prefix;
        this.phoneNumber = phoneNumber;
    }

    public static Phone nullValue() {
        return new Phone("","","");
    }

    public static Phone phoneFor(String phoneNumber) {
        Phone phone = nullValue();
        String[] split = phoneNumber.split(" ");
        if(split.length == 3){
            phone = new Phone(split[0], split[1], split[2]);
        } else if(split.length == 2){
            phone = new Phone("", split[0], split[1]);
        }
        return phone;
    }

    public String formattedPhone(){
        return String.format("%s %s %s", this.countryPrefix, this.prefix, this.phoneNumber).trim();
    }
}
