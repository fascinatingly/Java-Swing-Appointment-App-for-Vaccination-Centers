package com.DiamondRose.Appointment;

import com.DiamondRose.Main;
import com.DiamondRose.Vaccine.Vaccine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

final public class AppointmentUserStatus{

	private final Collection<Appointment> appointments;

	public AppointmentUserStatus(Collection<Appointment> appointments){
		this.appointments = appointments;
	}

	public Collection<Appointment> getAll(){
		return this.appointments;
	}

	private Collection<Appointment> getAll(Vaccine vaccine){
		return this.appointments.stream().filter(appointment -> appointment.vaccineId == vaccine.id).toList();
	}

	public Collection<Appointment> getPending(){
		return this.getAll().stream().filter(appointment -> appointment.getStatus() == AppointmentStatus.PENDING).toList();
	}

	public Collection<Appointment> getPending(Vaccine vaccine){
		return this.getAll(vaccine).stream().filter(appointment -> appointment.getStatus() == AppointmentStatus.PENDING).toList();
	}

	public Collection<Appointment> getCompleted(){
		return this.getAll().stream().filter(appointment -> appointment.getStatus() == AppointmentStatus.COMPLETE).toList();
	}

	public Collection<Appointment> getCompleted(Vaccine vaccine){
		return this.getAll(vaccine).stream().filter(appointment -> appointment.getStatus() == AppointmentStatus.COMPLETE).toList();
	}

	public Map<Vaccine, Collection<Appointment>> getFullyVaccinatedBy(){
		Map<Vaccine, Collection<Appointment>> result = new ConcurrentHashMap<>();
		for(Appointment appointment : this.getCompleted()){
			result.computeIfAbsent(Main.getVaccineManager().get(appointment.vaccineId), v -> new ArrayList<>()).add(appointment);
		}
		result.forEach((vaccine, appointments) -> {
			if(appointments.size() < vaccine.dose){
				result.remove(vaccine);
			}
		});
		return result;
	}

	public boolean isFullyVaccinated(){
		return !this.getFullyVaccinatedBy().isEmpty();
	}
}
