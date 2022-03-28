package com.DiamondRose.Form;

import com.DiamondRose.Appointment.Appointment;
import com.DiamondRose.Appointment.AppointmentStatus;
import com.DiamondRose.Appointment.AppointmentUserStatus;
import com.DiamondRose.Main;
import com.DiamondRose.User.People;
import com.DiamondRose.User.User;
import com.DiamondRose.Vaccine.Vaccine;
import com.DiamondRose.Vaccine.VaccineCenter;
import com.DiamondRose.Vaccine.VaccineCenterManager;
import com.github.lgooddatepicker.components.CalendarPanel;
import com.github.lgooddatepicker.optionalusertools.CalendarListener;
import com.github.lgooddatepicker.zinternaltools.CalendarSelectionEvent;
import com.github.lgooddatepicker.zinternaltools.YearMonthChangeEvent;

import javax.swing.*;
import javax.swing.Timer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

final public class AppointmentCreatorDialog extends JDialog {

    public static ArrayList<LocalTime> generateHourComboBox(JComboBox<String> hourComboBox, LocalDate selectedDate, Appointment appointment){
        HashSet<String> reservedAppointments = new HashSet<>(); // "0000" "1800" "2300" "2330"
        for(Appointment appointmentInMap : Main.getAppointmentManager().getAllAppointments()){
            if(appointmentInMap.date.toLocalDate().equals(selectedDate)){ // we have an appointment during the selected date
                reservedAppointments.add(String.format("%02d%02d", appointmentInMap.date.getHour(), appointmentInMap.date.getMinute()));
            }
        }

        if(appointment != null && selectedDate.equals(appointment.date.toLocalDate())){
            reservedAppointments.remove(String.format("%02d%02d", appointment.date.getHour(), appointment.date.getMinute()));
        }

        ArrayList<LocalTime> localTimes = new ArrayList<>();
        for(int i = 9; i < 24; i++) {
            boolean oclockAvailable = !reservedAppointments.contains(String.format("%02d%02d", i, 0));
            boolean halfPastAvailable = !reservedAppointments.contains(String.format("%02d%02d", i, 30));
            if(oclockAvailable){
                LocalTime time = LocalTime.of(i, 0);
                hourComboBox.addItem(time.format(DateTimeFormatter.ofPattern("hh:mm a")));
                localTimes.add(time);
            }
            if(halfPastAvailable){
                LocalTime time = LocalTime.of(i, 30);
                hourComboBox.addItem(time.format(DateTimeFormatter.ofPattern("hh:mm a")));
                localTimes.add(time);
            }
        }
        return localTimes;
    }

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox<String> centerComboBox;
    private JComboBox<String> vaccineComboBox;
    private JComboBox<String> hourComboBox;
    private CalendarPanel calendarDate;
    private JProgressBar loadingProgressBar;
    private JTextField personNameTextField;
    private ArrayList<Vaccine> listedVaccines;
    private LocalTime selectedLocalTime;

    private final User user;

    public AppointmentCreatorDialog(User user){
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        this.setTitle("New Vaccine Appointment Booking");
        this.user = user;

        for(VaccineCenter center : Main.getVaccineCenterManager().getAll()){
            this.centerComboBox.addItem(center.name);
        }

        if(!user.isEmployee()){
            this.personNameTextField.setText(user.getFullName());
            this.personNameTextField.setEditable(false);
        }

        Runnable onCenterComboBoxChange = () -> {
            VaccineCenter chosenVCenter = Main.getVaccineCenterManager().get(centerComboBox.getItemAt(centerComboBox.getSelectedIndex()));//.split(":")[0].trim()); //TODO-remove: this split and trim do nothing, the only thing in centerComboBox is the name
            if(chosenVCenter != null){
                this.listedVaccines = VaccineCenterManager.addStockToComboBox(vaccineComboBox, chosenVCenter);
            }else{ //which should never happen
                JOptionPane.showMessageDialog(this, "The vaccine center selected was not found. Please contact developers.", "Development issue", JOptionPane.ERROR_MESSAGE);
            }
            vaccineComboBox.setEnabled(true);
        };
        onCenterComboBoxChange.run();
        centerComboBox.addActionListener(event -> onCenterComboBoxChange.run());

        ArrayList<LocalTime> localTimes = new ArrayList<>();
        this.calendarDate.getSettings().setDateRangeLimits(LocalDate.now(), LocalDate.now().plusMonths(6));
        this.hourComboBox.setEnabled(false);
        this.calendarDate.addCalendarListener(new CalendarListener(){
            @Override
            public void selectedDateChanged(CalendarSelectionEvent calendarSelectionEvent){
                hourComboBox.removeAllItems();
                LocalDate selectedDate = calendarDate.getSelectedDate();
                if(selectedDate != null){
                    localTimes.addAll(AppointmentCreatorDialog.generateHourComboBox(hourComboBox, selectedDate, null));
                    hourComboBox.setEnabled(true);
                }else{
                    hourComboBox.setEnabled(false);
                }
            }

            @Override
            public void yearMonthChanged(YearMonthChangeEvent yearMonthChangeEvent){
            }
        });

        Runnable onHourComboBoxChange = () -> {
            if(this.hourComboBox.getSelectedIndex() > 0){
                this.selectedLocalTime = localTimes.get(this.hourComboBox.getSelectedIndex());
            }else{
                this.selectedLocalTime = null;
            }
        };
        onHourComboBoxChange.run();
        this.hourComboBox.addActionListener(e -> onHourComboBoxChange.run());

        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());
    }

    private void onOK() {
        String[] nameParts = this.personNameTextField.getText().split(" ");
        User person = nameParts.length == 2 ? Main.getUserManager().getByFullName(nameParts[0], nameParts[1]) : null;
        if(person == null){
            JOptionPane.showMessageDialog(this, "No person has been found with the name " + this.personNameTextField.getText(), "Appointment has unmet parameters", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(!(person instanceof People)){
            JOptionPane.showMessageDialog(this, (person == this.user ? "You are" : person.getFullName() + " is") + " a " + person.getType().toLowerCase() + " and hence cannot be appointed vaccinations.", "Appointment has unmet parameters", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean isBookingForSelf = person.id == this.user.id;

        if(this.hourComboBox.getSelectedIndex() <= 0){
            JOptionPane.showMessageDialog(this, "Please select a time-slot for the appointment", "Appointment has unmet parameters", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Vaccine vaccine = this.listedVaccines.get(this.vaccineComboBox.getSelectedIndex());
        LocalDateTime appointmentDate = LocalDateTime.of(this.calendarDate.getSelectedDate(), this.selectedLocalTime);

        AppointmentUserStatus userStatus = Main.getAppointmentManager().getUserStatus(person);
        long dosesAlreadyTaken = userStatus.getCompleted(vaccine).size();
        Appointment pendingAppointment = userStatus.getPending(vaccine).stream().findAny().orElse(null);
        LocalDateTime lastDoseTakenAt = userStatus.getCompleted(vaccine).stream()
            .map(appointment -> appointment.date)
            .max(Comparator.comparing(date -> date))
        .orElse(LocalDateTime.MIN);

        if(pendingAppointment != null){
            JOptionPane.showMessageDialog(this, (isBookingForSelf ? "You already have" : "This person already has") + " a pending appointment on " + pendingAppointment.date + ".", "Appointment cannot be booked", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(dosesAlreadyTaken >= vaccine.dose){
            JOptionPane.showMessageDialog(this, (isBookingForSelf ? "You have" : "This person has") + " already taken " + dosesAlreadyTaken + " dose(s) of " + vaccine.name + " vaccine.", "Appointment cannot be booked", JOptionPane.ERROR_MESSAGE);
            return;
        }

        long daysBetweenLastDoseAndAppointment = lastDoseTakenAt.until(appointmentDate, ChronoUnit.DAYS);
        if(daysBetweenLastDoseAndAppointment < vaccine.daysBetweenDoses){
            JOptionPane.showMessageDialog(this,
                (isBookingForSelf ? "You have" : "This person has") + " already taken a dose of " + vaccine.name + " vaccine " + lastDoseTakenAt.until(LocalDateTime.now(), ChronoUnit.DAYS) + " days ago.\n" +
                    (isBookingForSelf ? "Your" : "Their") + " next appointment can be booked on " + LocalDateTime.now().plusDays(daysBetweenLastDoseAndAppointment).toLocalDate() + ".",
                "Appointment cannot be booked",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        VaccineCenter center = Objects.requireNonNull(Main.getVaccineCenterManager().get(this.centerComboBox.getItemAt(this.centerComboBox.getSelectedIndex())));
        Appointment appointment = new Appointment(Main.getAppointmentManager().getNextAppointmentId(), person.id, vaccine.id, center.id, appointmentDate, AppointmentStatus.PENDING);

        this.setEnabled(false);
        final Timer t = new Timer(50, e -> {
            loadingProgressBar.setValue(loadingProgressBar.getValue() + 1);
            if (loadingProgressBar.getValue() == 100) {
                ((Timer) e.getSource()).stop();

                Main.getAppointmentManager().createNewAppointment(appointment.id, appointment);
                center.setVaccineStock(vaccine, center.getVaccineStock(vaccine) - 1);
                Main.getVaccineCenterManager().saveToFile();

                JOptionPane.showMessageDialog(this, new StringJoiner("", "<html><body>", "</body></html>")
                   .add("<p>Appointment has been created successfully!</p>")
                   .add("<p>Appointment Details:</p>")
                   .add("<p><code><b>%s: </b>%s</code></p>".formatted("Name", person.getFullName()))
                   .add("<p><code><b>%s: </b>%d</code></p>".formatted("ID", appointment.id))
                   .add("<p><code><b>%s: </b>%s</code></p>".formatted("Center", center.name))
                   .add("<p><code><b>%s: </b>%s</code></p>".formatted("Vaccine", vaccine.name))
                   .add("<p><code><b>%s: </b>%s</code></p>".formatted("Date and Time", appointment.date)),
                   "Appointment Booked Successfully", JOptionPane.INFORMATION_MESSAGE);

                dispose();
            }
        });
        t.start();

    }

    private void onCancel() {
        dispose();
    }
}

