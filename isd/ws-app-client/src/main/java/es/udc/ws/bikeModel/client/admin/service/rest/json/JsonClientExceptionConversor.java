package es.udc.ws.bikeModel.client.admin.service.rest.json;

import java.io.InputStream;
import java.util.Calendar;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;

import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;
import es.udc.ws.bikeModel.client.admin.service.exceptions.*;
import es.udc.ws.bikeModel.client.admin.utils.*;

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

	public static ClientInvalidNewRentableFromException fromInvalidNewRentableFromException(InputStream ex)
			throws ParsingException {
		try {

			ObjectMapper objectMapper = ObjectMapperFactory.instance();
			JsonNode rootNode = objectMapper.readTree(ex);
			if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
				throw new ParsingException("Unrecognized JSON (object expected)");
			} else {
				JsonNode data = rootNode.path("invalidNewRentableFromException");
				Calendar newRentableFrom = DateConversor.getCalendar(data.get("newRentableFrom").textValue().trim());
				Calendar oldRentableFrom = DateConversor.getCalendar(data.get("oldRentableFrom").textValue().trim());
				return new ClientInvalidNewRentableFromException(newRentableFrom, oldRentableFrom);
			}
		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}

	public static ClientInvalidRentableFromException fromInvalidRentableFromException(InputStream ex)
			throws ParsingException {
		try {
			ObjectMapper objectMapper = ObjectMapperFactory.instance();
			JsonNode rootNode = objectMapper.readTree(ex);
			if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
				throw new ParsingException("Unrecognized JSON (object expected)");
			} else {
				JsonNode data = rootNode.path("invalidRentableFromException");
				Calendar rentableFrom = DateConversor.getCalendar(data.get("rentableFrom").textValue().trim());
				return new ClientInvalidRentableFromException(rentableFrom);
			}
		} catch (Exception e) {
			throw new ParsingException(e);
		}

	}

}
