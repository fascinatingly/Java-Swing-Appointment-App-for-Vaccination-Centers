package com.DiamondRose.Form.Util;

import com.DiamondRose.Appointment.Appointment;
import com.DiamondRose.Form.SessionMainMenuForm;
import com.DiamondRose.Main;

import javax.swing.*;
import java.util.ArrayList;

final public class AppointmentSearchKeyListener extends SearchKeyListener{

	private final SessionMainMenuForm menu;
	private final JComboBox<String> category;

	public AppointmentSearchKeyListener(SessionMainMenuForm menu, JTextField textField, JComboBox<String> category){
		super(textField);
		this.menu = menu;
		this.category = category;
		category.addActionListener(e -> this.onAnyKeyChange(this.textField.getText()));
	}

	public void onAnyKeyChange(String query){
		query = query.toLowerCase();
		ArrayList<Appointment> results = new ArrayList<>();
		for(Appointment appointment : this.menu.getViewableAppointments()){
			if(query.isBlank()){
				results.add(appointment);
			}else{
				switch(this.category.getItemAt(this.category.getSelectedIndex())){
					case "ID":
						if(Long.toString(appointment.id).startsWith(query)){
							results.add(appointment);
						}
						break;
					case "Person":
						if(Main.getUserManager().getById(appointment.personId).getFullName().toLowerCase().startsWith(query)){
							results.add(appointment);
						}
						break;
					case "Vaccine":
						if(Main.getVaccineManager().get(appointment.vaccineId).name.toLowerCase().startsWith(query)){
							results.add(appointment);
						}
						break;
					case "Center":
						if(Main.getVaccineCenterManager().get(appointment.centerId).name.toLowerCase().startsWith(query)){
							results.add(appointment);
						}
						break;
					case "Status":
						if(appointment.getStatus().name().toLowerCase().startsWith(query)){
							results.add(appointment);
						}
						break;
				}
			}
		}
		this.menu.updateAppointmentsTab(results);
	}
}
