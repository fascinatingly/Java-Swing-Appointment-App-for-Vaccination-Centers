package com.DiamondRose.Vaccine;

import com.DiamondRose.Savable;

final public class Vaccine implements Savable{

	public static Vaccine fromString(String string){
		String[] data = string.split("\t");
		return new Vaccine(Integer.parseInt(data[0]), data[1], Integer.parseInt(data[2]), Integer.parseInt(data[3]));
	}

	public final long id;
	public String name;
	public int dose;
	public int daysBetweenDoses;

	public Vaccine(long id, String name, int dose, int daysBetweenDoses){
		this.id = id;
		this.name = name;
		this.dose = dose;
		this.daysBetweenDoses = daysBetweenDoses;
	}

	public String toString(){
		return String.join("\t",
			Long.toString(this.id),
			this.name,
			Integer.toString(this.dose),
			Integer.toString(this.daysBetweenDoses)
		);
	}
}

