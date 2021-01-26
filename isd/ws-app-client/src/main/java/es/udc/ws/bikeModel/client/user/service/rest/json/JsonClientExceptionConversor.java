package es.udc.ws.bikeModel.client.user.service.rest.json;

import java.io.InputStream;
import java.util.Calendar;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;

import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;
import es.udc.ws.bikeModel.client.user.service.exceptions.*;
import es.udc.ws.bikeModel.client.user.utils.*;

public class JsonClientExceptionConversor {
	public static InputValidationException fromInputValidationException(InputStream ex) throws ParsingException {
		try {

			ObjectMapper objectMapper = ObjectMapperFactory.instance();
			JsonNode rootNode = objectMapper.readTree(ex);
			if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
				throw new ParsingException("Unrecognized JSON (object expected)");
			} else {
				String message = rootNode.get("inputValidationException").get("message").textValue();
				return new InputValidationException(message);
			}
		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}

	public static InstanceNotFoundException fromInstanceNotFoundException(InputStream ex) throws ParsingException {
		try {

			ObjectMapper objectMapper = ObjectMapperFactory.instance();
			JsonNode rootNode = objectMapper.readTree(ex);
			if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
				throw new ParsingException("Unrecognized JSON (object expected)");
			} else {
				JsonNode data = rootNode.get("instanceNotFoundException");
				String instanceId = data.get("instanceId").textValue();
				String instanceType = data.get("instanceType").textValue();
				return new InstanceNotFoundException(instanceId, instanceType);
			}

		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}

	public static ClientAlreadyRatedException fromAlreadyRatedException(InputStream ex) throws ParsingException {
		try {

			ObjectMapper objectMapper = ObjectMapperFactory.instance();
			JsonNode rootNode = objectMapper.readTree(ex);
			if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
				throw new ParsingException("Unrecognized JSON (object expected)");
			} else {
				JsonNode data = rootNode.path("alreadyRatedException");
				Long rentalId = data.get("rentalId").longValue();
				return new ClientAlreadyRatedException(rentalId);
			}
		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}

	public static ClientInvalidRatingUserException fromInvalidRatingUserException(InputStream ex)
			throws ParsingException {
		try {
			ObjectMapper objectMapper = ObjectMapperFactory.instance();
			JsonNode rootNode = objectMapper.readTree(ex);
			if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
				throw new ParsingException("Unrecognized JSON (object expected)");
			} else {
				JsonNode data = rootNode.path("invalidRatingUserException");
				String supposedUser = data.get("supposedUser").textValue();
				String wrongUser = data.get("wrongUser").textValue();
				return new ClientInvalidRatingUserException(supposedUser, wrongUser);
			}
		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}

	public static ClientInvalidRentalTimeException fromInvalidRentalTimeException(InputStream ex)
			throws ParsingException {
		try {
			ObjectMapper objectMapper = ObjectMapperFactory.instance();
			JsonNode rootNode = objectMapper.readTree(ex);
			if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
				throw new ParsingException("Unrecognized JSON (object expected)");
			} else {
				JsonNode data = rootNode.path("invalidRentalTimeException");
				Calendar startRental = DateConversor.getCalendar(data.get("startRental").textValue().trim());
				Calendar endRental = DateConversor.getCalendar(data.get("endRental").textValue().trim());
				return new ClientInvalidRentalTimeException(startRental, endRental);
			}
		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}

	public static ClientInvalidStartRentalException fromInvalidStartRentalException(InputStream ex)
			throws ParsingException {
		try {
			ObjectMapper objectMapper = ObjectMapperFactory.instance();
			JsonNode rootNode = objectMapper.readTree(ex);
			if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
				throw new ParsingException("Unrecognized JSON (object expected)");
			} else {
				JsonNode data = rootNode.path("invalidStartRentalException");
				Calendar startRental = DateConversor.getCalendar(data.get("startRental").textValue().trim());
				Calendar rentableFrom = DateConversor.getCalendar(data.get("rentableFrom").textValue().trim());
				return new ClientInvalidStartRentalException(startRental, rentableFrom);
			}
		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}

	public static ClientNotEnoughUnitsException fromNotEnoughUnitsException(InputStream ex) throws ParsingException {
		try {
			ObjectMapper objectMapper = ObjectMapperFactory.instance();
			JsonNode rootNode = objectMapper.readTree(ex);
			if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
				throw new ParsingException("Unrecognized JSON (object expected)");
			} else {
				JsonNode data = rootNode.path("notEnoughUnitsException");
				int totalUnits = data.get("totalUnits").intValue();
				int bikesToRent = data.get("bikesToRent").intValue();
				return new ClientNotEnoughUnitsException(totalUnits, bikesToRent);
			}
		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}

	public static ClientInvalidRatingDateException fromInvalidRatingDateException(InputStream ex)
			throws ParsingException {
		try {
			ObjectMapper objectMapper = ObjectMapperFactory.instance();
			JsonNode rootNode = objectMapper.readTree(ex);
			if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
				throw new ParsingException("Unrecognized JSON (object expected)");
			} else {
				JsonNode data = rootNode.path("invalidRatingDateException");
				Calendar ratingDate = DateConversor.getCalendar(data.get("ratingDate").textValue().trim());
				Calendar endRental = DateConversor.getCalendar(data.get("endRental").textValue().trim());

				return new ClientInvalidRatingDateException(ratingDate, endRental);
			}
		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}

}
