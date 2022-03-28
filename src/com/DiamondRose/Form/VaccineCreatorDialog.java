package com.DiamondRose.Form;

import com.DiamondRose.Main;
import com.DiamondRose.Vaccine.Vaccine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

final public class VaccineCreatorDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JProgressBar loadingProgressBar;
    private JTextField vaccineNameTextField;
    private JTextField vaccineIdTextField;
    private JTextField daysBetweenDosesTextField;
    private JComboBox<Integer> dosesComboBox;

    public VaccineCreatorDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        this.setTitle("Vaccine Creator");

        for(int i = 1; i < 4; i++){
            this.dosesComboBox.addItem(i);
        }

        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        int id;
        String vaccineName = this.vaccineNameTextField.getText();
        int doses = this.dosesComboBox.getItemAt(this.dosesComboBox.getSelectedIndex());
        int daysBetweenDoses;

        try {
            id = Integer.parseInt(this.vaccineIdTextField.getText());
        }catch(NumberFormatException e){
            JOptionPane.showMessageDialog(this, "Please supply an integer value for ID.", "Invalid value for ID", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(id <= 0){
            JOptionPane.showMessageDialog(this, "Please supply a positive integer value for ID.", "Invalid value for ID", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            daysBetweenDoses = Integer.parseInt(this.daysBetweenDosesTextField.getText());
        }catch(NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid number for days between doses. Please enter a valid number for days between doses.", "Invalid value for days days between doses", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (daysBetweenDoses < 1) {
            JOptionPane.showMessageDialog(this, "Please supply a positive integer value to days between doses", "Invalid value for days between doses", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(vaccineName.length() < 3){
            JOptionPane.showMessageDialog(this, "Please supply a valid vaccine name.", "Invalid value for vaccine name", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(Main.getVaccineManager().get(id) != null){
            JOptionPane.showMessageDialog(this, "A vaccine with the given ID already exists.", "Illegal value for vaccine ID", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Vaccine newVaccine = new Vaccine(id, vaccineName, doses, daysBetweenDoses);
        Main.getVaccineManager().register(newVaccine);

        for(Component component : this.contentPane.getComponents()){
            component.setEnabled(false);
        }
        Thread t = new Thread(() -> {
            this.loadingProgressBar.setMinimum(0);
            this.loadingProgressBar.setMaximum(100);
            this.loadingProgressBar.setValue(0);
            this.loadingProgressBar.setStringPainted(true);
            this.loadingProgressBar.setOrientation(JProgressBar.HORIZONTAL);
            this.loadingProgressBar.setForeground(Color.BLUE);
            for(int i = 1; i <= 100; i++){
                this.loadingProgressBar.setValue(i);
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