package com.DiamondRose.Vaccine;

import com.DiamondRose.Main;

import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

final public class StockManager {

    private final HashMap<Long, Integer> distributableStock = new HashMap<>();
    private final String filePath;

    public StockManager(String dStockFilePath) {
        this.filePath = dStockFilePath;
        this.loadFromFile();
        Main.getVaccineManager().addVaccineDeleteListener(vaccine -> {
            this.distributableStock.remove(vaccine.id);
            this.saveToFile();
        });
        Main.getVaccineCenterManager().addVaccineCenterDeleteListener(center -> {
            center.getAllVaccineStock().forEach((vaccine, stock) -> this.setDistributableStock(vaccine, this.getDistributableStock(vaccine) + stock));
            this.saveToFile();
        });
    }

    private void loadFromFile() {
        FileReader fileReader;
        try{
            fileReader = new FileReader(this.filePath);
        }catch(FileNotFoundException e){
            return;
        }
        Scanner scanner = new Scanner(fileReader);
        while(scanner.hasNextLine()){
            String[] data = scanner.nextLine().split("=");
            this.distributableStock.put(Long.parseLong(data[0]), Integer.parseInt(data[1]));
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
        this.distributableStock.forEach((id, stock) -> {
            printWriter.write(id + "=" + stock);
            printWriter.write("\n");
        });
        printWriter.close();
    }

    public HashMap<Long, Integer> get(){
        return this.distributableStock;
    }

    public void setDistributableStock(Vaccine vaccine, int stock){
        if(stock <= 0){
            this.distributableStock.remove(vaccine.id);
        }else{
            this.distributableStock.put(vaccine.id, stock);
        }
        this.saveToFile();
    }

    public int getDistributableStock(Vaccine vaccine){
        return this.distributableStock.getOrDefault(vaccine.id, 0);
    }
}
