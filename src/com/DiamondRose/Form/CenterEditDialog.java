package com.DiamondRose.Form;

import com.DiamondRose.Main;
import com.DiamondRose.Util.Address;
import com.DiamondRose.Vaccine.VaccineCenter;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import java.awt.event.*;
import java.text.ParseException;

final public class CenterEditDialog extends JDialog {

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField centerNameTextField;
    private JTextField addressStateTextField;
    private JTextField addressCityTextField;
    private JFormattedTextField addressZipTextField;
    private JTextField addressCountryTextField;

    private final VaccineCenter center;

    public CenterEditDialog(VaccineCenter center) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        this.center = center;
        this.setTitle("Center Editor - " + center.name + " (#" + center.id + ")");

        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        this.centerNameTextField.setText(center.name);

        try{
            this.addressZipTextField.setFormatterFactory(new DefaultFormatterFactory(new MaskFormatter("#####"))); // zip code must be 5 numeric digits
        }catch(ParseException e){
            throw new RuntimeException(e);
        }

        this.addressCityTextField.setText(center.address.city);
        this.addressCountryTextField.setText(center.address.country);
        this.addressStateTextField.setText(center.address.state);
        this.addressZipTextField.setText(Integer.toString(center.address.zip));
    }

    private void onOK() {
        String newCenterName = this.centerNameTextField.getText();
        if(newCenterName.length() < 3){
            JOptionPane.showMessageDialog(this, "Center Name must be at least 3 characters in length","Failed to edit vaccine center", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Address newCenterAddress;
        try{
            newCenterAddress = new Address(
                this.addressCountryTextField.getText(),
                this.addressCityTextField.getText(),
                this.addressStateTextField.getText(),
                Integer.parseInt(this.addressZipTextField.getText())
            );
        }catch(IllegalArgumentException ex){
            if(ex instanceof NumberFormatException){
                JOptionPane.showMessageDialog(this, "Invalid value supplied for zip code", "Failed to edit vaccine center", JOptionPane.ERROR_MESSAGE);
            }else{
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Failed to edit vaccine center", JOptionPane.ERROR_MESSAGE);
            }
            return;
        }

        this.center.name = newCenterName;
        this.center.address = newCenterAddress;
        Main.getVaccineCenterManager().saveToFile();
        dispose();
    }

    private void onCancel() {
        dispose();
    }
}
