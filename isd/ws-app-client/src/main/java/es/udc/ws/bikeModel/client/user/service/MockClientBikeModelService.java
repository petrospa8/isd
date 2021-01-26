package es.udc.ws.bikeModel.client.user.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import es.udc.ws.bikeModel.client.user.service.dto.ClientBikeModelDto;
import es.udc.ws.bikeModel.client.user.service.dto.ClientReservationDto;

public class MockClientBikeModelService implements ClientBikeModelService {

	public List<ClientBikeModelDto> findByKeyWords(String keyWords, Calendar fromDate) {
		List<ClientBikeModelDto> bikeModels = new ArrayList<>();

		bikeModels.add(new ClientBikeModelDto(1L, "description 1", Calendar.getInstance(), 0, 0F));

		bikeModels.add(new ClientBikeModelDto(1L, "description 1", Calendar.getInstance(), 0, 0F));

		return bikeModels;
	}

	public ClientReservationDto rentBike(Long bikeModelId, String userEmail, String creditCard, int bikesRented,
			Calendar startRental, Calendar endRental) {
		return null;
	}

	public void rateReservation(Long rentalId, String userEmail, float score) {
	}

	public List<ClientReservationDto> findReservations(String userEmail) {
		List<ClientReservationDto> reservations = new ArrayList<>();

		Calendar end = Calendar.getInstance();
		end.add(Calendar.DATE, 2);

		reservations.add(new ClientReservationDto(1L, 1L, Calendar.getInstance(), end, 1, 6F));

		reservations.add(new ClientReservationDto(2L, 2L, Calendar.getInstance(), end, 1, 9F));

		return reservations;
	}

}
