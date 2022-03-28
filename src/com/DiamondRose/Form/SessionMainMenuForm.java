package com.DiamondRose.Form;

import com.DiamondRose.Appointment.Appointment;
import com.DiamondRose.Appointment.AppointmentManager;
import com.DiamondRose.Appointment.AppointmentUserStatus;
import com.DiamondRose.Form.Util.AppointmentSearchKeyListener;
import com.DiamondRose.Form.Util.UserSearchKeyListener;
import com.DiamondRose.Form.Util.VaccineCenterSearchKeyListener;
import com.DiamondRose.Form.Util.VaccineSearchKeyListener;
import com.DiamondRose.Main;
import com.DiamondRose.User.Citizen;
import com.DiamondRose.User.User;
import com.DiamondRose.Vaccine.Vaccine;
import com.DiamondRose.Vaccine.VaccineCenter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

final public class SessionMainMenuForm extends JFrame{

	public JPanel panel;
	private JLabel motd;
	private JButton logoutButton;
	private JLabel moreDetails;
	private JTextField addVaccineToVaccineListField;
	private JList<String> vaccineList;
	private JButton vaccineAddButton;
	private JButton guidedAddCenterButton;
	private JList<String> centerList;
	private JButton addAppointmentButton;
	private JButton centerRestockButton;
	private JButton vaccineEditButton;
	private JButton vaccineDeleteButton;
	private JTabbedPane tabbedPane;
	private JRadioButton byNeedleRadioButton;
	private JRadioButton byPrefixRadioButton;
	private JTable allAppointmentsTable;
	private JTextField appointmentsTabSearchTextField;
	private JComboBox<String> appointmentsTabSearchComboBox;
	private JButton settingsChangePictureButton;
	private JTextField settingsNewEmailTextField;
	private JLabel settingsPicutureJLabel;
	private JTable allPeopleTable;
	private JTextField personsTabSearchTextField;
	private JComboBox<String> personsTabSearchComboBox;
	private JLabel noAppointmentsNotice;
	private JButton centerDeleteButton;
	private JPasswordField settingsNewPasswordTextField;
	private JComboBox<String> stockVaccineJComboBox;
	private JSpinner stockVaccineJSpinner;
	private JButton stockVaccineProceedButton;
	private JLabel stockVaccineStatus;
	private JButton centerEditButton;
	private JTextField centerSearchTextField;
	private JRadioButton stockAddToRadioButton;
	private JRadioButton stockRemoveFromRadioButton;
	private JButton settingsUpdatePasswordButton;
	private JButton settingsUpdateEmailButton;
	private JRadioButton centerSearchByPrefixRadioButton;
	private JRadioButton centerSearchByNeedleRadioButton;
	private JButton settingsDeletePictureButton;
	private JButton centerInventoryButton;

	private final User user;
	private final ArrayList<Vaccine> vaccineTabIndexedList = new ArrayList<>();
	private final ArrayList<VaccineCenter> vaccineCenterTabIndexedList = new ArrayList<>();
	private final ArrayList<User> peopleTabIndexedList = new ArrayList<>();
	private final ArrayList<Appointment> appointmentTabIndexedList = new ArrayList<>();

	public SessionMainMenuForm(JFrame mainFrame, User user) {
		this.add(this.panel);
		this.user = user;
		mainFrame.setTitle("Vaccination Manager");
		mainFrame.setSize(this.getPreferredSize());
		this.updateMotd();

		this.logoutButton.setIcon(new ImageIcon(new ImageIcon("resources/button/logout.png").getImage().getScaledInstance(16, 16,  Image.SCALE_SMOOTH)));
		this.logoutButton.addActionListener(e -> {
			mainFrame.setContentPane(new LoginForm(mainFrame).panel);
			mainFrame.revalidate();
		});

		this.updateTab(this.tabbedPane.getTitleAt(this.tabbedPane.getSelectedIndex()));
		this.tabbedPane.addChangeListener(l -> this.updateTab(this.tabbedPane.getTitleAt(this.tabbedPane.getSelectedIndex())));

		this.initHomeTab();
		this.initPeopleTab();
		this.initAppointmentsTab();
		this.initVaccinesTab();
		this.initCentersTab();
		this.initStockTab();
		this.initSettingsTab();
	}

	private void initHomeTab() {
		if(!user.isEmployee()) {
			String[] disabledTabs = new String[]{"People", "Vaccines", "Centers", "Stock"};
			for(String disabledTab : disabledTabs){
				this.tabbedPane.removeTabAt(this.tabbedPane.indexOfTab(disabledTab));
			}
		}
	}

	private void initPeopleTab(){
		this.personsTabSearchComboBox.setSelectedItem("Name");
		this.personsTabSearchTextField.addKeyListener(new UserSearchKeyListener(this, this.personsTabSearchTextField, this.personsTabSearchComboBox));
		this.allPeopleTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			if(e.getClickCount() == 2){
				PersonEditDialog dialog = new PersonEditDialog(peopleTabIndexedList.get(allPeopleTable.getSelectedRow()));
				dialog.pack();
				dialog.setLocationRelativeTo(null);
				dialog.setVisible(true);
				dialog.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosed(WindowEvent e) {
						updatePeopleTab();
						updateMotd();
					}
				});
			}
			}
		});
	}

	private void initAppointmentsTab(){
		this.addAppointmentButton.addActionListener(e -> {
			AppointmentCreatorDialog dialog = new AppointmentCreatorDialog(this.user);
			dialog.pack();
			dialog.setLocationRelativeTo(null);
			dialog.setVisible(true);

			dialog.addWindowListener(new WindowAdapter(){
				@Override
				public void windowClosed(WindowEvent e){
					updateAppointmentsTab();
				}
			});
		});

		this.appointmentsTabSearchTextField.addKeyListener(new AppointmentSearchKeyListener(this, this.appointmentsTabSearchTextField, this.appointmentsTabSearchComboBox));

		this.allAppointmentsTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Appointment appointment = appointmentTabIndexedList.get(allAppointmentsTable.getSelectedRow());
				if(e.getClickCount() == 2 && (user.isEmployee() || !appointment.isExpired())){
					AppointmentEditDialog dialog = new AppointmentEditDialog(user, appointment);
					dialog.pack();
					dialog.setLocationRelativeTo(null);
					dialog.setVisible(true);
					dialog.addWindowListener(new WindowAdapter() {
						@Override
						public void windowClosed(WindowEvent e) {
							updateAppointmentsTab();
						}
					});
				}
			}
		});
	}

	private void initVaccinesTab(){
		this.vaccineAddButton.addActionListener(e -> {
			VaccineCreatorDialog dialog = new VaccineCreatorDialog();
			dialog.pack();
			dialog.setLocationRelativeTo(null);
			dialog.setVisible(true);
			dialog.addWindowListener(new WindowAdapter(){
				@Override
				public void windowClosed(WindowEvent e){
					updateVaccinesTab();
				}
			});
		});

		this.vaccineEditButton.addActionListener(e -> {
			Vaccine vaccine;
			try{
				vaccine = this.vaccineTabIndexedList.get(this.vaccineList.getSelectedIndex());
			}catch(IndexOutOfBoundsException ex){
				JOptionPane.showMessageDialog(this, "Please select a Vaccine from the list first.", "Information", JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			VaccineEditDialog dialog = new VaccineEditDialog(vaccine);
			dialog.pack();
			dialog.setLocationRelativeTo(null);
			dialog.setVisible(true);
			dialog.addWindowListener(new WindowAdapter(){
				@Override
				public void windowClosed(WindowEvent e){
					updateVaccinesTab();
				}
			});
		});

		this.vaccineDeleteButton.setIcon(new ImageIcon(new ImageIcon("resources/button/trash.png").getImage().getScaledInstance(16, 16,  Image.SCALE_SMOOTH)));
		this.vaccineDeleteButton.addActionListener(e -> {
			Vaccine vaccine;
			try{
				vaccine = this.vaccineTabIndexedList.get(this.vaccineList.getSelectedIndex());
			}catch(IndexOutOfBoundsException ex){
				JOptionPane.showMessageDialog(this, "Please select a Vaccine from the list first.", "Information", JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			if(JOptionPane.showConfirmDialog(this,
				"Are you sure you want to delete the %s Vaccine?\n\n".formatted(vaccine.name) +
					"This operation will also delete the following:\n" +
					"- All appointments associated with this vaccine\n" +
					"- Vaccine center stock related to this vaccine\n" +
					"- Distributable stock related to this vaccine"
			) == JOptionPane.YES_OPTION){
				Main.getVaccineManager().delete(vaccine);
				this.updateVaccinesTab();
			}
		});

		this.byNeedleRadioButton.setActionCommand("needle");
		this.byPrefixRadioButton.setActionCommand("prefix");
		ButtonGroup group = new ButtonGroup();
		group.add(this.byNeedleRadioButton);
		group.add(this.byPrefixRadioButton);
		group.setSelected(this.byPrefixRadioButton.getModel(), true);
		this.addVaccineToVaccineListField.addKeyListener(new VaccineSearchKeyListener(this, this.addVaccineToVaccineListField, group));
	}

	private void initCentersTab(){
		this.centerSearchByNeedleRadioButton.setActionCommand("needle");
		this.centerSearchByPrefixRadioButton.setActionCommand("prefix");
		ButtonGroup group = new ButtonGroup();
		group.add(this.centerSearchByNeedleRadioButton);
		group.add(this.centerSearchByPrefixRadioButton);
		group.setSelected(this.centerSearchByPrefixRadioButton.getModel(), true);
		this.centerSearchTextField.addKeyListener(new VaccineCenterSearchKeyListener(this, this.centerSearchTextField, group));

		this.guidedAddCenterButton.addActionListener(e -> {
			CenterCreatorDialog dialog = new CenterCreatorDialog();
			dialog.pack();
			dialog.setLocationRelativeTo(null);
			dialog.setVisible(true);
			dialog.addWindowListener(new WindowAdapter(){
				@Override
				public void windowClosed(WindowEvent e){
					updateCentersTab();
				}
			});
		});
		this.centerDeleteButton.setIcon(new ImageIcon(new ImageIcon("resources/button/trash.png").getImage().getScaledInstance(16, 16,  Image.SCALE_SMOOTH)));
		this.centerDeleteButton.addActionListener(e -> {
			if(!this.centerList.isSelectionEmpty()){
				VaccineCenter center = this.vaccineCenterTabIndexedList.get(this.centerList.getSelectedIndex());
				if(JOptionPane.showConfirmDialog(this, "Are you sure you want to delete the %s Vaccination Center?".formatted(center.name)) == JOptionPane.YES_OPTION){
					Main.getVaccineCenterManager().delete(center);
					this.updateCentersTab();
				}
			}else{
				JOptionPane.showMessageDialog(this, "Please select a Vaccine Center from the list first.", "Information", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		this.centerRestockButton.addActionListener(e -> {
			if(!this.centerList.isSelectionEmpty()){
				CenterRestockDialog dialog = new CenterRestockDialog(this.vaccineCenterTabIndexedList.get(this.centerList.getSelectedIndex()));
				dialog.pack();
				dialog.setLocationRelativeTo(null);
				dialog.setVisible(true);
			}else{
				JOptionPane.showMessageDialog(this, "Please select a Vaccine Center from the list first.", "Information", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		this.centerEditButton.addActionListener(e -> {
			if(!this.centerList.isSelectionEmpty()){
				CenterEditDialog dialog = new CenterEditDialog(this.vaccineCenterTabIndexedList.get(this.centerList.getSelectedIndex()));
				dialog.pack();
				dialog.setLocationRelativeTo(null);
				dialog.setVisible(true);
				dialog.addWindowListener(new WindowAdapter(){
					@Override
					public void windowClosed(WindowEvent e){
						updateCentersTab();
					}
				});
			}else{
				JOptionPane.showMessageDialog(this, "Please select a Vaccine Center from the list first.", "Information", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		this.centerInventoryButton.addActionListener(e -> {
			if(!this.centerList.isSelectionEmpty()){
				CenterViewInventoryDialog dialog = new CenterViewInventoryDialog(this.vaccineCenterTabIndexedList.get(this.centerList.getSelectedIndex()));
				dialog.pack();
				dialog.setLocationRelativeTo(null);
				dialog.setVisible(true);
			}else{
				JOptionPane.showMessageDialog(this, "Please select a Vaccine Center from the list first.", "Information", JOptionPane.INFORMATION_MESSAGE);
			}
		});
	}

	private void initStockTab(){
		this.stockAddToRadioButton.setActionCommand("add");
		this.stockRemoveFromRadioButton.setActionCommand("remove");
		ButtonGroup stockRadioButtonGroup = new ButtonGroup();
		stockRadioButtonGroup.add(this.stockAddToRadioButton);
		stockRadioButtonGroup.add(this.stockRemoveFromRadioButton);
		stockRadioButtonGroup.setSelected(this.stockAddToRadioButton.getModel(), true);
		stockRadioButtonGroup.getElements().asIterator().forEachRemaining(button -> this.updateStockTabStatus());
		this.stockVaccineJSpinner.addChangeListener(e -> this.updateStockTabStatus());
		this.stockVaccineJComboBox.addActionListener(e -> this.updateStockTabStatus());
		this.stockVaccineProceedButton.addActionListener(e -> {
			Vaccine vaccine = Main.getVaccineManager().get(this.stockVaccineJComboBox.getItemAt(this.stockVaccineJComboBox.getSelectedIndex()));
			if(vaccine == null){
				return;
			}

			int quantity = Integer.parseInt(this.stockVaccineJSpinner.getValue() + "");
			if(quantity <= 0){
				JOptionPane.showMessageDialog(this, "Quantity must be greater than zero", "Failed to restock vaccine", JOptionPane.ERROR_MESSAGE);
				return;
			}

			int currentQuantity = Main.getStockManager().getDistributableStock(vaccine);
			switch(stockRadioButtonGroup.getSelection().getActionCommand()){
				case "add":
					Main.getStockManager().setDistributableStock(vaccine, currentQuantity + quantity);
					JOptionPane.showMessageDialog(this, "Successfully added " + quantity + " doses of " + vaccine.name + " vaccine to stock!", "Success", JOptionPane.INFORMATION_MESSAGE);
					break;
				case "remove":
					int newQuantity = currentQuantity - quantity;
					if(newQuantity < 0){
						JOptionPane.showMessageDialog(this, "Cannot deduct more than " + currentQuantity + " doses from " + vaccine.name + " vaccine.",  "Failed to restock vaccine", JOptionPane.ERROR_MESSAGE);
						return;
					}
					Main.getStockManager().setDistributableStock(vaccine, newQuantity);
					JOptionPane.showMessageDialog(this, "Successfully removed " + quantity + " doses of " + vaccine.name + " vaccine from stock!", "Success", JOptionPane.INFORMATION_MESSAGE);
					break;
			}

			this.updateStockTabStatus();
		});
	}

	private void initSettingsTab(){
		this.settingsChangePictureButton.addActionListener(e -> {
			PersonChangePictureDialog dialog = new PersonChangePictureDialog(user);
			dialog.pack();
			dialog.setLocationRelativeTo(null);
			dialog.setVisible(true);
			dialog.addWindowListener(new WindowAdapter(){
				@Override
				public void windowClosed(WindowEvent e){
					updateSettingsTab();
				}
			});
		});

		this.settingsDeletePictureButton.setIcon(new ImageIcon(new ImageIcon("resources/button/trash.png").getImage().getScaledInstance(16, 16,  Image.SCALE_SMOOTH)));

		this.settingsUpdateEmailButton.addActionListener(e -> {
			String newValue = this.settingsNewEmailTextField.getText();
			if(newValue.isBlank() || newValue.equals(user.email)){
				return;
			}

			user.email = newValue;
			Main.getUserManager().saveToFile();
			JOptionPane.showMessageDialog(this, "Your e-mail address has been successfully updated!", "Success", JOptionPane.INFORMATION_MESSAGE);
		});

		this.settingsUpdatePasswordButton.addActionListener(e -> {
			String newValue = new String(this.settingsNewPasswordTextField.getPassword());
			if(newValue.isBlank()){
				return;
			}

			this.settingsNewPasswordTextField.setText("");
			user.password = newValue;
			Main.getUserManager().saveToFile();
			JOptionPane.showMessageDialog(this, "Your password has been successfully updated!", "Success", JOptionPane.INFORMATION_MESSAGE);
		});
	}

	private void updateMotd(){
		this.motd.setText("Logged in as " + user.firstName + " " + user.lastName);
	}

	private void updateTab(String tab){
		switch (tab) {
			case "Home" -> this.updateHomeTab();
			case "People" -> this.updatePeopleTab();
			case "Appointments" -> this.updateAppointmentsTab();
			case "Vaccines" -> this.updateVaccinesTab();
			case "Centers" -> this.updateCentersTab();
			case "Stock" -> this.updateStockTab();
			case "Settings" -> this.updateSettingsTab();
		}
	}

	private void updatePeopleTab(){
		this.updatePeopleTab(Main.getUserManager().getAll());
	}

	public void updatePeopleTab(Collection<User> users){
		DefaultTableModel model = new DefaultTableModel(){
			@Override
			public boolean isCellEditable(int row, int column){
				return false;
			}
		};
		model.addColumn("Type");
		model.addColumn("ID");
		model.addColumn("Full Name");
		model.addColumn("Username");
		model.addColumn("E-mail Address");
		model.addColumn("Date of Birth");
		model.addColumn("Gender");
		model.addColumn("Address");
		this.peopleTabIndexedList.clear();
		for(User user : users){
			model.addRow(new Object[]{
				user.getType(),
				user.id.toString(),
				user.getFullName(),
				user.username,
				user.email,
				user.dob.format(DateTimeFormatter.ISO_LOCAL_DATE),
				user.gender.toString(),
				user.address.simplified()
			});
			this.peopleTabIndexedList.add(user);
		}
		this.allPeopleTable.setModel(model);
	}

	public Collection<Appointment> getViewableAppointments(){
		return this.user.isEmployee() ? Main.getAppointmentManager().getAllAppointments() : Main.getAppointmentManager().getUserStatus(this.user).getAll();
	}

	private void updateAppointmentsTab(){
		this.updateAppointmentsTab(this.getViewableAppointments());
	}

	public void updateAppointmentsTab(Collection<Appointment> list){
		this.addAppointmentButton.setEnabled(Main.getVaccineManager().getAll().size() != 0 && Main.getVaccineCenterManager().getAll().size() != 0);

		DefaultTableModel model = new DefaultTableModel(){
			@Override
			public boolean isCellEditable(int row, int column){
				return false;
			}
		};
		model.addColumn("ID");
		model.addColumn("Person");
		model.addColumn("Vaccine");
		model.addColumn("Center");
		model.addColumn("Schedule");
		model.addColumn("Status");

		AppointmentManager manager = Main.getAppointmentManager();
		this.appointmentTabIndexedList.clear();
		for(Appointment appointment : list){
			User person = Main.getUserManager().getById(appointment.personId);
			model.addRow(new Object[]{
				appointment.id,
				"<html><p color='%s'>%s</p></html>".formatted(manager.getUserStatus(person).isFullyVaccinated() ? "green" : "black", person.getFullName()),
				Main.getVaccineManager().get(appointment.vaccineId).name,
				Main.getVaccineCenterManager().get(appointment.centerId).name,
				appointment.date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a")),
				"<html><p color='%s'>%s</p></html>".formatted(switch(appointment.getStatus()){
					case PENDING -> "blue";
					case COMPLETE -> "green";
					case CANCELLED -> "orange";
					case FAILED -> "red";
				}, appointment.getStatus().name())
			});
			this.appointmentTabIndexedList.add(appointment);
		}

		if(list.isEmpty()){
			this.noAppointmentsNotice.setVisible(true);
			this.allAppointmentsTable.setModel(new DefaultTableModel());
		}else{
			this.noAppointmentsNotice.setVisible(false);
			this.allAppointmentsTable.setModel(model);
		}
	}

	private void updateHomeTab(){
		this.moreDetails.removeAll();
		this.moreDetails.setIcon(new ImageIcon(user.getIcon().getImage().getScaledInstance(128, 128,  Image.SCALE_SMOOTH)));

		StringJoiner status = new StringJoiner("", "<html><body>", "</body></html>")
			.add("<h2 style='margin-top: 0'>" + user.lastName + ", " + user.firstName + "</h2>")
			.add("<p>Account Type: " + user.getType() + "</p>")
			.add("<p>Gender: " + user.gender + " </p>")
			.add("<p>Age: " + LocalDate.now().minusYears(user.dob.getYear()).getYear() + " years</p>")
			.add("<p>E-mail: " + user.email + "</p>");

		if(!this.user.isEmployee()){
			AppointmentUserStatus userStatus = Main.getAppointmentManager().getUserStatus(this.user);
			Collection<Vaccine> fullyVaccinatedBy = userStatus.getFullyVaccinatedBy().keySet();
			if(fullyVaccinatedBy.isEmpty()){
				Collection<Appointment> pendingAppointments = userStatus.getPending();
				if(pendingAppointments.isEmpty()){
					if(userStatus.getCompleted().isEmpty()){
						status.add("<p color='red' style='font-weight: bold'>You are not vaccinated.</p>");
						status.add("<p color='red'>Head over to the Appointments tab and book an appointment.</p>");
					}else{
						status.add("<p color='orange' style='font-weight: bold'>You are partially vaccinated.</p>");
						status.add("<p color='orange'>Head over to the Appointments tab and book an appointment.</p>");
					}
				}else{
					status.add("<p color='orange' style='font-weight: bold'>You are not fully vaccinated.</p>");
					status.add("<p color='orange'>Your appointment is on %s.</p>".formatted(pendingAppointments.stream().min(Comparator.comparing(appointment -> appointment.date)).get().date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a"))));
				}
			}else{
				status.add("<p color='green' style='font-weight: bold'>You are fully vaccinated, congratulations!</p>");
				status.add("<p color='green' style='font-style: italic'>(vaccinated by %s)</p>".formatted(String.join(", ", fullyVaccinatedBy.stream().map(vaccine -> vaccine.name).toList())));
			}
		}

		this.moreDetails.setText(status.toString());
	}

	private void updateVaccinesTab(){
		this.updateVaccinesTab(Main.getVaccineManager().getAll());
	}

	public void updateVaccinesTab(Collection<Vaccine> list){
		this.vaccineTabIndexedList.clear();
		DefaultListModel<String> listModel = new DefaultListModel<>();
		int i = 0;
		for(Vaccine vaccine : list) {
			listModel.addElement("<html><body><b>%d</b>. %s (#%d)</body></html>".formatted(++i, vaccine.name, vaccine.id));
			this.vaccineTabIndexedList.add(vaccine);
		}
		vaccineList.setModel(listModel);
	}

	private void updateCentersTab(){
		this.updateCentersTab(Main.getVaccineCenterManager().getAll());
	}

	public void updateCentersTab(Collection<VaccineCenter> list){
		DefaultListModel<String> listModel = new DefaultListModel<>();
		int i = 0;
		this.vaccineCenterTabIndexedList.clear();
		for(VaccineCenter center : list) {
			listModel.addElement("<html><body><b>%d</b>. %s (#%d)</body></html>".formatted(++i, center.name, center.id));
			this.vaccineCenterTabIndexedList.add(center);
		}
		this.centerList.setModel(listModel);
	}

	private void updateStockTab(){
		this.stockVaccineJComboBox.removeAllItems();
		for(Vaccine vaccine : Main.getVaccineManager().getAll()){
			this.stockVaccineJComboBox.addItem(vaccine.name);
		}

		this.updateStockTabStatus();
	}

	private void updateStockTabStatus(){
		Vaccine vaccine = Main.getVaccineManager().get(this.stockVaccineJComboBox.getItemAt(this.stockVaccineJComboBox.getSelectedIndex()));
		if(vaccine != null){
			this.stockVaccineStatus.setText(
				"There are currently " + Main.getStockManager().getDistributableStock(vaccine) + " dose(s) of " + vaccine.name + " vaccine in stock."
			);
		}
	}

	private void updateSettingsTab(){
		this.settingsNewEmailTextField.setText(user.email);
		this.settingsPicutureJLabel.setIcon(new ImageIcon(user.getIcon().getImage().getScaledInstance(128, 128,  Image.SCALE_SMOOTH)));
		this.settingsPicutureJLabel.setText("");

		if(new File(this.user.getCustomIconPath()).exists()){
			this.settingsDeletePictureButton.addActionListener(e -> {
				try{
					Files.delete(Path.of(user.getCustomIconPath()));
				}catch(IOException ignored){
				}
				this.updateSettingsTab();
			});
			this.settingsDeletePictureButton.setVisible(true);
		}else{
			this.settingsDeletePictureButton.setVisible(false);
		}
	}
}
