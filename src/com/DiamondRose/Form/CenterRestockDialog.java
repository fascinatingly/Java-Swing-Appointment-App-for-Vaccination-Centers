package com.DiamondRose.Form;

import com.DiamondRose.Main;
import com.DiamondRose.Vaccine.Vaccine;
import com.DiamondRose.Vaccine.VaccineCenter;

import javax.swing.*;
import java.awt.event.*;
import java.util.Objects;

final public class CenterRestockDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox<String> availableVaccineComboBox;
    private JSpinner restockQuantitySpinner;
    private JLabel statusJLabel;
    private final VaccineCenter center;

    public CenterRestockDialog(VaccineCenter center) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        this.center = center;

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        this.setTitle(center.name + " (#" + center.id + ") Vaccination Center");
        for(Vaccine vaccine : Main.getVaccineManager().getAll()){
            this.availableVaccineComboBox.addItem(vaccine.name);
        }

        this.statusJLabel.setVisible(false);

        this.restockQuantitySpinner.setValue(100);
        Runnable onSpinnerOrComboBoxChange = () -> {
            Vaccine vaccine = Objects.requireNonNull(Main.getVaccineManager().get(this.availableVaccineComboBox.getItemAt(this.availableVaccineComboBox.getSelectedIndex())));
            int quantity = (int) this.restockQuantitySpinner.getValue();
            this.statusJLabel.setText(
                "You are about to restock " + quantity + " doses of " + vaccine.name + " vaccine in " + center.name + " Vaccination Center.\n" +
                "After this operation, the vaccination center will have a total of " + (quantity + center.getVaccineStock(vaccine)) + " dose(s) of " + vaccine.name + " vaccine."
            );
        };
        this.restockQuantitySpinner.addChangeListener(e -> onSpinnerOrComboBoxChange.run());
        this.availableVaccineComboBox.addActionListener(e -> onSpinnerOrComboBoxChange.run());
        onSpinnerOrComboBoxChange.run();
    }

    private void onOK() {
        Vaccine vaccine = Objects.requireNonNull(Main.getVaccineManager().get(this.availableVaccineComboBox.getItemAt(this.availableVaccineComboBox.getSelectedIndex())));
        int quantity = (int) this.restockQuantitySpinner.getValue();
        int availableQuantity = Main.getStockManager().getDistributableStock(vaccine);
        if(quantity > availableQuantity){
            JOptionPane.showMessageDialog(this, "There are only " + availableQuantity + " dose(s) of " + vaccine.name + " available in stock.", "Failed to restock vaccinations", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Main.getStockManager().setDistributableStock(vaccine, availableQuantity - quantity);
        this.center.setVaccineStock(vaccine, this.center.getVaccineStock(vaccine) + quantity);
        Main.getVaccineCenterManager().saveToFile();
        dispose();
    }

    private void onCancel() {
        dispose();
    }
}
