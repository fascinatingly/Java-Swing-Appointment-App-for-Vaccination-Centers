package com.DiamondRose.User;

import com.DiamondRose.Util.Address;
import com.DiamondRose.Util.Gender;

import java.time.LocalDate;
import java.util.UUID;

final public class Personnel extends User{

	public static Personnel fromString(String string) {
		String[] data = string.split("\t");
		return new Personnel(UUID.fromString(data[0]), data[1], data[2], data[3], data[4], LocalDate.parse(data[5]), Gender.valueOf(data[6]), data[7], Address.fromString(data[8]));
	}

	public Personnel(UUID id, String username, String email, String firstName, String lastName, LocalDate dob, Gender gender, String password, Address address) {
		super(id, username, email, firstName, lastName, dob, gender, password, address);
	}

	@Override
	public String getType() {
		return "Employee";
	}

	@Override
	public boolean isEmployee(){
		return true;
	}
}

