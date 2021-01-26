package es.udc.ws.bikeModel.client.admin.service.exceptions;

import java.util.Calendar;

@SuppressWarnings("serial")
public class ClientInvalidNewRentableFromException extends Exception {
	Calendar newRentableFrom;
	Calendar oldRentableFrom;

	public ClientInvalidNewRentableFromException(Calendar newRentableFrom, Calendar oldRentableFrom) {
		super("Invalid new value for RentableFrom. New date was (" + newRentableFrom.getTime()
				+ ") but should be later than (" + oldRentableFrom.getTime() + ").");

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