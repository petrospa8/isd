package es.udc.ws.bikeModels.model.bikeModel;

import java.util.Calendar;

public class BikeModel {
	private Long bikeModelId;
	private String name;
	private String description;
	private Calendar rentableFrom;
	private int totalUnits;
	private float pricePerDay;
	private Calendar creationDate;
	private int timesRated;
	private float avgScore;

	public BikeModel(String name, String description, Calendar rentableFrom, int totalUnits, float pricePerDay) {
		this.name = name;
		this.description = description;
		this.rentableFrom = rentableFrom;
		if (rentableFrom != null) {
			this.rentableFrom.set(Calendar.MILLISECOND, 0);
			this.rentableFrom.set(Calendar.SECOND, 0);
			this.rentableFrom.set(Calendar.MINUTE, 0);
			this.rentableFrom.set(Calendar.HOUR_OF_DAY, 0);
		}
		this.totalUnits = totalUnits;
		this.pricePerDay = pricePerDay;
	}

	public BikeModel(Long bikeModelId, String name, String description, Calendar rentableFrom, int totalUnits,
			float pricePerDay) {
		this(name, description, rentableFrom, totalUnits, pricePerDay);
		this.bikeModelId = bikeModelId;
	}

	public BikeModel(Long bikeModelId, String name, String description, Calendar rentableFrom, int totalUnits,
			float pricePerDay, Calendar creationDate) {
		this(bikeModelId, name, description, rentableFrom, totalUnits, pricePerDay);
		this.creationDate = creationDate;
		if (creationDate != null) {
			this.creationDate.set(Calendar.MILLISECOND, 0);
			this.creationDate.set(Calendar.SECOND, 0);
		}
	}

	public Long getBikeModelId() {
		return this.bikeModelId;
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

	public void setBikeModelId(Long bikeModelId) {
		this.bikeModelId = bikeModelId;
	}

	public Calendar getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Calendar date) {
		this.creationDate = date;
		if (creationDate != null) {
			this.creationDate.set(Calendar.MILLISECOND, 0);
		}
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

	public void updateBikeModel(BikeModel bikeModel) {
		this.name = bikeModel.getName();
		this.description = bikeModel.getDescription();
		this.rentableFrom = bikeModel.getRentableFrom();
		this.totalUnits = bikeModel.getTotalUnits();
		this.pricePerDay = bikeModel.getPricePerDay();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bikeModelId == null) ? 0 : bikeModelId.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((rentableFrom == null) ? 0 : rentableFrom.hashCode());
		result = prime * result + totalUnits;
		result = prime * result + ((creationDate == null) ? 0 : creationDate.hashCode());
		result = prime * result + Float.floatToIntBits(pricePerDay);
		result = prime * result + timesRated;
		result = prime * result + Float.floatToIntBits(avgScore);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		BikeModel other = (BikeModel) obj;

		if (bikeModelId == null) {
			if (bikeModelId != other.bikeModelId) {
				return false;
			}
		} else if (!bikeModelId.equals(other.bikeModelId)) {
			return false;
		}

		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}

		if (description == null) {
			if (other.description != null) {
				return false;
			}
		} else if (!description.equals(other.description)) {
			return false;
		}

		if (rentableFrom == null) {
			if (other.rentableFrom != null) {
				return false;
			}
		} else if (!rentableFrom.equals(other.rentableFrom)) {
			return false;
		}

		if (totalUnits != other.totalUnits) {
			return false;
		}

		if (creationDate == null) {
			if (other.creationDate != null) {
				return false;
			}
		} else if (!creationDate.equals(other.creationDate)) {
			return false;
		}

		if (Float.floatToIntBits(pricePerDay) != Float.floatToIntBits(other.pricePerDay)) {
			return false;
		}

		if (timesRated != other.timesRated) {
			return false;
		}

		if (Float.floatToIntBits(avgScore) != Float.floatToIntBits(other.avgScore)) {
			return false;
		}

		return true;
	}

}