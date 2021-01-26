package es.udc.ws.bikeModels.model.reservation;

import java.util.Calendar;

public class Reservation {

	private Long rentalId;
	private final String userEmail;
	private final String creditCard;
	private Calendar startRental;
	private Calendar endRental;
	private int bikesToRent;
	private Long bikeModelId;
	private Calendar creationDate;
	private float score;

	public Reservation(String userEmail, String creditCard, Calendar startRental, Calendar endRental, int bikesToRent,
			Long bikeModelId, Calendar creationDate) {

		this.userEmail = userEmail;
		this.creditCard = creditCard;
		this.startRental = startRental;
		if (startRental != null) {
			this.startRental.set(Calendar.MILLISECOND, 0);
		}
		this.endRental = endRental;
		if (endRental != null) {
			this.endRental.set(Calendar.MILLISECOND, 0);
		}
		this.bikesToRent = bikesToRent;
		this.bikeModelId = bikeModelId;
		this.score = -1;
		this.creationDate = creationDate;
		if (creationDate != null) {
			this.creationDate.set(Calendar.MILLISECOND, 0);
		}
	}

	public Reservation(Long rentalId, String userEmail, String creditCard, Calendar startRental, Calendar endRental,
			int bikesToRent, Long bikeModelId, Calendar creationDate) {
		this(userEmail, creditCard, startRental, endRental, bikesToRent, bikeModelId, creationDate);
		this.rentalId = rentalId;
	}

	// Getters
	public String getUserEmail() {
		return this.userEmail;
	}

	public String getCreditCard() {
		return this.creditCard;
	}

	public Calendar getStartRental() {
		return this.startRental;
	}

	public Calendar getEndRental() {
		return this.endRental;
	}

	public int getBikesToRent() {
		return this.bikesToRent;
	}

	public Long getRentalId() {
		return this.rentalId;
	}

	public Long getBikeModelId() {
		return this.bikeModelId;
	}

	public Calendar getCreationDate() {
		return this.creationDate;
	}

	public float getScore() {
		return this.score;
	}

	// Setters

	public void setStartRental(Calendar date) {
		this.startRental = date;
		if (this.startRental != null) {
			this.startRental.set(Calendar.MILLISECOND, 0);
			this.startRental.set(Calendar.SECOND, 0);
			this.startRental.set(Calendar.MINUTE, 0);
		}
	}

	public void setEndRental(Calendar date) {
		this.endRental = date;
		if (this.endRental != null) {
			this.endRental.set(Calendar.MILLISECOND, 0);
			this.endRental.set(Calendar.SECOND, 0);
			this.endRental.set(Calendar.MINUTE, 0);
		}
	}

	public void setCreationDate(Calendar creationDate) {
		this.creationDate = creationDate;
		if (creationDate != null) {
			this.creationDate.set(Calendar.MILLISECOND, 0);
		}
	}

	public void setBikesToRent(int number) {
		this.bikesToRent = number;
	}

	public void setBikeModelId(Long model) {
		this.bikeModelId = model;
	}

	public void setScore(float score) {
		this.score = score;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((creditCard == null) ? 0 : creditCard.hashCode());
		result = prime * result + ((rentalId == null) ? 0 : rentalId.hashCode());
		result = prime * result + ((userEmail == null) ? 0 : userEmail.hashCode());
		result = prime * result + ((bikeModelId == null) ? 0 : bikeModelId.hashCode());
		result = prime * result + ((startRental == null) ? 0 : startRental.hashCode());
		result = prime * result + ((endRental == null) ? 0 : endRental.hashCode());
		result = prime * result + ((creationDate == null) ? 0 : creationDate.hashCode());
		result = prime * result + bikesToRent;
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

		Reservation other = (Reservation) obj;
		if (creditCard == null) {
			if (other.creditCard != null) {
				return false;
			}
		} else if (!creditCard.equals(other.creditCard)) {
			return false;
		}

		if (rentalId == null) {
			if (other.rentalId != null) {
				return false;
			}
		} else if (!rentalId.equals(other.rentalId)) {
			return false;
		}

		if (bikeModelId == null) {
			if (other.bikeModelId != null) {
				return false;
			}
		} else if (!bikeModelId.equals(other.bikeModelId)) {
			return false;
		}

		if (startRental == null) {
			if (other.startRental != null) {
				return false;
			}
		} else if (!startRental.equals(other.startRental)) {
			return false;
		}

		if (endRental == null) {
			if (other.endRental != null) {
				return false;
			}
		} else if (!endRental.equals(other.endRental)) {
			return false;
		}

		if (bikesToRent != other.bikesToRent) {
			return false;
		}

		if (userEmail == null) {
			if (other.userEmail != null) {
				return false;
			}
		} else if (!userEmail.equals(other.userEmail)) {
			return false;
		}
		if (creationDate == null) {
			if (other.creationDate != null) {
				return false;
			}
		} else if (!creationDate.equals(other.creationDate)) {
			return false;
		}
		return true;
	}
}
