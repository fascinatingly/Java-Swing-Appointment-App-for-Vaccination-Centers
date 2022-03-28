package com.DiamondRose.Form;

import com.DiamondRose.User.Citizen;
import com.DiamondRose.Main;
import com.DiamondRose.User.NonCitizen;
import com.DiamondRose.User.People;
import com.DiamondRose.User.UserManager;
import com.DiamondRose.Util.Address;
import com.DiamondRose.Util.Gender;
import com.DiamondRose.Util.MyKadNumber;
import com.github.lgooddatepicker.components.DatePicker;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.StringJoiner;
import java.util.UUID;

final public class SignupForm extends JFrame{
	public JPanel panel;
	private JButton backButton;
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JButton signupButton;
	private JTextField emailField;
	private JTextField firstName;
	private JTextField lastName;
	private JFormattedTextField citizenMyKadNumber;
	private JTextField nonCitizenPassportNumber;
	private JTabbedPane accountTypeJTabbedPane;
	private DatePicker dobDatePicker;
	private JTextField cityTextField;
	private JTextField stateTextField;
	private JTextField countryTextField;
	private JFormattedTextField zipTextField;
	private JComboBox<Gender> genderComboBox;

	public SignupForm(JFrame mainFrame){
		this.add(this.panel);
		mainFrame.setSize(this.getPreferredSize());
		mainFrame.setTitle("Vaccination Manager - Registration");

		for(Gender gender : Gender.values()){
			this.genderComboBox.addItem(gender);
		}

		this.dobDatePicker.setDate(LocalDate.now().minusYears(8));

		this.backButton.addActionListener(e -> {
			mainFrame.setContentPane(new LoginForm(mainFrame).panel);
			mainFrame.revalidate();
		});

		try{
			this.citizenMyKadNumber.setFormatterFactory(new DefaultFormatterFactory(new MaskFormatter("############"))); // MyKAD number must be 12 digits
			this.zipTextField.setFormatterFactory(new DefaultFormatterFactory(new MaskFormatter("#####"))); // zip code must be 5 numeric digits
		}catch(ParseException e){
			throw new RuntimeException(e);
		}

		Runnable onMyKadNumberChange = () -> {
			MyKadNumber number;
			try{
				number = MyKadNumber.fromString(this.citizenMyKadNumber.getText());
			}catch(IllegalArgumentException e){
				return;
			}
			this.dobDatePicker.setDate(number.getDateOfBirth());
			this.stateTextField.setText(number.getState().getName());
			this.genderComboBox.setSelectedItem(number.getGender());
		};
		this.citizenMyKadNumber.setText(MyKadNumber.random().getNumberWithoutDashes());
		Runnable onAccountTypeTabChange = () -> {
			if(this.accountTypeJTabbedPane.getSelectedIndex() == 0){
				this.countryTextField.setEnabled(false);
				this.countryTextField.setText("Malaysia");
				this.stateTextField.setEnabled(false);
				this.dobDatePicker.setEnabled(false);
				this.genderComboBox.setEnabled(false);
				onMyKadNumberChange.run();
			}else{
				this.countryTextField.setEnabled(true);
				this.countryTextField.setText("");
				this.stateTextField.setEnabled(true);
				this.stateTextField.setText("");
				this.dobDatePicker.setEnabled(true);
				this.genderComboBox.setEnabled(true);
			}
		};
		this.accountTypeJTabbedPane.addChangeListener(e -> onAccountTypeTabChange.run());
		onAccountTypeTabChange.run();

		onMyKadNumberChange.run();
		this.citizenMyKadNumber.getDocument().addDocumentListener(new DocumentListener(){
			@Override
			public void insertUpdate(DocumentEvent e){
				update();
			}
			@Override
			public void removeUpdate(DocumentEvent e){
				update();
			}
			@Override
			public void changedUpdate(DocumentEvent e){
			}
			private void update(){
				onMyKadNumberChange.run();
			}
		});

		this.signupButton.addActionListener(e -> {
			String username = this.usernameField.getText();
			String email = this.emailField.getText();
			String password = String.valueOf(this.passwordField.getPassword());
			String firstName = this.firstName.getText();
			String lastName = this.lastName.getText();
			LocalDate dob = this.dobDatePicker.getDate();
			Gender gender = this.genderComboBox.getItemAt(this.genderComboBox.getSelectedIndex());
			Address address;
			try{
				address = new Address(
					this.countryTextField.getText(),
					this.cityTextField.getText(),
					this.stateTextField.getText(),
					Integer.parseInt(this.zipTextField.getText())
				);
			}catch(IllegalArgumentException ex){
				if(ex instanceof NumberFormatException){
					JOptionPane.showMessageDialog(this, "Invalid value supplied for zip code", "Registration Failed", JOptionPane.ERROR_MESSAGE);
				}else{
					JOptionPane.showMessageDialog(this, ex.getMessage(), "Registration Failed", JOptionPane.ERROR_MESSAGE);
				}
				return;
			}

			try{
				UserManager.validateUserInput(firstName, lastName, username, password, email, dob);
			}catch(IllegalArgumentException ex){
				JOptionPane.showMessageDialog(this, ex.getMessage(), "Registration Failed", JOptionPane.ERROR_MESSAGE);
				return;
			}

			People newPerson;
			switch(this.accountTypeJTabbedPane.getSelectedIndex()){
				case 0:
					MyKadNumber myKad;
					try{
						myKad = MyKadNumber.fromString(this.citizenMyKadNumber.getText());
					}catch(IllegalArgumentException ex){
						JOptionPane.showMessageDialog(this, "Invalid MyKad number supplied", "Registration Failed", JOptionPane.ERROR_MESSAGE);
						return;
					}
					newPerson = new Citizen(UUID.randomUUID(), username, email, firstName, lastName, dob, gender, password, address, myKad);
					break;
				case 1:
					String passportNumber = this.nonCitizenPassportNumber.getText();
					if(passportNumber.length() < 6){
						JOptionPane.showMessageDialog(this, "Invalid Passport number supplied", "Registration Failed", JOptionPane.ERROR_MESSAGE);
						return;
					}
					newPerson = new NonCitizen(UUID.randomUUID(), username, email, firstName, lastName, dob, gender, password, address, passportNumber);
					break;
				default:
					throw new IllegalStateException("Unexpected value: " + this.accountTypeJTabbedPane.getSelectedIndex());
			}

			try{
				Main.getUserManager().register(newPerson);
			}catch(IllegalArgumentException ex){
				JOptionPane.showMessageDialog(this, ex.getMessage(), "Registration Failed", JOptionPane.ERROR_MESSAGE);
				return;
			}

			JOptionPane.showMessageDialog(this, new StringJoiner("", "<html>", "</html>")
					.add("<p>You have successfully registered for an account.</p>")
					.add("<p>You will now be logged in using your credentials.</p>")
					.add("<p>Be sure to remember your password for future login.</p>")
					.add("<code><b>E-mail Address: </b>" + newPerson.email + "</code><br/>")
					.add("<code><b>Username: </b>" + newPerson.username + "</code>")
					.toString()
				, "Registration Successful", JOptionPane.INFORMATION_MESSAGE);
			mainFrame.setContentPane(new SessionMainMenuForm(mainFrame, newPerson).panel);
			mainFrame.revalidate();
		});
	}
}
