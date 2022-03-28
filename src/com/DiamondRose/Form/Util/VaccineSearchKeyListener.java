package com.DiamondRose.Form.Util;

import com.DiamondRose.Form.SessionMainMenuForm;
import com.DiamondRose.Main;
import com.DiamondRose.Vaccine.Vaccine;

import javax.swing.*;
import java.util.ArrayList;

final public class VaccineSearchKeyListener extends SearchKeyListener{

	private final SessionMainMenuForm menu;
	private final ButtonGroup group;

	public VaccineSearchKeyListener(SessionMainMenuForm menu, JTextField textField, ButtonGroup group){
		super(textField);
		this.menu = menu;
		this.group = group;
		group.getElements().asIterator().forEachRemaining(button -> button.addActionListener(e -> this.onAnyKeyChange(this.textField.getText())));
	}

	public void onAnyKeyChange(String query){
		query = query.toLowerCase();
		ArrayList<Vaccine> results = new ArrayList<>();
		for(Vaccine vaccine : Main.getVaccineManager().getAll()){
			if(query.isBlank()){
				results.add(vaccine);
			}else{
				switch(this.group.getSelection().getActionCommand()){
					case "needle":
						if(vaccine.name.toLowerCase().contains(query)){
							results.add(vaccine);
						}
						break;
					case "prefix":
						if(vaccine.name.toLowerCase().startsWith(query)){
							results.add(vaccine);
						}
						break;
				}
			}
		}
		this.menu.updateVaccinesTab(results);
	}
}
