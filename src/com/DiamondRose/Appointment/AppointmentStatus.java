package com.DiamondRose.Appointment;

public enum AppointmentStatus{

	/**
	 * Caused due to manually cancelling an appointment.
	 */
	CANCELLED("Cancelled"),

	/**
	 * Caused when a user does not show up for their appointment,
	 * i.e., {@see Appointment.date} is past current date.
	 */
	FAILED("Failed"),

	/**
	 * Depicts an appointment is progressing fine and is waiting to
	 * be taken by the user.
	 */
	PENDING("Pending"),

	/**
	 * Depicts an appointment has been taken by the user.
	 */
	COMPLETE("Complete");

	private final String displayName;

	AppointmentStatus(String displayName){
		this.displayName = displayName;
	}

	@Override
	public String toString(){
		return this.displayName;
	}
}
