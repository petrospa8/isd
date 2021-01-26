package es.udc.ws.bikeModel.client.admin.service.dto;

import java.util.Calendar;

public class ClientBikeModelDto {

	private Long bikeModelId;
	private String name;
	private String description;
	private Calendar rentableFrom;
	private int totalUnits;
	private float pricePerDay;
	private int timesRated;
	private float avgScore;

	public ClientBikeModelDto() {

	}

	public ClientBikeModelDto(Long bikeModelId, String name, String description, Calendar rentableFrom, int totalUnits,
			float pricePerDay) {

		this.bikeModelId = bikeModelId;
		this.name = name;
		this.description = description;
		this.rentableFrom = rentableFrom;
		this.totalUnits = totalUnits;
		this.pricePerDay = pricePerDay;

	}

	public ClientBikeModelDto(Long bikeModelId, String name, String description, Calendar rentableFrom, int totalUnits,
			float pricePerDay, int timesRated, float avgScore) {
		this(bikeModelId, name, description, rentableFrom, totalUnits, pricePerDay);
		this.timesRated = timesRated;
		this.avgScore = avgScore;

	}

	public Long getBikeModelId() {
		return bikeModelId;
	}

	public void setBikeModelId(Long bikeModelId) {
		this.bikeModelId = bikeModelId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public int getTotalUnits() {
		return totalUnits;
	}

	public void setTotalUnits(int totalUnits) {
		this.totalUnits = totalUnits;
	}

	public float getPricePerDay() {
		return pricePerDay;
	}

	public void setPricePerDay(float pricePerDay) {
		this.pricePerDay = pricePerDay;
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
		return "BikeModelDto [bikeModelId=" + bikeModelId + ", name=" + name + ", description=" + description
				+ ", rentableFrom= " + rentableFrom + ", totalUnits = " + totalUnits + ", pricePerDay= " + pricePerDay
				+ ", timesRated=" + timesRated + ", avgScore= " + avgScore + "]";
	}
}
