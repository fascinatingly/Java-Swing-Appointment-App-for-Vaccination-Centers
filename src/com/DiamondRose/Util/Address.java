package com.DiamondRose.Util;

import com.DiamondRose.Savable;

final public class Address implements Savable{

	public static final Address NULL = new Address("null", "null", "null", 10000);

	public static Address fromString(String string){
		if(string.equals("null")){
			return NULL;
		}

		String[] data = string.split("\\|");
		return new Address(data[0], data[1], data[2], Integer.parseInt(data[3]));
	}

	public String country;
	public String city;
	public String state;
	public int zip;

	public Address(String country, String city, String state, int zip){
		if(country.length() < 3){
			throw new IllegalArgumentException("Invalid value supplied for country");
		}
		if(city.length() < 3){
			throw new IllegalArgumentException("Invalid value supplied for city");
		}
		if(state.length() < 3){
			throw new IllegalArgumentException("Invalid value supplied for state");
		}
		if(Integer.toString(zip).length() != 5){
			throw new IllegalArgumentException("Invalid value supplied for zip code");
		}

		this.country = country;
		this.city = city;
		this.state = state;
		this.zip = zip;
	}

	public String simplified(){
		return "%s, %s %d, %s".formatted(this.city, this.state, this.zip, this.country);
	}

	@Override
	public String toString(){
		return this == NULL ? "null" : String.join("|", this.country, this.city, this.state, Integer.toString(this.zip));
	}
}
