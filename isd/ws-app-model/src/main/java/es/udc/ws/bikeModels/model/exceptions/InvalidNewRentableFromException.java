package es.udc.ws.bikeModels.model.exceptions;

import java.util.Calendar;

@SuppressWarnings("serial")
public class InvalidNewRentableFromException extends Exception {
	Calendar newRentableFrom;
	Calendar oldRentableFrom;
	boolean wasRented;

	public InvalidNewRentableFromException(Calendar newRentableFrom, Calendar oldRentableFrom) {

		super("Invalid new value for RentableFrom. New date was (" + newRentableFrom.getTime()
				+ ") but it cannot be after the current RentableFrom date (" + oldRentableFrom.getTime()
				+ ") while a reservation for that bike model exists.");
		this.newRentableFrom = newRentableFrom;
		this.oldRentableFrom = oldRentableFrom;
	}

	public Calendar getNewRentableFrom() {
		return this.newRentableFrom;
	}

	public Calendar getOldRentableFrom() {
		return this.oldRentableFrom;
	}

}