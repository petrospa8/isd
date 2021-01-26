package es.udc.ws.bikeModel.serviceutil;

import java.util.ArrayList;
import java.util.List;

import es.udc.ws.bikeModel.dto.ServiceBikeModelDto;
import es.udc.ws.bikeModels.model.bikeModel.BikeModel;

public class BikeModelToBikeModelDtoConversor {

	public static List<ServiceBikeModelDto> toBikeModelDtos(List<BikeModel> bikeModels) {
		List<ServiceBikeModelDto> bikeModelDtos = new ArrayList<>(bikeModels.size());
		for (int i = 0; i < bikeModels.size(); i++) {
			BikeModel bikeModel = bikeModels.get(i);
			bikeModelDtos.add(toBikeModelDto(bikeModel));
		}
		return bikeModelDtos;
	}

	public static ServiceBikeModelDto toBikeModelDto(BikeModel bikeModel) {
		return new ServiceBikeModelDto(bikeModel.getBikeModelId(), bikeModel.getName(), bikeModel.getDescription(),
				bikeModel.getRentableFrom(), bikeModel.getTotalUnits(), bikeModel.getPricePerDay(),
				bikeModel.getTimesRated(), bikeModel.getAvgScore());
	}

	public static BikeModel toBikeModel(ServiceBikeModelDto bikeModel) {
		return new BikeModel(bikeModel.getBikeModelId(), bikeModel.getName(), bikeModel.getDescription(),
				bikeModel.getRentableFrom(), bikeModel.getTotalUnits(), bikeModel.getPricePerDay());
	}
}
