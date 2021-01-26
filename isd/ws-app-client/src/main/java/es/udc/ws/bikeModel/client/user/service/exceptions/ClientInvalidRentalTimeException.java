package es.udc.ws.bikeModel.client.user.service.exceptions;

import java.util.Calendar;

@SuppressWarnings("serial")
public class ClientInvalidRentalTimeException extends Exception {

	private Calendar startRental;
	private Calendar endRental;

	public ClientInvalidRentalTimeException(Calendar startRental, Calendar endRental) {

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
