package com.DiamondRose.Form;

import com.DiamondRose.Main;
import com.DiamondRose.Util.Address;
import com.DiamondRose.Vaccine.VaccineCenter;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;

final public class CenterCreatorDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JProgressBar pendingProgressBar;
    private JTextField centerIdTextField;
    private JTextField centerNameTextField;
    private JTextField addressStateTextField;
    private JTextField addressCityTextField;
    private JFormattedTextField addressZipTextField;
    private JTextField addressCountryTextField;

    public CenterCreatorDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        this.setTitle("Vaccine Center Registration");

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
    }

    private void onOK() {
        long centerId;
        try {
            centerId = Long.parseLong(this.centerIdTextField.getText());
        } catch(NumberFormatException e){
            JOptionPane.showMessageDialog(this, "Center ID must be numeric","Failed to register vaccine center", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(centerId < 0){
            JOptionPane.showMessageDialog(this, "Center ID must be greater than 0","Failed to register vaccine center", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (Main.getVaccineCenterManager().get(centerId) != null) {
            JOptionPane.showMessageDialog(this, "The ID entered already exists. Please pick another ID.","Failed to register vaccine center.", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String centerName = this.centerNameTextField.getText();
        if(centerName.length() < 3) {
            JOptionPane.showMessageDialog(this, "Center Name must be at least 3 characters in length","Failed to register vaccine center", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Address address;
        try{
            address = new Address(
                this.addressCountryTextField.getText(),
                this.addressCityTextField.getText(),
                this.addressStateTextField.getText(),
                Integer.parseInt(this.addressZipTextField.getText())
            );
        }catch(IllegalArgumentException ex){
            if(ex instanceof NumberFormatException){
                JOptionPane.showMessageDialog(this, "Invalid value supplied for zip code", "Failed to register vaccine center", JOptionPane.ERROR_MESSAGE);
            }else{
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Failed to register vaccine center", JOptionPane.ERROR_MESSAGE);
            }
            return;
        }

        Main.getVaccineCenterManager().register(new VaccineCenter(centerId, centerName, address));
        this.setEnabled(false);
        Thread t = new Thread(() -> {
            this.pendingProgressBar.setMinimum(0);
            this.pendingProgressBar.setMaximum(100);
            this.pendingProgressBar.setValue(0);
            this.pendingProgressBar.setStringPainted(true);
            this.pendingProgressBar.setOrientation(JProgressBar.HORIZONTAL);
            this.pendingProgressBar.setForeground(Color.BLUE);
            for(int i = 1; i <= 100; i++){
                this.pendingProgressBar.setValue(i);
                try {
                    Thread.sleep(50);
                }catch (InterruptedException ignored){
                }
            }

            dispose();
        });
        t.start();
    }

    private void onCancel() {
        dispose();
    }
}

