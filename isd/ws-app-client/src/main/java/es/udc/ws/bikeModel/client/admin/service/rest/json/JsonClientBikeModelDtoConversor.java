package es.udc.ws.bikeModel.client.admin.service.rest.json;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;

import es.udc.ws.bikeModel.client.admin.service.dto.ClientBikeModelDto;
import es.udc.ws.bikeModel.client.admin.utils.*;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

public class JsonClientBikeModelDtoConversor {

	public static JsonNode toObjectNode(ClientBikeModelDto bikeModel) throws IOException {

		ObjectNode bikeModelObject = JsonNodeFactory.instance.objectNode();

		if (bikeModel.getBikeModelId() != null) {
			bikeModelObject.put("bikeModelId", bikeModel.getBikeModelId());
		}

		bikeModelObject.put("name", bikeModel.getName()).put("description", bikeModel.getDescription())
				.put("rentableFrom", DateConversor.getDate(bikeModel.getRentableFrom()))
				.put("totalUnits", bikeModel.getTotalUnits()).put("pricePerDay", bikeModel.getPricePerDay())
				.put("timesRated", bikeModel.getTimesRated()).put("avgScore", bikeModel.getAvgScore());

		return bikeModelObject;
	}

	public static ClientBikeModelDto toClientBikeModelDto(InputStream jsonBikeModel) throws ParsingException {

		try {
			ObjectMapper objectMapper = ObjectMapperFactory.instance();
			JsonNode rootNode = objectMapper.readTree(jsonBikeModel);
			if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
				throw new ParsingException("Unrecognised JSON (object expected");
			} else {
				return toClientBikeModelDto(rootNode);
			}
		} catch (ParsingException ex) {
			throw ex;
		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}

	public static List<ClientBikeModelDto> toClientBikeModelDtos(InputStream jsonBikeModels) throws ParsingException {
		try {
			ObjectMapper objectMapper = ObjectMapperFactory.instance();
			JsonNode rootNode = objectMapper.readTree(jsonBikeModels);
			if (rootNode.getNodeType() != JsonNodeType.ARRAY) {
				throw new ParsingException("Unrecognized JSON (array expected");
			} else {
				ArrayNode bikeModelsArray = (ArrayNode) rootNode;
				List<ClientBikeModelDto> bikeModelDtos = new ArrayList<>(bikeModelsArray.size());
				for (JsonNode bikeModelNode : bikeModelsArray) {
					bikeModelDtos.add(toClientBikeModelDto(bikeModelNode));
				}

				return bikeModelDtos;
			}
		} catch (ParsingException ex) {
			throw ex;
		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}

	private static ClientBikeModelDto toClientBikeModelDto(JsonNode bikeModelNode) throws ParsingException {
		if (bikeModelNode.getNodeType() != JsonNodeType.OBJECT) {
			throw new ParsingException("Unrecognized JSON (object expected)");
		} else {
			ObjectNode bikeModelObject = (ObjectNode) bikeModelNode;

			JsonNode bikeModelIdNode = bikeModelObject.get("bikeModelId");
			Long bikeModelId = (bikeModelIdNode != null) ? bikeModelIdNode.longValue() : null;
			String name = bikeModelObject.get("name").textValue().trim();
			String description = bikeModelObject.get("description").textValue().trim();
			Calendar rentableFrom = DateConversor.getCalendar(bikeModelObject.get("rentableFrom").textValue().trim());
			int totalUnits = bikeModelObject.get("totalUnits").intValue();
			float pricePerDay = bikeModelObject.get("pricePerDay").floatValue();
			int timesRated = bikeModelObject.get("timesRated").intValue();
			float avgScore = bikeModelObject.get("avgScore").floatValue();

			return new ClientBikeModelDto(bikeModelId, name, description, rentableFrom, totalUnits, pricePerDay,
					timesRated, avgScore);
		}
	}

}
