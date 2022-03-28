package com.DiamondRose.Appointment;

import com.DiamondRose.Main;
import com.DiamondRose.User.User;

import java.io.*;
import java.util.*;

final public class AppointmentManager {

    private final Map<Long, Appointment> appointments = new HashMap<>();
    private final String filePath;

    public AppointmentManager(String filePath) {
        this.filePath = filePath;
        this.loadFromFile();
        Main.getVaccineManager().addVaccineDeleteListener(vaccine -> {
            for(Appointment appointment : this.appointments.values()){
                if(appointment.vaccineId == vaccine.id){
                    this.appointments.remove(appointment.id);
                }
            }
            this.saveToFile();
        });
        Main.getVaccineCenterManager().addVaccineCenterDeleteListener(center -> {
            for(Appointment appointment : this.appointments.values()){
                if(appointment.centerId == center.id){
                    this.appointments.remove(appointment.id);
                }
            }
            this.saveToFile();
        });
        this.refreshAppointments();
    }

    private void loadFromFile() {
        //read file and put appointments into this.appointments
        FileReader fileReader;
        try{
            fileReader = new FileReader(this.filePath);
        }catch(FileNotFoundException e){
            return; // don't proceed further if file does not exist
        }

        // read all long elements in file back into appointmentList using Scanner
        Scanner scanner = new Scanner(fileReader);
        while(scanner.hasNext()){
            Appointment appointment = Appointment.fromString(scanner.nextLine());
            this.appointments.put(appointment.id, appointment);
        }
        scanner.close();
    }

    public void saveToFile() {
        //write this.appointments into file
        FileWriter fileWriter;
        try{
            fileWriter = new FileWriter(this.filePath);
        }catch(IOException e){
            throw new RuntimeException(e);
        }

        PrintWriter printWriter = new PrintWriter(fileWriter);
        // write every element in appointmentList into file with space as separator
        for(Appointment appointment : this.appointments.values()){
            printWriter.write(appointment.toString() + "\n");
        }
        printWriter.close();
    }

    public Appointment getAppointmentById(long id) {
        return this.appointments.get(id);
    }

    public long getNextAppointmentId(){
        long maxAppointmentId = 0;
        for(Appointment appointment : this.appointments.values()){
            if(appointment.id > maxAppointmentId){
                maxAppointmentId = appointment.id;
            }
        }
        return maxAppointmentId + 1;
    }

    public void createNewAppointment(long id, Appointment appointment) {
        this.appointments.put(id, appointment);
        this.saveToFile();
    }

    public void refreshAppointments(){
        boolean changed = false;
        for(Appointment appointment : this.appointments.values()){
            if(appointment.refreshStatus()){
                changed = true;
            }
        }
        if(changed){
            this.saveToFile();
        }
    }

    public Collection<Appointment> getAllAppointments() {
        return this.appointments.values();
    }

    public AppointmentUserStatus getUserStatus(User user){
        return new AppointmentUserStatus(this.getAllAppointments().stream().filter(appointment -> appointment.personId.equals(user.id)).toList());
    }

    public void delete(Appointment appointment){
        this.appointments.remove(appointment.id);
        this.saveToFile();
    }
}