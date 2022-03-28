package com.DiamondRose.Form.Util;

import com.DiamondRose.Form.SessionMainMenuForm;
import com.DiamondRose.Main;
import com.DiamondRose.User.User;

import javax.swing.*;
import java.util.ArrayList;

final public class UserSearchKeyListener extends SearchKeyListener{

	private final SessionMainMenuForm menu;
	private final JComboBox<String> category;

	public UserSearchKeyListener(SessionMainMenuForm menu, JTextField textField, JComboBox<String> category){
		super(textField);
		this.menu = menu;
		this.category = category;
		category.addActionListener(e -> this.onAnyKeyChange(this.textField.getText()));
	}

	public void onAnyKeyChange(String query){
		query = query.toLowerCase();
		ArrayList<User> results = new ArrayList<>();
		for(User user : Main.getUserManager().getAll()){
			if(query.isBlank()){
				results.add(user);
			}else{
				switch(this.category.getItemAt(this.category.getSelectedIndex())){
					case "Type":
						if(user.getType().toLowerCase().startsWith(query)){
							results.add(user);
						}
						break;
					case "ID":
						if(user.id.toString().startsWith(query)){
							results.add(user);
						}
						break;
					case "Name":
						if(user.getFullName().toLowerCase().startsWith(query)){
							results.add(user);
						}
						break;
					case "Username":
						if(user.username.toLowerCase().startsWith(query)){
							results.add(user);
						}
						break;
					case "Email":
						if(user.email.toLowerCase().startsWith(query)){
							results.add(user);
						}
						break;
					case "Gender":
						if(user.gender.toString().toLowerCase().startsWith(query)){
							results.add(user);
						}
						break;
					case "Address":
						if(user.address.simplified().toLowerCase().contains(query)){
							results.add(user);
						}
						break;
				}
			}
		}
		this.menu.updatePeopleTab(results);
	}
}
