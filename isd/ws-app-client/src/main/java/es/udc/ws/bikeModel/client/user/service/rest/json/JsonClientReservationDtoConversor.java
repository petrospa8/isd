package es.udc.ws.bikeModel.client.user.service.rest.json;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;

import es.udc.ws.bikeModel.client.user.service.dto.ClientReservationDto;
import es.udc.ws.bikeModel.client.user.utils.*;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

public class JsonClientReservationDtoConversor {

	public static ClientReservationDto toClientReservationDto(InputStream jsonReservation) throws ParsingException {
		try {

			ObjectMapper objectMapper = ObjectMapperFactory.instance();
			JsonNode rootNode = objectMapper.readTree(jsonReservation);
			if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
				throw new ParsingException("Unrecognized JSON (object expected)");
			} else {
				ObjectNode reservationObject = (ObjectNode) rootNode;
				Long rentalId = reservationObject.get("rentalId").longValue();
				Long bikeModelId = reservationObject.get("bikeModelId").longValue();
				Calendar startRentalDate = DateConversor
						.getCalendar(reservationObject.get("startRental").textValue().trim());
				Calendar endRentalDate = DateConversor
						.getCalendar(reservationObject.get("endRental").textValue().trim());
				int bikesToRent = reservationObject.get("bikesToRent").intValue();
				float score = reservationObject.get("score").floatValue();
				return new ClientReservationDto(rentalId, bikeModelId, startRentalDate, endRentalDate, bikesToRent,
						score);

			}
		} catch (ParsingException ex) {
			throw ex;
		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}

	public static List<ClientReservationDto> toClientReservationDtos(InputStream jsonReservations)
			throws ParsingException {
		try {
			ObjectMapper objectMapper = ObjectMapperFactory.instance();
			JsonNode rootNode = objectMapper.readTree(jsonReservations);
			if (rootNode.getNodeType() != JsonNodeType.ARRAY) {
				throw new ParsingException("Unrecognized JSON (array expected");
			} else {
				ArrayNode reservationsArray = (ArrayNode) rootNode;
				List<ClientReservationDto> reservationDtos = new ArrayList<>(reservationsArray.size());
				for (JsonNode reservationNode : reservationsArray) {
					reservationDtos.add(toClientReservationDto(reservationNode));
				}

				return reservationDtos;
			}
		} catch (ParsingException ex) {
			throw ex;
		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}

	private static ClientReservationDto toClientReservationDto(JsonNode reservationNode) throws ParsingException {
		if (reservationNode.getNodeType() != JsonNodeType.OBJECT) {
			throw new ParsingException("Unrecognized JSON (object expected)");
		} else {
			ObjectNode reservationObject = (ObjectNode) reservationNode;

			JsonNode rentalIdNode = reservationObject.get("rentalId");
			Long rentalId = (rentalIdNode != null) ? rentalIdNode.longValue() : null;

			JsonNode bikeModelIdNode = reservationObject.get("bikeModelId");
			Long bikeModelId = (bikeModelIdNode != null) ? bikeModelIdNode.longValue() : null;

			Calendar startRentalDate = DateConversor
					.getCalendar(reservationObject.get("startRental").textValue().trim());
			Calendar endRentalDate = DateConversor.getCalendar(reservationObject.get("endRental").textValue().trim());
			int bikesToRent = reservationObject.get("bikesToRent").intValue();
			float score = reservationObject.get("score").floatValue();

			return new ClientReservationDto(rentalId, bikeModelId, startRentalDate, endRentalDate, bikesToRent, score);
		}
	}
}
