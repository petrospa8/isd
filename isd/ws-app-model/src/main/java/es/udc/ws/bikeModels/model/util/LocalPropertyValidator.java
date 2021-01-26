package es.udc.ws.bikeModels.model.util;

import java.util.Calendar;
import java.util.Date;

import es.udc.ws.bikeModels.model.exceptions.*;
import es.udc.ws.util.exceptions.InputValidationException;

public class LocalPropertyValidator {

	private LocalPropertyValidator() {
	}

	public static void validateRentableDate(String propertyName, Calendar propertyValue, Calendar lowerLimit)
			throws InvalidStartRentalException {
		if ((propertyValue == null) || (lowerLimit.after(propertyValue))) {
			throw new InvalidStartRentalException(propertyValue, lowerLimit);
		}
	}

	public static void validateStartDate(String propertyName, Calendar propertyValue, Calendar lowerLimit)
			throws InputValidationException {
		if ((propertyValue == null) || (lowerLimit.after(propertyValue))) {
			throw new InputValidationException("Invalid date :" + propertyName + " (" + propertyValue.getTime()
					+ "). It must be after today's date " + "(" + lowerLimit.getTime() + ".");
		}
	}

	public static void validateEndDate(String propertyName, Calendar propertyValue, Calendar upperLimit)
			throws InvalidRentalTimeException {
		Date startRentalDate = upperLimit.getTime();
		Calendar limit = Calendar.getInstance();
		limit.setTime(startRentalDate);
		limit.add(Calendar.DATE, 15);
		if ((propertyValue == null) || (propertyValue.after(limit)) || propertyValue.before(upperLimit)) {
			throw new InvalidRentalTimeException(upperLimit, propertyValue);
		}
	}

	public static void validateBikesRented(String propertyName, int bikesRented, int totalUnits)
			throws NotEnoughUnitsException {
		if (bikesRented > totalUnits) {
			throw new NotEnoughUnitsException(totalUnits, bikesRented);
		}
	}

	public static void validateRentableFrom(Calendar rentableFrom) throws InvalidRentableFromException {
		if (rentableFrom.before(Calendar.getInstance())) {
			throw new InvalidRentableFromException(rentableFrom);
		}
	}

	public static void validateTotalUnits(String propertyName, int totalUnits) throws InputValidationException {
		if (totalUnits <= 0) {
			throw new InputValidationException(
					"Invalid " + propertyName + " (" + totalUnits + "). It must be above zero.");
		}
	}

	public static void validatePricePerDay(String propertyName, float pricePerDay) throws InputValidationException {
		if (pricePerDay <= 0F) {
			throw new InputValidationException(
					"Invalid " + propertyName + " (" + pricePerDay + "). It must be above zero.");
		}
	}

	public static void validateUserEmail(String propertyName, String userEmail) throws InputValidationException {
		if (userEmail == null) {
			throw new InputValidationException("Invalid " + propertyName + ") . It cannot be null.");
		}
	}

	public static void validateCreationDate(String propertyName, Calendar creationDate)
			throws InputValidationException {
		if (creationDate == null) {
			throw new InputValidationException("Invalid " + propertyName + ". It cannot be null");
		}
	}

	public static void validateUserScore(String propertyName, float userScore) throws InputValidationException {
		if ((userScore < 0F) || (userScore > 10F)) {
			throw new InputValidationException(
					"Invalid " + propertyName + " " + userScore + ": must be between 0 and 10 ");
		}
	}

	public static void validateNewRentableFrom(String propertyName, Calendar newRentableFrom, Calendar oldRentableFrom,
			boolean wasRented) throws InvalidNewRentableFromException {
		if (wasRented) {
			if (newRentableFrom.after(oldRentableFrom)) {
				throw new InvalidNewRentableFromException(newRentableFrom, oldRentableFrom);
			}
		}
	}

	public static void validateWasRated(String propertyName, float score, Long rentalId) throws AlreadyRatedException {
		if (score != -1) {
			throw new AlreadyRatedException(rentalId);
		}
	}

	public static void validateSameUserEmail(String propertyName, String reservationUserEmail, String ratingUserEmail)
			throws InvalidRatingUserException {
		if (!reservationUserEmail.equals(ratingUserEmail)) {
			throw new InvalidRatingUserException(reservationUserEmail, ratingUserEmail);
		}

	}

	public static void validateFinishedRental(String propertyName, Calendar ratingDate, Calendar endRental)
			throws InvalidRatingDateException {
		if (ratingDate.before(endRental)) {
			throw new InvalidRatingDateException(ratingDate, endRental);
		}
	}

}
