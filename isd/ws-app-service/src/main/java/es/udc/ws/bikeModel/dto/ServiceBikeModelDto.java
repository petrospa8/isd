package es.udc.ws.bikeModel.dto;

import java.util.Calendar;

public class ServiceBikeModelDto {

	private Long bikeModelId;
	private String name;
	private String description;
	private Calendar rentableFrom;
	private int totalUnits;
	private float pricePerDay;
	private int timesRated;
	private float avgScore;

	public ServiceBikeModelDto() {

	}

	public ServiceBikeModelDto(Long bikeModelId, String name, String description, Calendar rentableFrom, int totalUnits,
			float pricePerDay, int timesRated, float avgScore) {

		this.bikeModelId = bikeModelId;
		this.name = name;
		this.description = description;
		this.rentableFrom = rentableFrom;
		this.totalUnits = totalUnits;
		this.pricePerDay = pricePerDay;
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
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Calendar getRentableFrom() {
		return this.rentableFrom;
	}

	public void setRentableFrom(Calendar date) {
		this.rentableFrom = date;
	}

	public int getTotalUnits() {
		return this.totalUnits;
	}

	public void setTotalUnits(int totalUnits) {
		this.totalUnits = totalUnits;
	}

	public float getPricePerDay() {
		return this.pricePerDay;
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
				+ ", rentableFrom=" + rentableFrom + ", totalUnits=" + totalUnits + ", pricePerDay=" + pricePerDay
				+ ", timesRated=" + timesRated + ", avgScore=" + avgScore + "]";
	}
}
