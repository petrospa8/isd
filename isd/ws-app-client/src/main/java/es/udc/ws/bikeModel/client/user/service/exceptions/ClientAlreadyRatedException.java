package es.udc.ws.bikeModel.client.user.service.exceptions;

@SuppressWarnings("serial")
public class ClientAlreadyRatedException extends Exception {

	private Long rentalId;

	public ClientAlreadyRatedException(Long rentalId) {
		super("rentalId " + rentalId + " has" + " already been rated and cannot be rated again.");
		this.rentalId = rentalId;
	}

	public Long getRentalId() {
		return this.rentalId;
	}

}
