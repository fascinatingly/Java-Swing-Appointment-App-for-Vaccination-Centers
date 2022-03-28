package com.DiamondRose.Util;

import com.DiamondRose.Savable;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Random;

final public class MyKadNumber implements Savable{

	public static MyKadNumber fromString(String string){
		string = string.replaceAll("-", "");
		if(string.length() != 12 || !string.chars().allMatch(Character::isDigit)){
			throw new IllegalArgumentException("Invalid MyKad number supplied");
		}

		int year = Integer.parseInt((string.charAt(8) == '0' ? "20" : "19") + string.substring(0, 2));
		LocalDate dob;
		try{
			Month month = Month.of(Integer.parseInt(string.substring(2, 4)));
			dob = LocalDate.of(year, month, Integer.parseInt(string.substring(4, 6)));
		}catch(DateTimeException e){
			throw new IllegalArgumentException("Invalid MyKad number supplied", e);
		}

		MalaysianState state;
		try{
			state = MalaysianState.fromCode(Integer.parseInt(string.substring(6, 8)));
		}catch(IllegalArgumentException e){
			throw new IllegalArgumentException("Invalid MyKad number supplied", e);
		}

		return new MyKadNumber(dob, state, Integer.parseInt(string.substring(8, 11)), Integer.parseInt(string.substring(11)));
	}

	public static MyKadNumber random(){
		Random random = new Random();

		YearMonth yearMonth = YearMonth.of(random.nextInt(1980, Year.now().getValue() - 9), random.nextInt(1, 12 + 1));
		int day = random.nextInt(1, yearMonth.lengthOfMonth() + 1);
		LocalDate dob = LocalDate.of(yearMonth.getYear(), yearMonth.getMonth(), day);

		MalaysianState state = MalaysianState.random();

		int specialNumber = dob.getYear() >= 2000 ? random.nextInt(0, 100) : random.nextInt(100, 1000);
		int genderNumber = random.nextInt(0, 10);
		return new MyKadNumber(dob, state, specialNumber, genderNumber);
	}

	private final LocalDate dateOfBirth;
	private final MalaysianState state;
	private final int specialNumber;
	private final int genderNumber;

	public MyKadNumber(LocalDate dateOfBirth, MalaysianState state, int specialNumber, int genderNumber){
		if(specialNumber < 0 || specialNumber > 999){
			throw new IllegalArgumentException("Invalid special number supplied");
		}
		if(genderNumber < 0 || genderNumber > 9){
			throw new IllegalArgumentException("Invalid gender number supplied");
		}

		this.dateOfBirth = dateOfBirth;
		this.state = state;
		this.specialNumber = specialNumber;
		this.genderNumber = genderNumber;
	}

	public LocalDate getDateOfBirth(){
		return this.dateOfBirth;
	}

	public MalaysianState getState(){
		return this.state;
	}

	public int getSpecialNumber(){
		return this.specialNumber;
	}

	public int getGenderNumber(){
		return this.genderNumber;
	}

	public Gender getGender(){
		return this.genderNumber % 2 == 0 ? Gender.FEMALE : Gender.MALE;
	}

	public String getNumber(){
		return "%s-%s-%s%s".formatted(
			new SimpleDateFormat("yyMMdd").format(Date.valueOf(this.dateOfBirth)),
			String.format("%02d", this.state.getCode()),
			String.format("%03d", this.specialNumber),
			this.genderNumber
		);
	}

	public String getNumberWithoutDashes(){
		return this.getNumber().replaceAll("-", "");
	}

	@Override
	public String toString(){
		return this.getNumberWithoutDashes();
	}
}
