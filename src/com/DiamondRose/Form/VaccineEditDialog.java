package com.DiamondRose.Form;

import com.DiamondRose.Main;
import com.DiamondRose.Vaccine.Vaccine;
import com.DiamondRose.Vaccine.VaccineManager;

import javax.swing.*;
import java.awt.event.*;

final public class VaccineEditDialog extends JDialog{
	private JPanel contentPane;
	private JButton buttonOK;
	private JButton buttonCancel;
	private JTextField nameTextField;
	private JTextField dosesTextField;
	private JTextField daysBetweenDosesTextField;

	private final Vaccine vaccine;

	public VaccineEditDialog(Vaccine vaccine){
		setContentPane(contentPane);
		setModal(true);
		getRootPane().setDefaultButton(buttonOK);

		this.setTitle("Vaccine Editor - " + vaccine.name + " (#" + vaccine.id + ")");

		this.vaccine = vaccine;

		this.buttonOK.addActionListener(e -> this.onOK());
		this.buttonCancel.addActionListener(e -> this.onCancel());

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				onCancel();
			}
		});

		contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

		this.nameTextField.setText(vaccine.name);
		this.dosesTextField.setText(Integer.toString(vaccine.dose));
		this.daysBetweenDosesTextField.setText(Integer.toString(vaccine.daysBetweenDoses));
	}

	private void onOK(){
		String newName = this.nameTextField.getText();
		String newDosesInput = this.dosesTextField.getText();
		String newDaysBetweenDosesInput = this.daysBetweenDosesTextField.getText();

		Vaccine vaccine;
		try{
			vaccine = VaccineManager.buildVaccineFromUserInput(Integer.toString(Integer.MAX_VALUE), newName, newDosesInput, newDaysBetweenDosesInput);
		}catch(RuntimeException e){
			JOptionPane.showMessageDialog(this, e.getMessage(), "Failed to edit the vaccine", JOptionPane.ERROR_MESSAGE);
			return;
		}

		this.vaccine.name = vaccine.name;
		this.vaccine.dose = vaccine.dose;
		this.vaccine.daysBetweenDoses = vaccine.daysBetweenDoses;
		Main.getVaccineManager().saveToFile();

		dispose();
	}

	private void onCancel(){
		dispose();
	}
}
