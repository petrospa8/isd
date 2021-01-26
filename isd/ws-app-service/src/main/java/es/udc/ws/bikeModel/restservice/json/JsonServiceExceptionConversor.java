package es.udc.ws.bikeModel.restservice.json;

import java.util.Calendar;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import es.udc.ws.bikeModels.model.exceptions.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public class JsonServiceExceptionConversor {
	public final static String CONVERSION_PATTERN = "EEE, d MMM yyy HH:mm:ss Z";

	public static JsonNode toInputValidationException(InputValidationException ex) {

		ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();
		ObjectNode dataObject = JsonNodeFactory.instance.objectNode();

		dataObject.put("message", ex.getMessage());

		exceptionObject.set("inputValidationException", dataObject);

		return exceptionObject;
	}

	public static JsonNode toInstanceNotFoundException(InstanceNotFoundException ex) {

		ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();
		ObjectNode dataObject = JsonNodeFactory.instance.objectNode();

		dataObject.put("instanceId", (ex.getInstanceId() != null) ? ex.getInstanceId().toString() : null);
		dataObject.put("instanceType", ex.getInstanceType());

		exceptionObject.set("instanceNotFoundException", dataObject);

		return exceptionObject;
	}

	public static JsonNode toAlreadyRatedException(AlreadyRatedException ex) {

		ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();
		ObjectNode dataObject = JsonNodeFactory.instance.objectNode();

		dataObject.put("message", ex.getMessage());
		dataObject.put("rentalId", ex.getRentalId());

		exceptionObject.set("alreadyRatedException", dataObject);

		return exceptionObject;
	}

	public static JsonNode toInvalidRentableFromException(InvalidRentableFromException ex) {

		ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();
		ObjectNode dataObject = JsonNodeFactory.instance.objectNode();

		dataObject.put("message", ex.getMessage());
		dataObject.put("rentableFrom", getDate(ex.getRentableFrom()));

		exceptionObject.set("invalidRentableFromException", dataObject);

		return exceptionObject;
	}

	public static JsonNode toInvalidNewRentableFromException(InvalidNewRentableFromException ex) {

		ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();
		ObjectNode dataObject = JsonNodeFactory.instance.objectNode();

		dataObject.put("message", ex.getMessage());
		dataObject.put("newRentableFrom", getDate(ex.getNewRentableFrom()));
		dataObject.put("oldRentableFrom", getDate(ex.getOldRentableFrom()));

		exceptionObject.set("invalidNewRentableFromException", dataObject);

		return exceptionObject;
	}

	public static JsonNode toInvalidRentalTimeException(InvalidRentalTimeException ex) {

		ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();
		ObjectNode dataObject = JsonNodeFactory.instance.objectNode();

		dataObject.put("message", ex.getMessage());
		dataObject.put("startRental", getDate(ex.getStartRental()));
		dataObject.put("endRental", getDate(ex.getEndRental()));

		exceptionObject.set("invalidRentalTimeException", dataObject);

		return exceptionObject;
	}

	public static JsonNode toInvalidStartRentalException(InvalidStartRentalException ex) {

		ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();
		ObjectNode dataObject = JsonNodeFactory.instance.objectNode();

		dataObject.put("message", ex.getMessage());
		dataObject.put("startRental", getDate(ex.getStartRental()));
		dataObject.put("rentableFrom", getDate(ex.getRentableFrom()));

		exceptionObject.set("invalidStartRentalException", dataObject);

		return exceptionObject;
	}

	public static String getDate(Calendar rentableFrom) {

		int day = rentableFrom.get(Calendar.DAY_OF_MONTH);
		int month = rentableFrom.get(Calendar.MONTH) - Calendar.JANUARY + 1;
		int year = rentableFrom.get(Calendar.YEAR);

		String date = new String(year + "-" + month + "-" + day);
		return date;
	}

	public static JsonNode toInvalidRatingUserException(InvalidRatingUserException ex) {

		ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();
		ObjectNode dataObject = JsonNodeFactory.instance.objectNode();

		dataObject.put("message", ex.getMessage());
		dataObject.put("supposedUser", ex.getSupposedUser());
		dataObject.put("wrongUser", ex.getWrongUser());

		exceptionObject.set("invalidRatingUserException", dataObject);

		return exceptionObject;
	}

	public static JsonNode toNotEnoughUnitsException(NotEnoughUnitsException ex) {

		ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();
		ObjectNode dataObject = JsonNodeFactory.instance.objectNode();

		dataObject.put("message", ex.getMessage());
		dataObject.put("totalUnits", ex.getTotalUnits());
		dataObject.put("bikesToRent", ex.getBikesToRent());
		exceptionObject.set("notEnoughUnitsException", dataObject);

		return exceptionObject;
	}

	public static JsonNode toInvalidRatingDateException(InvalidRatingDateException ex) {
		ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();
		ObjectNode dataObject = JsonNodeFactory.instance.objectNode();
		dataObject.put("message", ex.getMessage());
		dataObject.put("ratingDate", getDate(ex.getRatingDate()));
		dataObject.put("endRental", getDate(ex.getEndRental()));
		exceptionObject.set("invalidRatingDateException", dataObject);

		return exceptionObject;
	}
}
