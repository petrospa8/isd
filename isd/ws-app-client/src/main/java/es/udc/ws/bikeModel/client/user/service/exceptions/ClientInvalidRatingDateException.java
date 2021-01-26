package es.udc.ws.bikeModel.client.user.service.exceptions;

import java.util.Calendar;

@SuppressWarnings("serial")
public class ClientInvalidRatingDateException extends Exception {
	private Calendar ratingDate;
	private Calendar endRental;

	public ClientInvalidRatingDateException(Calendar ratingDate, Calendar endRental) {

		super("An unfinished reservation cannot be rated. The rating (" + ratingDate.getTime()
				+ ") must happen after the end of the rental (" + endRental.getTime() + ")");

		this.ratingDate = ratingDate;
		this.endRental = endRental;
	}

	public Calendar getRatingDate() {
		return ratingDate;
	}

	public Calendar getendRental() {
		return endRental;
	}
}
