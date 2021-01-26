package es.udc.ws.bikeModel.restservice.json;

import java.io.InputStream;
import java.util.Calendar;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;

import es.udc.ws.bikeModel.dto.ServiceBikeModelDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;
import es.udc.ws.bikeModel.serviceutil.*;

public class JsonServiceBikeModelDtoConversor {

	public static JsonNode toObjectNode(ServiceBikeModelDto bikeModel) {

		ObjectNode bikeModelObject = JsonNodeFactory.instance.objectNode();

		if (bikeModel.getBikeModelId() != null) {
			bikeModelObject.put("bikeModelId", bikeModel.getBikeModelId());
		}
		bikeModelObject.put("name", bikeModel.getName()).put("description", bikeModel.getDescription())
				.put("totalUnits", bikeModel.getTotalUnits()).put("pricePerDay", bikeModel.getPricePerDay())
				.put("rentableFrom", DateConversor.getDate(bikeModel.getRentableFrom()))
				.put("timesRated", bikeModel.getTimesRated()).put("avgScore", bikeModel.getAvgScore());

		return bikeModelObject;
	}

	public static ArrayNode toArrayNode(List<ServiceBikeModelDto> bikeModel) {

		ArrayNode bikeModelNode = JsonNodeFactory.instance.arrayNode();
		for (int i = 0; i < bikeModel.size(); i++) {
			ServiceBikeModelDto bikeModelDto = bikeModel.get(i);
			JsonNode bikeModelObject = toObjectNode(bikeModelDto);
			bikeModelNode.add(bikeModelObject);
		}

		return bikeModelNode;
	}

	public static ServiceBikeModelDto toServiceBikeModelDto(InputStream jsonBikeModel) throws ParsingException {
		try {
			ObjectMapper objectMapper = ObjectMapperFactory.instance();
			JsonNode rootNode = objectMapper.readTree(jsonBikeModel);

			if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
				throw new ParsingException("Unrecognized JSON (object expected)");
			} else {

				ObjectNode bikeModelObject = (ObjectNode) rootNode;
				JsonNode bikeModelIdNode = bikeModelObject.get("bikeModelId");
				Long bikeModelId = (bikeModelIdNode != null) ? bikeModelIdNode.longValue() : null;
				String name = bikeModelObject.get("name").textValue().trim();
				String description = bikeModelObject.get("description").textValue().trim();
				Calendar rentableFrom = DateConversor.getCalendar(bikeModelObject.get("rentableFrom").textValue());
				int totalUnits = bikeModelObject.get("totalUnits").intValue();
				float pricePerDay = bikeModelObject.get("pricePerDay").floatValue();
				int timesRated = bikeModelObject.get("timesRated").intValue();
				float avgScore = bikeModelObject.get("avgScore").floatValue();

				return new ServiceBikeModelDto(bikeModelId, name, description, rentableFrom, totalUnits, pricePerDay,
						timesRated, avgScore);
			}
		} catch (ParsingException ex) {
			throw ex;
		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}
}
