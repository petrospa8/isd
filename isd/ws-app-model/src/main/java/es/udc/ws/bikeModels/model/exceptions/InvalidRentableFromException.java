package es.udc.ws.bikeModels.model.exceptions;

import java.util.Calendar;

@SuppressWarnings("serial")
public class InvalidRentableFromException extends Exception {

	private Calendar rentableFrom;

	public InvalidRentableFromException(Calendar rentableFrom) {

		super("The date from which a bike model" + " is available to rent (" + rentableFrom.getTime() + ")"
				+ " must be after the current date.");

		this.rentableFrom = rentableFrom;
	}

	public Calendar getRentableFrom() {
		return rentableFrom;
	}
}
