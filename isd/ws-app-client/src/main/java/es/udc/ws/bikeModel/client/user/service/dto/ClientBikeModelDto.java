package es.udc.ws.bikeModel.client.user.service.dto;

import java.util.Calendar;

public class ClientBikeModelDto {

	private Long bikeModelId;
	private String description;
	private Calendar rentableFrom;
	private int timesRated;
	private float avgScore;

	public ClientBikeModelDto() {

	}

	public ClientBikeModelDto(Long bikeModelId, String description, Calendar rentableFrom) {

		this.bikeModelId = bikeModelId;
		this.description = description;
		this.rentableFrom = rentableFrom;

	}

	public ClientBikeModelDto(Long bikeModelId, String description, Calendar rentableFrom, int timesRated,
			float avgScore) {
		this(bikeModelId, description, rentableFrom);
		this.timesRated = timesRated;
		this.avgScore = avgScore;

	}

	public Long getBikeModelId() {
		return bikeModelId;
	}

	public void setBikeModelId(Long bikeModelId) {
		this.bikeModelId = bikeModelId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Calendar getRentableFrom() {
		return rentableFrom;
	}

	public void setRentableFrom(Calendar rentableFrom) {
		this.rentableFrom = rentableFrom;
	}

	public int getTimesRated() {
		return timesRated;
	}

	public void setTimesRated(int timesRated) {
		this.timesRated = timesRated;
	}

	public float getAvgScore() {
		return avgScore;
	}

	public void setAvgScore(float avgScore) {
		this.avgScore = avgScore;
	}

	@Override
	public String toString() {
		return "BikeModelDto [bikeModelId=" + bikeModelId + ", description=" + description + ", rentableFrom= "
				+ rentableFrom + ", timesRated=" + timesRated + ", avgScore= " + avgScore + "]";
	}
}
