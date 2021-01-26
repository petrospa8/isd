package es.udc.ws.bikeModel.serviceutil;

import java.util.ArrayList;
import java.util.List;

import es.udc.ws.bikeModel.dto.ServiceReservationDto;
import es.udc.ws.bikeModels.model.reservation.Reservation;

public class ReservationToReservationDtoConversor {

	public static List<ServiceReservationDto> toReservationDtos(List<Reservation> reservations) {
		List<ServiceReservationDto> reservationDtos = new ArrayList<>(reservations.size());
		for (int i = 0; i < reservations.size(); i++) {
			Reservation reservation = reservations.get(i);
			reservationDtos.add(toReservationDto(reservation));
		}
		return reservationDtos;
	}

	public static ServiceReservationDto toReservationDto(Reservation reservation) {
		return new ServiceReservationDto(reservation.getRentalId(), reservation.getBikeModelId(),
				reservation.getStartRental(), reservation.getEndRental(), reservation.getBikesToRent(),
				reservation.getScore());
	}
}
