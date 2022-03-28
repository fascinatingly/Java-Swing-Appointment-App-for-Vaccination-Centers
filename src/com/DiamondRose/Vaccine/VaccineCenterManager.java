package com.DiamondRose.Vaccine;

import javax.swing.*;
import java.io.*;
import java.util.*;
import java.util.function.Consumer;

final public class VaccineCenterManager{

	private final String filePath;
	private final Map<Long, VaccineCenter> centers = new HashMap<>();
	private final ArrayList<Consumer<VaccineCenter>> vaccineCenterDeleteListeners = new ArrayList<>();

	public VaccineCenterManager(String filePath) {
		this.filePath = filePath;
		this.loadFromFile();
	}

	private void loadFromFile() {
		FileReader fileReader;
		try{
			fileReader = new FileReader(this.filePath);
		}catch(FileNotFoundException e){
			return; // don't proceed further if file does not exist
		}
		Scanner scanner = new Scanner(fileReader);
		while(scanner.hasNextLine()){
			VaccineCenter vaccineCenter = VaccineCenter.fromString(scanner.nextLine());
			this.centers.put(vaccineCenter.id, vaccineCenter);
		}
		scanner.close();
	}

	public void saveToFile() {
		FileWriter fileWriter;
		try{
			fileWriter = new FileWriter(this.filePath);
		}catch(IOException e){
			throw new RuntimeException(e);
		}

		PrintWriter printWriter = new PrintWriter(fileWriter);
		// write every element in appointmentList into file with space as separator
		for(VaccineCenter vaccine : this.centers.values()){
			printWriter.write(vaccine.toString() + "\n");

		}
		printWriter.close();
	}

	public static ArrayList<Vaccine> addStockToComboBox(JComboBox<String> comboBox, VaccineCenter center) {
		comboBox.removeAllItems();
		ArrayList<Vaccine> addedStock = new ArrayList<>();
		center.getAllVaccineStock().forEach((vaccine, stock) -> {
			if(stock > 0){
				comboBox.addItem(vaccine.name + ": " + stock + " remaining");
				addedStock.add(vaccine);
			}
		});
		return addedStock;
	}

	public void register(VaccineCenter vaccineCenter){
		this.centers.put(vaccineCenter.id, vaccineCenter);
		this.saveToFile();
	}

	public VaccineCenter get(long id){
		return this.centers.get(id);
	}
	public VaccineCenter get(String name) {
		for(VaccineCenter vaccineCenter : this.getAll()){
			if(vaccineCenter.name.equalsIgnoreCase(name)){
				return vaccineCenter;
			}
		}
		return null;
	}

	public Collection<VaccineCenter> getAll(){
		return this.centers.values();
	}

	public void addVaccineCenterDeleteListener(Consumer<VaccineCenter> listener){
		this.vaccineCenterDeleteListeners.add(listener);
	}

	public void delete(VaccineCenter center){
		this.centers.remove(center.id);
		this.saveToFile();
		this.vaccineCenterDeleteListeners.forEach(listener -> listener.accept(center));
	}
}
