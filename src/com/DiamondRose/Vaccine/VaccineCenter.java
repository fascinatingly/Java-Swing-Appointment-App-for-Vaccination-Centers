package com.DiamondRose.Vaccine;

import com.DiamondRose.Main;
import com.DiamondRose.Savable;
import com.DiamondRose.Util.Address;

import java.util.HashMap;
import java.util.StringJoiner;

final public class VaccineCenter implements Savable{

	public static VaccineCenter fromString(String string){
		String[] data = string.split("\t");
		HashMap<Long, Integer> vaccineStock = new HashMap<>();
		for(String s : data[3].split(",")){
			Long key = Long.parseLong(s.trim().split("=")[0]);
			Integer value = Integer.parseInt(s.trim().split("=")[1]);
			vaccineStock.put(key, value);
		}
		return new VaccineCenter(Long.parseLong(data[0]), data[1], Address.fromString(data[2]), vaccineStock);
	}

	public final long id;
	public String name;
	public Address address;
	public final HashMap<Long, Integer> vaccineStock;

	public VaccineCenter(long id, String name, Address address, HashMap<Long, Integer> vaccineStock){
		this.id = id;
		this.name = name;
		this.address = address;
		this.vaccineStock = vaccineStock;
	}

	public VaccineCenter(long id, String name, Address address){
		this(id, name, address, new HashMap<>());
	}

	public void setVaccineStock(Vaccine vaccine, int stock){
		if(stock <= 0){
			this.vaccineStock.remove(vaccine.id);
		}else{
			this.vaccineStock.put(vaccine.id, stock);
		}
	}

	public int getVaccineStock(Vaccine vaccine){
		return this.vaccineStock.getOrDefault(vaccine.id, 0);
	}

	public HashMap<Vaccine, Integer> getAllVaccineStock(){
		HashMap<Vaccine, Integer> stock = new HashMap<>();
		this.vaccineStock.forEach((id, amount) -> stock.put(Main.getVaccineManager().get(id), amount));
		return stock;
	}

	public String toString(){
		StringJoiner vaccineStockSj = new StringJoiner(",");
		this.vaccineStock.forEach((key, value) -> vaccineStockSj.add(key + "=" + value));
		return String.join("\t", Long.toString(this.id), this.name, this.address.toString(), vaccineStockSj.toString());
	}
}
