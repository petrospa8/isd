package es.udc.ws.bikeModel.restservice.json;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import es.udc.ws.bikeModel.serviceutil.*;
import es.udc.ws.bikeModel.dto.ServiceReservationDto;

public class JsonServiceReservationDtoConversor {

	public static JsonNode toObjectNode(ServiceReservationDto reservation) {

		ObjectNode reservationNode = JsonNodeFactory.instance.objectNode();

		reservationNode.put("rentalId", reservation.getRentalId());
		reservationNode.put("bikeModelId", reservation.getBikeModelId())
				.put("startRental", DateConversor.getDate(reservation.getStartRental()))
				.put("endRental", DateConversor.getDate(reservation.getEndRental()))
				.put("bikesToRent", reservation.getBikesToRent()).put("score", reservation.getScore());
		return reservationNode;
	}

	public static ArrayNode toArrayNode(List<ServiceReservationDto> reservations) {

		ArrayNode reservationNode = JsonNodeFactory.instance.arrayNode();
		for (int i = 0; i < reservations.size(); i++) {
			ServiceReservationDto reservationDto = reservations.get(i);
			JsonNode reservationObject = toObjectNode(reservationDto);
			reservationNode.add(reservationObject);
		}

		return reservationNode;
	}
}
