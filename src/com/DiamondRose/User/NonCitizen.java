package com.DiamondRose.User;

import com.DiamondRose.Util.Address;
import com.DiamondRose.Util.Gender;

import java.time.LocalDate;
import java.util.UUID;

final public class NonCitizen extends People{

    public static NonCitizen fromString(String string) {
        String[] data = string.split("\t");
        return NonCitizen.fromPerson(People.fromString(string), data[9]);
    }

    private static NonCitizen fromPerson(People person, String passport){
        return new NonCitizen(person.id, person.username, person.email, person.firstName, person.lastName, person.dob, person.gender, person.password, person.address, passport);
    }

    private final String passport;

    public NonCitizen(UUID id, String username, String email, String firstName, String lastName, LocalDate dob, Gender gender, String password, Address address, String passport) {
        super(id, username, email, firstName, lastName, dob, gender, password, address);
        this.passport = passport;
    }

    @Override
    public String getType() {
        return "Non-Citizen";
    }

    public String getPassport(){
        return this.passport;
    }

    public String toString() {
        return String.join("\t", super.toString(), this.passport);
    }
}

