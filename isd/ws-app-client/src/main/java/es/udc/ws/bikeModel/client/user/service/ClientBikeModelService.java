package es.udc.ws.bikeModel.client.user.service;

import java.util.Calendar;
import java.util.List;

import es.udc.ws.bikeModel.client.user.service.dto.*;
import es.udc.ws.bikeModel.client.user.service.exceptions.*;
import es.udc.ws.util.exceptions.*;

public interface ClientBikeModelService {

	public List<ClientBikeModelDto> findByKeyWords(String keyWords, Calendar fromDate);

	public ClientReservationDto rentBike(Long bikeModelId, String userEmail, String creditCard, int bikesRented,
			Calendar startRental, Calendar endRental)
			throws InstanceNotFoundException, InputValidationException, ClientInvalidRentalTimeException,
			ClientInvalidStartRentalException, ClientNotEnoughUnitsException, Exception;

	public void rateReservation(Long rentalId, String userEmail, float score)
			throws InputValidationException, InstanceNotFoundException, ClientAlreadyRatedException,
			ClientInvalidRatingUserException, ClientInvalidRatingDateException, Exception;

	public List<ClientReservationDto> findReservations(String userEmail);
}
