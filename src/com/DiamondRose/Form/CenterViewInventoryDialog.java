package com.DiamondRose.Form;

import com.DiamondRose.Vaccine.VaccineCenter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;

public class CenterViewInventoryDialog extends JDialog{
	private JPanel contentPane;
	private JButton buttonCancel;
	private JTable table;
	private JLabel noVaccinesExistNotice;

	public CenterViewInventoryDialog(VaccineCenter center){
		setContentPane(contentPane);
		setModal(true);

		this.setTitle(center.name + " Vaccine Center Inventory");

		buttonCancel.addActionListener(e -> onCancel());

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				onCancel();
			}
		});

		contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

		DefaultTableModel model = new DefaultTableModel(){
			@Override
			public boolean isCellEditable(int row, int column){
				return false;
			}
		};
		model.addColumn("Vaccine");
		model.addColumn("Stock");
		center.getAllVaccineStock().forEach((vaccine, stock) -> model.addRow(new Object[]{vaccine.name, stock}));
		this.table.setModel(model);
		this.noVaccinesExistNotice.setVisible(center.getAllVaccineStock().isEmpty());
	}

	private void onCancel(){
		dispose();
	}
}
