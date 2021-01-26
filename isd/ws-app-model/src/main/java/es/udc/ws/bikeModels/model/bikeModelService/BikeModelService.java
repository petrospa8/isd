package es.udc.ws.bikeModels.model.bikeModelService;

import java.util.Calendar;
import java.util.List;

import es.udc.ws.bikeModels.model.bikeModel.BikeModel;
import es.udc.ws.bikeModels.model.exceptions.*;
import es.udc.ws.bikeModels.model.reservation.Reservation;
import es.udc.ws.util.exceptions.*;

public interface BikeModelService {

	public BikeModel addBikeModel(BikeModel bikeModel) throws InputValidationException, InvalidRentableFromException;

	public void updateBikeModel(BikeModel bikeModel) throws InputValidationException, InstanceNotFoundException,
			InvalidRentableFromException, InvalidNewRentableFromException;

	public BikeModel findModel(Long bikeModelId) throws InstanceNotFoundException;

	public List<BikeModel> findByKeyWords(String keyWords, Calendar fromDate);

	public Reservation rentBike(Long bikeModelId, String userEmail, String creditCard, int bikesRented,
			Calendar startRental, Calendar endRental) throws InstanceNotFoundException, InputValidationException,
			InvalidRentalTimeException, InvalidStartRentalException, NotEnoughUnitsException;

	public List<Reservation> findReservations(String userEmail);

	public void rateReservation(Long rentalId, String userEmail, float avgScore) throws InputValidationException,
			InstanceNotFoundException, InvalidRentableFromException, InvalidNewRentableFromException,
			AlreadyRatedException, InvalidRatingUserException, InvalidRatingDateException;
}
