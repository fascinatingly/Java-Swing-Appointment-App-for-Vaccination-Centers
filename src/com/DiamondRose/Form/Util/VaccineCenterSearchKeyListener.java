package com.DiamondRose.Form.Util;

import com.DiamondRose.Form.SessionMainMenuForm;
import com.DiamondRose.Main;
import com.DiamondRose.Vaccine.VaccineCenter;

import javax.swing.*;
import java.util.ArrayList;

final public class VaccineCenterSearchKeyListener extends SearchKeyListener{

	private final SessionMainMenuForm menu;
	private final ButtonGroup group;

	public VaccineCenterSearchKeyListener(SessionMainMenuForm menu, JTextField textField, ButtonGroup group){
		super(textField);
		this.menu = menu;
		this.group = group;
		group.getElements().asIterator().forEachRemaining(button -> button.addActionListener(e -> this.onAnyKeyChange(this.textField.getText())));
	}

	public void onAnyKeyChange(String query){
		query = query.toLowerCase();
		ArrayList<VaccineCenter> results = new ArrayList<>();
		for(VaccineCenter center : Main.getVaccineCenterManager().getAll()){
			if(query.isBlank()){
				results.add(center);
			}else{
				switch(this.group.getSelection().getActionCommand()){
					case "needle":
						if(center.name.toLowerCase().contains(query)){
							results.add(center);
						}
						break;
					case "prefix":
						if(center.name.toLowerCase().startsWith(query)){
							results.add(center);
						}
						break;
				}
			}
		}
		this.menu.updateCentersTab(results);
	}
}
