package com.DiamondRose;

import com.DiamondRose.Appointment.AppointmentManager;
import com.DiamondRose.Form.LoginForm;
import com.DiamondRose.User.UserManager;
import com.DiamondRose.Vaccine.StockManager;
import com.DiamondRose.Vaccine.VaccineCenterManager;
import com.DiamondRose.Vaccine.VaccineManager;

import javax.swing.*;

final public class Main{

    private static UserManager userManager;
    private static VaccineCenterManager vaccineCenterManager;
    private static VaccineManager vaccineManager;
    private static AppointmentManager appointmentManager;
    private static StockManager stockManager;

    public static void main(String[] args) {
        Main.userManager = new UserManager("person.txt");
        Main.vaccineCenterManager = new VaccineCenterManager("vaccine_center.txt");
        Main.vaccineManager = new VaccineManager("vaccine.txt");
        Main.appointmentManager = new AppointmentManager("appointment.txt");
        Main.stockManager = new StockManager("dist_stock.txt");
        Main.renderGUI();
    }

    public static UserManager getUserManager() {
        return Main.userManager;
    }

    public static VaccineCenterManager getVaccineCenterManager() {
        return Main.vaccineCenterManager;
    }

    public static VaccineManager getVaccineManager() {
        return Main.vaccineManager;
    }

    public static AppointmentManager getAppointmentManager() {
        return Main.appointmentManager;
    }

    public static StockManager getStockManager() {
        return Main.stockManager;
    }

    private static void renderGUI(){
        JFrame frame = new JFrame();
        frame.setContentPane(new LoginForm(frame).panel);
        frame.setSize(frame.getContentPane().getPreferredSize());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }
}