package com.DiamondRose.Form;

import com.DiamondRose.Main;
import com.DiamondRose.User.User;
import com.DiamondRose.User.UserManager;
import com.DiamondRose.Util.Address;
import com.DiamondRose.Util.Gender;
import com.github.lgooddatepicker.components.DatePicker;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import java.awt.event.*;
import java.text.ParseException;
import java.time.LocalDate;

final public class PersonEditDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField firstNameTextField;
    private JTextField lastNameTextField;
    private DatePicker dateOfBirthDatePicker;
    private JTextField usernameTextField;
    private JTextField emailTextField;
	private JPasswordField passwordField;
    private JComboBox<Gender> genderComboBox;
    private JTextField addressStateTextField;
    private JTextField addressCityTextField;
    private JFormattedTextField addressZipTextField;
    private JTextField addressCountryTextField;

    private final User user;

    public PersonEditDialog(User user) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        for(Gender gender : Gender.values()){
            this.genderComboBox.addItem(gender);
        }

        this.setTitle("Editing " + user.getFullName() + " (#" + user.id + ")");
        this.setIconImage(user.getIcon().getImage());
        this.user = user;

        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        try{
            this.addressZipTextField.setFormatterFactory(new DefaultFormatterFactory(new MaskFormatter("#####"))); // zip code must be 5 numeric digits
        }catch(ParseException e){
            throw new RuntimeException(e);
        }

        this.firstNameTextField.setText(user.firstName);
        this.lastNameTextField.setText(user.lastName);
        this.dateOfBirthDatePicker.setDate(user.dob);
        this.usernameTextField.setText(user.username);
        this.emailTextField.setText(user.email);
        this.passwordField.setText(user.password);
        this.genderComboBox.setSelectedItem(user.gender);

        this.addressCityTextField.setText(user.address.city);
        this.addressStateTextField.setText(user.address.state);
        this.addressCountryTextField.setText(user.address.country);
        this.addressZipTextField.setText(Integer.toString(user.address.zip));
    }

    private void onOK() {
        String newFirstName = this.firstNameTextField.getText();
        String newLastName = this.lastNameTextField.getText();
        LocalDate newDob = this.dateOfBirthDatePicker.getDate();
        String newUsername = this.usernameTextField.getText();
        String newEmail = this.emailTextField.getText();
        String newPassword = new String(this.passwordField.getPassword());
        Gender newGender = this.genderComboBox.getItemAt(this.genderComboBox.getSelectedIndex());

        Address address;
        if(this.addressCountryTextField.getText().equals("null")){
            address = Address.NULL;
        }else{
            try{
                address = new Address(
                    this.addressCountryTextField.getText(),
                    this.addressCityTextField.getText(),
                    this.addressStateTextField.getText(),
                    Integer.parseInt(this.addressZipTextField.getText())
                );
            }catch(IllegalArgumentException ex){
                if(ex instanceof NumberFormatException){
                    JOptionPane.showMessageDialog(this, "Invalid value supplied for zip code", "Failed to save changes", JOptionPane.ERROR_MESSAGE);
                }else{
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Failed to save changes", JOptionPane.ERROR_MESSAGE);
                }
                return;
            }
        }

        try{
            UserManager.validateUserInput(newFirstName, newLastName, newUsername, newPassword, newEmail, newDob);
        }catch(IllegalArgumentException e){
            JOptionPane.showMessageDialog(this, e.getMessage(), "Failed to save changes", JOptionPane.ERROR_MESSAGE);
            return;
        }

        User existingSameUsername = Main.getUserManager().getByUsername(newUsername);
        if(existingSameUsername != null && existingSameUsername.id != this.user.id){
            JOptionPane.showMessageDialog(this, "This username is already taken.", "Failed to save changes", JOptionPane.ERROR_MESSAGE);
            return;
        }

        User existingSameEmail = Main.getUserManager().getByEmail(newUsername);
        if(existingSameEmail != null && existingSameEmail.id != this.user.id){
            JOptionPane.showMessageDialog(this, "This email is already taken.", "Failed to save changes", JOptionPane.ERROR_MESSAGE);
            return;
        }

        user.firstName = newFirstName;
        user.lastName = newLastName;
        user.dob = newDob;
        user.username = newUsername;
        user.email = newEmail;
        user.password = newPassword;
        user.gender = newGender;
        user.address = address;
        Main.getUserManager().saveToFile();
        dispose();
    }

    private void onCancel() {
        dispose();
    }
}
