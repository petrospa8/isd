package es.udc.ws.bikeModel.client.admin.service;

import java.util.Calendar;

import es.udc.ws.bikeModel.client.admin.service.dto.ClientBikeModelDto;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public class MockClientBikeModelService implements ClientBikeModelService {

	@Override
	public Long addBikeModel(ClientBikeModelDto bikeModel) {
		return (long) 0;
	}

	@Override
	public void updateBikeModel(ClientBikeModelDto bikeModel) {
		return;
	}

	@Override
	public ClientBikeModelDto findBike(Long bikeModelId) throws InstanceNotFoundException {
		return new ClientBikeModelDto(bikeModelId, "bike H", "desc1", Calendar.getInstance(), 20, 5F, 0, 0F);
	}

}
