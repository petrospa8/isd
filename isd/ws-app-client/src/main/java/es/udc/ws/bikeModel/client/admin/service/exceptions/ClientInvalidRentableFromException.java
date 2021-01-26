package es.udc.ws.bikeModel.client.admin.service.exceptions;

import java.util.Calendar;

@SuppressWarnings("serial")
public class ClientInvalidRentableFromException extends Exception {

	private Calendar rentableFrom;

	public ClientInvalidRentableFromException(Calendar rentableFrom) {

		super("The date from which a bike model" + " is available to rent (" + rentableFrom.getTime() + ")"
				+ " must be after the current date.");

		this.rentableFrom = rentableFrom;
	}

	public Calendar getRentableFrom() {
		return rentableFrom;
	}
}
