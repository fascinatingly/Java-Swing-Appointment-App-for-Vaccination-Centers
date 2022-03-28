package com.DiamondRose.Vaccine;

import com.DiamondRose.Main;

import java.io.*;
import java.util.*;
import java.util.function.Consumer;

final public class VaccineManager{

	public static Vaccine buildVaccineFromUserInput(String idInput, String vaccineName, String dosesInput, String daysBetweenDosesInput){
		int id;
		int doses;
		int daysBetweenDoses;
		try {
			id = Integer.parseInt(idInput);
		}catch(NumberFormatException e){
			throw new RuntimeException("An integer value must be supplied for ID");
		}

		if(id <= 0){
			throw new RuntimeException("A positive integer value must be supplied for ID");
		}

		try{
			doses = Integer.parseInt(dosesInput);
		}catch(NumberFormatException e){
			throw new RuntimeException("Please enter a valid number for doses.");
		}

		if(doses < 1){
			throw new RuntimeException("Please supply a positive integer value to doses");
		}

		if(vaccineName.length() < 3){
			throw new RuntimeException("Please supply a valid vaccine name");
		}

		if(Main.getVaccineManager().get(id) != null){
			throw new RuntimeException("A vaccine with the given ID already exists");
		}
		try {
			id = Integer.parseInt(idInput);
		}catch(NumberFormatException e){
			throw new RuntimeException("Supply an integer value for ID.");
		}
		if(id <= 0){
			throw new RuntimeException("Please supply a positive integer value for ID.");
		}

		try{
			daysBetweenDoses = Integer.parseInt(daysBetweenDosesInput);
		}catch(NumberFormatException e){
			throw new RuntimeException("Invalid number for days between doses. Please enter a valid number for days between doses.");
		}
		if(daysBetweenDoses < 1){
			throw new RuntimeException("Please supply a positive integer value to days between doses");
		}

		if(Main.getVaccineManager().get(id) != null){
			throw new RuntimeException("A vaccine with the given ID already exists.");
		}

		return new Vaccine(id, vaccineName, doses, daysBetweenDoses);
	}

	private final String filePath;
	private final Map<Long, Vaccine> vaccines = new HashMap<>();
	private final ArrayList<Consumer<Vaccine>> vaccineDeleteListeners = new ArrayList<>();

	public VaccineManager(String filePath){
		this.filePath = filePath;
		this.loadFromFile();
	}

	private void loadFromFile(){
		FileReader fileReader;
		try{
			fileReader = new FileReader(this.filePath);
		}catch(FileNotFoundException e){
			return; // don't proceed further if file does not exist
		}

		// read all long elements in file back into appointmentList using Scanner
		Scanner scanner = new Scanner(fileReader);
		while(scanner.hasNextLine()){
			Vaccine vaccine = Vaccine.fromString(scanner.nextLine());
			this.vaccines.put(vaccine.id, vaccine);
		}
		scanner.close();
	}

	public void saveToFile(){
		FileWriter fileWriter;
		try{
			fileWriter = new FileWriter(this.filePath);
		}catch(IOException e){
			throw new RuntimeException(e);
		}

		PrintWriter printWriter = new PrintWriter(fileWriter);
		// write every element in appointmentList into file with space as separator
		for(Vaccine vaccine : this.vaccines.values()){
			printWriter.write(vaccine.toString() + "\n");
		}
		printWriter.close();
	}

	public void register(Vaccine vaccine){
		this.vaccines.put(vaccine.id, vaccine);
		this.saveToFile();
	}

	public Vaccine get(long id){
		return this.vaccines.get(id);
	}
	public Vaccine get(String name) {
		for(Vaccine vaccine : this.getAll()) {
			if(vaccine.name.equalsIgnoreCase(name)) {
				return vaccine;
			}
		}
		return null;
	}

	public Collection<Vaccine> getAll(){
		return this.vaccines.values();
	}

	public void addVaccineDeleteListener(Consumer<Vaccine> listener){
		this.vaccineDeleteListeners.add(listener);
	}

	public void delete(Vaccine vaccine){
		this.vaccines.remove(vaccine.id);
		this.saveToFile();
		this.vaccineDeleteListeners.forEach(listener -> listener.accept(vaccine));
	}
}
