package com.DiamondRose.User;

import com.DiamondRose.Savable;
import com.DiamondRose.Util.Address;
import com.DiamondRose.Util.Gender;

import javax.swing.*;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

abstract public class User implements Savable{

    public final UUID id;
    public String username;
    public String email;
    public String firstName;
    public String lastName;
    public LocalDate dob;
    public Gender gender;
    public String password;
    public Address address;

    public User(UUID id, String username, String email, String firstName, String lastName, LocalDate dob, Gender gender, String password, Address address) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.gender = gender;
        this.password = password;
        this.address = address;
    }

    public String getType(){
        return "User";
    }

    public String getFullName(){
        return this.firstName + " " + this.lastName;
    }

    public String getCustomIconPath(){
        return "resources/user/user-" + this.id + ".png";
    }

    public ImageIcon getIcon(){
        if(new File(this.getCustomIconPath()).exists()){
            return new ImageIcon(this.getCustomIconPath());
        }
        return new ImageIcon("resources/user/default.png");
    }

    public boolean isEmployee(){
        return false;
    }

    public String toString() {
        return String.join("\t",
            this.id.toString(),
            this.username,
            this.email,
            this.firstName,
            this.lastName,
            this.dob.format(DateTimeFormatter.ISO_LOCAL_DATE),
            this.gender.name(),
            this.password,
            this.address.toString()
        );
    }
}
