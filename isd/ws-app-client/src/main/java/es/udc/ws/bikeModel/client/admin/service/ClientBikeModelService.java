package es.udc.ws.bikeModel.client.admin.service;

import es.udc.ws.bikeModel.client.admin.service.dto.*;
import es.udc.ws.bikeModel.client.admin.service.exceptions.*;
import es.udc.ws.util.exceptions.*;

public interface ClientBikeModelService {

	public Long addBikeModel(ClientBikeModelDto bikeModel)
			throws InputValidationException, ClientInvalidRentableFromException, Exception;

	public void updateBikeModel(ClientBikeModelDto bikeModel)
			throws InputValidationException, InstanceNotFoundException, ClientInvalidRentableFromException,
			ClientInvalidNewRentableFromException, Exception;

	public ClientBikeModelDto findBike(Long bikeModelId) throws InstanceNotFoundException;
}
