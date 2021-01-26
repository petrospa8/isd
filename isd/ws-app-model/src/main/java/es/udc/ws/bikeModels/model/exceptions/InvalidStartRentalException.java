package es.udc.ws.bikeModels.model.exceptions;

import java.util.Calendar;

@SuppressWarnings("serial")
public class InvalidStartRentalException extends Exception {

	private Calendar startRental;
	private Calendar rentableFrom;

	public InvalidStartRentalException(Calendar startRental, Calendar rentableFrom) {

		super("That bike is not available yet. The selected date for the start of the rental " + "("
				+ startRental.getTime() + ")" + " must be after the date from which that bike model can be rented ("
				+ rentableFrom.getTime() + ").");

		this.rentableFrom = rentableFrom;
		this.startRental = startRental;
	}

	public Calendar getRentableFrom() {
		return this.rentableFrom;
	}

	public Calendar getStartRental() {
		return this.startRental;
	}
}
