package es.udc.ws.bikeModel.client.user.service.dto;

import java.util.Calendar;

public class ClientReservationDto {

	private Long rentalId;
	private Long bikeModelId;
	private Calendar startRental;
	private Calendar endRental;
	private int bikesToRent;
	private float score;

	public ClientReservationDto() {
	}

	public ClientReservationDto(Long rentalId, Long bikeModelId, Calendar startRental, Calendar endRental,
			int bikesToRent, float score) {

		this.rentalId = rentalId;
		this.bikeModelId = bikeModelId;
		this.startRental = startRental;
		this.endRental = endRental;
		this.bikesToRent = bikesToRent;
		this.score = score;
	}

	public Long getRentalId() {
		return this.rentalId;
	}

	public void setRentalId(Long rentalId) {
		this.rentalId = rentalId;
	}

	public Long getBikeModelId() {
		return this.bikeModelId;
	}

	public void setBikeModelId(Long bikeModelId) {
		this.bikeModelId = bikeModelId;
	}

	public Calendar getStartRental() {
		return this.startRental;
	}

	public void setStartRental(Calendar startRental) {
		this.startRental = startRental;
	}

	public Calendar getEndRental() {
		return this.endRental;
	}

	public void setEndRental(Calendar endRental) {
		this.endRental = endRental;
	}

	public int getBikesToRent() {
		return this.bikesToRent;
	}

	public void setBikesToRent(int number) {
		this.bikesToRent = number;
	}

	public float getScore() {
		return this.score;
	}

	public void setScore(float score) {
		this.score = score;
	}

}
