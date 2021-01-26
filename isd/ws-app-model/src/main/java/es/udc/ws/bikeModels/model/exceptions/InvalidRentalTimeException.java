package es.udc.ws.bikeModels.model.exceptions;

import java.util.Calendar;

@SuppressWarnings("serial")
public class InvalidRentalTimeException extends Exception {

	private Calendar startRental;
	private Calendar endRental;

	public InvalidRentalTimeException(Calendar startRental, Calendar endRental) {

		super("The end rental date must be after the start rental date and must not exceed a 15-day timespan."
				+ " Start rental date: " + startRental.getTime() + " | " + "End rental date: " + endRental.getTime()
				+ ".");
		this.startRental = startRental;
		this.endRental = endRental;
	}

	public Calendar getStartRental() {
		return startRental;
	}

	public Calendar getEndRental() {
		return endRental;
	}
}
