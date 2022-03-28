package com.DiamondRose.User;

import com.DiamondRose.Util.Address;
import com.DiamondRose.Util.Gender;
import com.DiamondRose.Util.MyKadNumber;

import java.time.LocalDate;
import java.util.UUID;

final public class Citizen extends People{

    public static Citizen fromString(String string) {
        String[] data = string.split("\t");
        return Citizen.fromPerson(People.fromString(string), MyKadNumber.fromString(data[9]));
    }

    private static Citizen fromPerson(People person, MyKadNumber myKadNumber){
        return new Citizen(person.id, person.username, person.email, person.firstName, person.lastName, person.dob, person.gender, person.password, person.address, myKadNumber);
    }

    private final MyKadNumber myKadNumber;

    public Citizen(UUID id, String username, String email, String firstName, String lastName, LocalDate dob, Gender gender, String password, Address address, MyKadNumber myKadNumber) {
        super(id, username, email, firstName, lastName, dob, gender, password, address);
        this.myKadNumber = myKadNumber;
    }

    public MyKadNumber getMyKadNumber(){
        return this.myKadNumber;
    }

    @Override
    public String getType() {
        return "Citizen";
    }

    public String toString() {
        return String.join("\t", super.toString(), this.myKadNumber.toString());
    }
}
