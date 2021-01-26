package es.udc.ws.bikeModels.model.exceptions;

import java.util.Calendar;

@SuppressWarnings("serial")
public class InvalidRatingDateException extends Exception {
	private Calendar ratingDate;
	private Calendar endRental;

	public InvalidRatingDateException(Calendar ratingDate, Calendar endRental) {

		super("An unfinished reservation cannot be rated. The rating (" + ratingDate.getTime()
				+ ") must happen after the end of the rental (" + endRental.getTime() + ")");

		this.ratingDate = ratingDate;
		this.endRental = endRental;
	}

	public Calendar getRatingDate() {
		return ratingDate;
	}

	public Calendar getEndRental() {
		return endRental;
	}
}
