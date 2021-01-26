package es.udc.ws.bikeModels.model.bikeModelService;

import java.util.Calendar;
import java.util.List;

import javax.sql.DataSource;

import es.udc.ws.bikeModels.model.bikeModel.*;
import es.udc.ws.bikeModels.model.exceptions.*;
import es.udc.ws.bikeModels.model.reservation.*;
import es.udc.ws.bikeModels.model.util.LocalPropertyValidator;
import es.udc.ws.util.exceptions.*;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.validation.PropertyValidator;

import static es.udc.ws.bikeModels.model.util.ModelConstants.BIKEMODEL_DATA_SOURCE;

import java.sql.Connection;
import java.sql.SQLException;

public class BikeModelServiceImpl implements BikeModelService {

	private final DataSource dataSource;
	private SqlBikeModelDao bikeModelDao = null;
	private SqlReservationDAO reservationDao = null;

	public BikeModelServiceImpl() {
		dataSource = DataSourceLocator.getDataSource(BIKEMODEL_DATA_SOURCE);
		bikeModelDao = SqlBikeModelDaoFactory.getDao();
		reservationDao = SqlReservationDAOFactory.getDao();
	}

	private void validateBikeModel(BikeModel bikeModel) throws InputValidationException, InvalidRentableFromException {
		PropertyValidator.validateMandatoryString("name", bikeModel.getName());
		PropertyValidator.validateMandatoryString("description", bikeModel.getDescription());
		LocalPropertyValidator.validateRentableFrom(bikeModel.getRentableFrom());
		LocalPropertyValidator.validateTotalUnits("totalUnits", bikeModel.getTotalUnits());
		LocalPropertyValidator.validatePricePerDay("pricePerDay", bikeModel.getPricePerDay());
	}

	private void validateBikeModelUpdate(BikeModel newBikeModel, BikeModel oldBikeModel, boolean wasRented)
			throws InvalidNewRentableFromException {
		LocalPropertyValidator.validateNewRentableFrom("rentableFrom", newBikeModel.getRentableFrom(),
				oldBikeModel.getRentableFrom(), wasRented);
	}

	@Override
	public BikeModel addBikeModel(BikeModel bikeModel) throws InputValidationException, InvalidRentableFromException {
		bikeModel.setCreationDate(Calendar.getInstance());
		bikeModel.setAvgScore(0F);
		bikeModel.setTimesRated(0);
		validateBikeModel(bikeModel);

		try (Connection connection = dataSource.getConnection()) {

			try {
				/* Prepare connection */
				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);

				/* Do work */
				BikeModel createdBikeModel = bikeModelDao.create(connection, bikeModel);

				/* Commit */
				connection.commit();

				return createdBikeModel;

			} catch (SQLException e) {
				connection.rollback();
				throw new RuntimeException(e);
			} catch (RuntimeException | Error e) {
				connection.rollback();
				throw e;
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public void updateBikeModel(BikeModel bikeModel) throws InputValidationException, InstanceNotFoundException,
			InvalidNewRentableFromException, InvalidRentableFromException {

		validateBikeModel(bikeModel);

		try (Connection connection = dataSource.getConnection()) {

			try {

				/* Prepare connection */
				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);

				/* Check values changed values */
				BikeModel oldBikeModel = bikeModelDao.find(connection, bikeModel.getBikeModelId());
				boolean wasRented = reservationDao.exists(connection, bikeModel.getBikeModelId());
				validateBikeModelUpdate(bikeModel, oldBikeModel, wasRented);

				/* Do work */
				bikeModelDao.update(connection, bikeModel);

				/* Commit */
				connection.commit();
			} catch (InstanceNotFoundException e) {
				connection.commit();
				throw e;
			} catch (SQLException e) {
				connection.rollback();
				throw new RuntimeException(e);
			} catch (RuntimeException | Error e) {
				connection.rollback();
				throw e;
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public BikeModel findModel(Long bikeModelId) throws InstanceNotFoundException {

		try (Connection connection = dataSource.getConnection()) {
			return bikeModelDao.find(connection, bikeModelId);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<BikeModel> findByKeyWords(String keyWords, Calendar fromDate) {
		try (Connection connection = dataSource.getConnection()) {
			return bikeModelDao.findByKeywords(connection, keyWords, fromDate);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Reservation rentBike(Long bikeModelId, String userEmail, String creditCard, int bikesToRent,
			Calendar startRental, Calendar endRental) throws InstanceNotFoundException, InputValidationException,
			InvalidRentalTimeException, InvalidStartRentalException, NotEnoughUnitsException {

		PropertyValidator.validateCreditCard(creditCard);
		LocalPropertyValidator.validateUserEmail("userEmail", userEmail);
		LocalPropertyValidator.validateEndDate("endRental", endRental, startRental);

		try (Connection connection = dataSource.getConnection()) {

			try {

				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);

				BikeModel bikeModel = bikeModelDao.find(connection, bikeModelId);

				Calendar creationDate = Calendar.getInstance();
				Calendar rentableFrom = bikeModel.getRentableFrom();
				int totalUnits = bikeModel.getTotalUnits();
				LocalPropertyValidator.validateStartDate("startRental", startRental, creationDate);
				LocalPropertyValidator.validateRentableDate("startRental", startRental, rentableFrom);
				LocalPropertyValidator.validateBikesRented("bikesToRent", bikesToRent, totalUnits);

				Reservation tmpResv = new Reservation(userEmail, creditCard, startRental, endRental, bikesToRent,
						bikeModelId, creationDate);
				Reservation reservation = reservationDao.create(connection, tmpResv);

				/* Commit */
				connection.commit();

				return reservation;

			} catch (InstanceNotFoundException e) {
				connection.commit();
				throw e;
			} catch (SQLException e) {
				connection.rollback();
				throw new RuntimeException(e);
			} catch (RuntimeException | Error e) {
				connection.rollback();
				throw e;
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public List<Reservation> findReservations(String userEmail) {

		try (Connection connection = dataSource.getConnection()) {

			List<Reservation> reservation = reservationDao.findByUser(connection, userEmail);
			return reservation;

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	private void validateRating(Reservation reservation, String userEmail, float newScore)
			throws InputValidationException, AlreadyRatedException, InvalidRatingUserException,
			InvalidRatingDateException {
		LocalPropertyValidator.validateUserScore("userScore", newScore);
		LocalPropertyValidator.validateSameUserEmail("userEmail", reservation.getUserEmail(), userEmail);
		LocalPropertyValidator.validateFinishedRental("endDate", Calendar.getInstance(), reservation.getEndRental());
		LocalPropertyValidator.validateWasRated("score", reservation.getScore(), reservation.getRentalId());

	}

	@Override
	public void rateReservation(Long rentalId, String userEmail, float newScore) throws AlreadyRatedException,
			InstanceNotFoundException, InputValidationException, InvalidNewRentableFromException,
			InvalidRatingUserException, InvalidRatingDateException, InvalidRentableFromException {
		try (Connection connection = dataSource.getConnection()) {
			Reservation reservation = reservationDao.find(connection, rentalId);
			validateRating(reservation, userEmail, newScore);
			if (reservationDao.exists(connection, reservation.getBikeModelId())) {
				BikeModel bikeModel = bikeModelDao.find(connection, reservation.getBikeModelId());
				float bikeScore = bikeModel.getAvgScore();
				int timesRated = bikeModel.getTimesRated();
				bikeScore = newScore + bikeScore * timesRated;
				timesRated++;
				bikeModel.setTimesRated(timesRated);
				bikeModel.setAvgScore(bikeScore / timesRated);
				bikeModelDao.update(connection, bikeModel);
			}
			reservation.setScore(newScore);
			reservationDao.update(connection, reservation);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
