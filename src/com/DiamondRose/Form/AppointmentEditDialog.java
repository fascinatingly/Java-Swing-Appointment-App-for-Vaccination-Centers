package com.DiamondRose.Form;

import com.DiamondRose.Appointment.Appointment;
import com.DiamondRose.Appointment.AppointmentStatus;
import com.DiamondRose.Main;
import com.DiamondRose.User.User;
import com.github.lgooddatepicker.components.DatePicker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

final public class AppointmentEditDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonBack;
    private JButton deleteAppointmentButton;
    private JComboBox<AppointmentStatus> statusComboBox;
    private JComboBox<String> timeSlotComboBox;
    private DatePicker dateComboBox;
    private JButton saveChangesButton;
    private JPanel statusPanel;

    public AppointmentEditDialog(User user, Appointment appointment) {
        setContentPane(contentPane);
        setModal(true);

        buttonBack.addActionListener(e -> onBack());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onBack();
            }
        });

        contentPane.registerKeyboardAction(e -> onBack(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        this.setTitle("Appointment #" + appointment.id);

        for(AppointmentStatus status : AppointmentStatus.values()){
            this.statusComboBox.addItem(status);
        }

        this.statusComboBox.setSelectedItem(appointment.getStatus());
        this.dateComboBox.setDate(appointment.date.toLocalDate());
        this.timeSlotComboBox.setSelectedItem(appointment.date.toLocalTime().format(DateTimeFormatter.ofPattern("hh:mm a")));

        this.deleteAppointmentButton.setIcon(new ImageIcon(new ImageIcon("resources/button/trash.png").getImage().getScaledInstance(16, 16,  Image.SCALE_SMOOTH)));

        ArrayList<LocalTime> localTimes = AppointmentCreatorDialog.generateHourComboBox(this.timeSlotComboBox, this.dateComboBox.getDate(), appointment);
        this.dateComboBox.getSettings().setDateRangeLimits(LocalDate.now(), LocalDate.now().plusMonths(6));
        Runnable onDateChange = () -> {
            timeSlotComboBox.removeAllItems();
            localTimes.addAll(AppointmentCreatorDialog.generateHourComboBox(timeSlotComboBox, dateComboBox.getDate(), appointment));
        };
        this.dateComboBox.addDateChangeListener(dateChangeEvent -> onDateChange.run());
        onDateChange.run();

        this.saveChangesButton.addActionListener(e -> {
            appointment.date = LocalDateTime.of(this.dateComboBox.getDate(), localTimes.get(timeSlotComboBox.getSelectedIndex()));
            appointment.setStatus(this.statusComboBox.getItemAt(this.statusComboBox.getSelectedIndex()));
            Main.getAppointmentManager().saveToFile();
            this.onBack();
        });

        if(!user.isEmployee()){
            this.statusPanel.setVisible(false);
            if(appointment.getStatus() == AppointmentStatus.PENDING){
                this.deleteAppointmentButton.setText("Cancel Appointment");
                this.deleteAppointmentButton.addActionListener(e -> {
                    appointment.setStatus(AppointmentStatus.CANCELLED);
                    Main.getAppointmentManager().saveToFile();
                    this.onBack();
                });
            }else{
                this.saveChangesButton.setVisible(false);
                this.deleteAppointmentButton.setVisible(false);
                this.dateComboBox.setEnabled(false);
                this.timeSlotComboBox.setEnabled(false);
            }
        }else{
            this.deleteAppointmentButton.addActionListener(e -> {
                Main.getAppointmentManager().delete(appointment);
                this.onBack();
            });
        }

        if(appointment.isExpired()){
            this.statusComboBox.removeItem(AppointmentStatus.PENDING);
        }
    }

    private void onBack() {
        dispose();
    }
}
