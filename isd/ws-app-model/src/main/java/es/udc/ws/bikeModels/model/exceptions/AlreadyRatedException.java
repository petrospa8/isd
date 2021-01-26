package es.udc.ws.bikeModels.model.exceptions;

@SuppressWarnings("serial")
public class AlreadyRatedException extends Exception {

	private Long rentalId;

	public AlreadyRatedException(Long rentalId) {
		super("rentalId " + rentalId + ") has" + " already been rated and cannot be rated again.");
		this.rentalId = rentalId;
	}

	public Long getRentalId() {
		return this.rentalId;
	}

}
