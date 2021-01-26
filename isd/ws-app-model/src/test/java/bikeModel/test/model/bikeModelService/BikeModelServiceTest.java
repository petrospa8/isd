package bikeModel.test.model.bikeModelService;

import es.udc.ws.bikeModels.model.bikeModel.BikeModel;
import es.udc.ws.bikeModels.model.bikeModel.SqlBikeModelDao;
import es.udc.ws.bikeModels.model.bikeModel.SqlBikeModelDaoFactory;
import es.udc.ws.bikeModels.model.bikeModelService.BikeModelService;
import es.udc.ws.bikeModels.model.bikeModelService.BikeModelServiceFactory;
import es.udc.ws.bikeModels.model.reservation.Reservation;
import es.udc.ws.bikeModels.model.reservation.SqlReservationDAO;
import es.udc.ws.bikeModels.model.reservation.SqlReservationDAOFactory;
import es.udc.ws.bikeModels.model.util.LocalPropertyValidator;
import es.udc.ws.bikeModels.model.exceptions.*;

import es.udc.ws.util.exceptions.*;
import es.udc.ws.util.sql.*;
import es.udc.ws.util.validation.PropertyValidator;

import static es.udc.ws.bikeModels.model.util.ModelConstants.BIKEMODEL_DATA_SOURCE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import org.junit.BeforeClass;
import org.junit.Test;

public class BikeModelServiceTest {

	private final long NON_EXISTENT_BIKEMODEL_ID = -1;
	private final long NON_EXISTENT_RESERVATION_ID = -1;
	private final String USER_EMAIL = "manuel@usc.es";

	private final String VALID_CREDIT_CARD_NUMBER = "1234567890123456";
	private final String INVALID_CREDIT_CARD_NUMBER = "";

	private final int BIKES_RENTED = 2;

	private static BikeModelService bikeModelService = null;
	private static SqlBikeModelDao bikeModelDao = null;
	private static SqlReservationDAO reservationDao = null;

	@BeforeClass
	public static void init() {

		DataSource dataSource = new SimpleDataSource();

		DataSourceLocator.addDataSource(BIKEMODEL_DATA_SOURCE, dataSource);

		bikeModelService = BikeModelServiceFactory.getService();
		bikeModelDao = SqlBikeModelDaoFactory.getDao();
		reservationDao = SqlReservationDAOFactory.getDao();
	}

	private BikeModel getValidBikeModelToRent(Calendar rentableFrom, int totalUnits) {
		BikeModel bikeModel = new BikeModel("name", "description", rentableFrom, totalUnits, 10.0F);
		return bikeModel;
	}

	private BikeModel getValidBikeModelDesc(String description) {
		Calendar rentableFrom = Calendar.getInstance();
		rentableFrom.add(Calendar.DATE, 1);
		BikeModel bikeModel = new BikeModel("name", description, rentableFrom, 50, 10.0F);

		return bikeModel;
	}

	private BikeModel getValidBikeModelRFrom(Calendar rentableFrom) {
		BikeModel bikeModel = new BikeModel("name", "description", rentableFrom, 50, 10.0F);
		return bikeModel;
	}

	private BikeModel getValidBikeModel() {
		return getValidBikeModelDesc("Description sample");
	}

	private BikeModel createUncheckedBikeModel(BikeModel bikeModel) throws SQLException {
		bikeModel.setCreationDate(Calendar.getInstance());
		bikeModel.setAvgScore(0F);
		bikeModel.setTimesRated(0);
		DataSource dataSource = DataSourceLocator.getDataSource(BIKEMODEL_DATA_SOURCE);
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
		}
	}

	public Reservation uncheckedRentBike(Long bikeModelId, String userEmail, String creditCard, int bikesToRent,
			Calendar startRental, Calendar endRental) throws InstanceNotFoundException, InputValidationException,
			InvalidRentalTimeException, InvalidStartRentalException, NotEnoughUnitsException {

		PropertyValidator.validateCreditCard(creditCard);
		LocalPropertyValidator.validateUserEmail("userEmail", userEmail);
		LocalPropertyValidator.validateEndDate("endRental", endRental, startRental);
		DataSource dataSource = DataSourceLocator.getDataSource(BIKEMODEL_DATA_SOURCE);
		try (Connection connection = dataSource.getConnection()) {

			try {

				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);

				BikeModel bikeModel = bikeModelDao.find(connection, bikeModelId);

				Calendar creationDate = Calendar.getInstance();
				Calendar rentableFrom = bikeModel.getRentableFrom();
				int totalUnits = bikeModel.getTotalUnits();
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

	private BikeModel createBikeModel(BikeModel bikeModel) throws InvalidRentableFromException {

		BikeModel addedBikeModel = null;
		try {
			addedBikeModel = bikeModelService.addBikeModel(bikeModel);
		} catch (InputValidationException e) {
			throw new RuntimeException(e);
		}
		return addedBikeModel;
	}

	private void updateBikeModel(BikeModel bikeModel) throws InstanceNotFoundException, InputValidationException,
			InvalidRentableFromException, InvalidNewRentableFromException {
		bikeModelService.updateBikeModel(bikeModel);
	}

	public void removeBikeModel(Long bikeModelId) throws InstanceNotFoundException {

		DataSource dataSource = DataSourceLocator.getDataSource(BIKEMODEL_DATA_SOURCE);

		try (Connection connection = dataSource.getConnection()) {

			try {
				/* Prepare connection. */
				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);

				/* Do work. */
				bikeModelDao.remove(connection, bikeModelId);

				/* Commit. */
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

	public Reservation findReservation(Long reservationId) throws InstanceNotFoundException {

		DataSource dataSource = DataSourceLocator.getDataSource(BIKEMODEL_DATA_SOURCE);

		try (Connection connection = dataSource.getConnection()) {

			Reservation reservation = reservationDao.find(connection, reservationId);
			return reservation;

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	private void removeReservation(Long reservationId) {

		DataSource dataSource = DataSourceLocator.getDataSource(BIKEMODEL_DATA_SOURCE);

		try (Connection connection = dataSource.getConnection()) {

			try {

				/* Prepare connection. */
				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);

				/* Do work. */
				reservationDao.remove(connection, reservationId);

				/* Commit. */
				connection.commit();

			} catch (InstanceNotFoundException e) {
				connection.commit();
				throw new RuntimeException(e);
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

	private boolean existsReservation(Long bikeModelId) {
		DataSource dataSource = DataSourceLocator.getDataSource(BIKEMODEL_DATA_SOURCE);
		boolean exists;
		try (Connection connection = dataSource.getConnection()) {

			try {

				/* Prepare connection. */
				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);

				/* Do work. */
				exists = reservationDao.exists(connection, bikeModelId);

				/* Commit. */
				connection.commit();
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
		return exists;
	}

	private void uncheckedUpdateBikeModel(BikeModel bikeModel) throws InstanceNotFoundException {
		DataSource dataSource = DataSourceLocator.getDataSource(BIKEMODEL_DATA_SOURCE);
		try (Connection connection = dataSource.getConnection()) {

			try {

				/* Prepare connection */
				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);

				/* Check values changed values */
				bikeModelDao.find(connection, bikeModel.getBikeModelId());
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

	private void uncheckedRateReservation(Long rentalId, String userEmail, float newScore) throws AlreadyRatedException,
			InstanceNotFoundException, InputValidationException, InvalidRentableFromException,
			InvalidNewRentableFromException, InvalidRatingUserException, InvalidRatingDateException {
		DataSource dataSource = DataSourceLocator.getDataSource(BIKEMODEL_DATA_SOURCE);
		try (Connection connection = dataSource.getConnection()) {
			Reservation reservation = reservationDao.find(connection, rentalId);
			LocalPropertyValidator.validateUserScore("userScore", newScore);
			LocalPropertyValidator.validateSameUserEmail("userEmail", reservation.getUserEmail(), userEmail);
			LocalPropertyValidator.validateFinishedRental("endDate", Calendar.getInstance(),
					reservation.getEndRental());
			LocalPropertyValidator.validateWasRated("score", reservation.getScore(), rentalId);
			if (reservationDao.exists(connection, reservation.getBikeModelId())) {
				BikeModel bikeModel = bikeModelDao.find(connection, reservation.getBikeModelId());
				float bikeScore = bikeModel.getAvgScore();
				int timesRated = bikeModel.getTimesRated();
				bikeScore = newScore + bikeScore * timesRated;
				timesRated++;
				bikeModel.setTimesRated(timesRated);
				bikeModel.setAvgScore(bikeScore / timesRated);
				uncheckedUpdateBikeModel(bikeModel);
			}
			reservation.setScore(newScore);
			reservationDao.update(connection, reservation);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void testAddBikeModelAndFindBikeModel()
			throws InputValidationException, InstanceNotFoundException, InvalidRentableFromException {

		BikeModel bikeModel = getValidBikeModel();
		BikeModel addedBikeModel = null;

		try {
			addedBikeModel = bikeModelService.addBikeModel(bikeModel);
			BikeModel foundBikeModel = bikeModelService.findModel(addedBikeModel.getBikeModelId());
			assertEquals(addedBikeModel, foundBikeModel);
		} finally {
			// Clear Database
			if (addedBikeModel != null) {
				removeBikeModel(addedBikeModel.getBikeModelId());
			}
		}
	}

	@Test
	public void testAddInvalidBikeModel()
			throws InvalidRentableFromException, InputValidationException, InstanceNotFoundException {
		BikeModel bikeModel = getValidBikeModel();
		BikeModel addedBikeModel = null;
		boolean exceptionCatched = false;

		try {
			// Check bike model name not null
			bikeModel.setName(null);
			try {
				addedBikeModel = bikeModelService.addBikeModel(bikeModel);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);

			// Check bike model name not empty
			exceptionCatched = false;
			bikeModel = getValidBikeModel();
			bikeModel.setName("");
			try {
				addedBikeModel = bikeModelService.addBikeModel(bikeModel);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);

			// Check bike model description not null
			exceptionCatched = false;
			bikeModel = getValidBikeModel();
			bikeModel.setDescription(null);
			try {
				addedBikeModel = bikeModelService.addBikeModel(bikeModel);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);

			// Check bike model description not empty
			exceptionCatched = false;
			bikeModel = getValidBikeModel();
			bikeModel.setDescription("");
			try {
				addedBikeModel = bikeModelService.addBikeModel(bikeModel);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);

			// Check bike model total units > 0
			exceptionCatched = false;
			bikeModel = getValidBikeModel();
			bikeModel.setTotalUnits(-1);
			try {
				addedBikeModel = bikeModelService.addBikeModel(bikeModel);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);

			// Check bike model price per day > 0
			exceptionCatched = false;
			bikeModel = getValidBikeModel();
			bikeModel.setPricePerDay(-1);
			try {
				addedBikeModel = bikeModelService.addBikeModel(bikeModel);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);

			// Check bike model rentable from date is after today's date
			exceptionCatched = false;
			bikeModel = getValidBikeModel();
			Calendar yesterday = bikeModel.getRentableFrom();
			yesterday.add(Calendar.DATE, -11);
			bikeModel.setRentableFrom(yesterday);

			try {
				addedBikeModel = bikeModelService.addBikeModel(bikeModel);
			} catch (InvalidRentableFromException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);

		} finally {
			if (!exceptionCatched) {
				// Clear Database
				removeBikeModel(addedBikeModel.getBikeModelId());
			}
		}
	}

	@Test
	public void testUpdateInvalidBikeModel()
			throws InstanceNotFoundException, InvalidRentableFromException, InvalidNewRentableFromException,
			InvalidRentalTimeException, InvalidStartRentalException, NotEnoughUnitsException, InputValidationException {
		BikeModel bikeModel;
		boolean exceptionCatched = false;

		// Check bike model name not null
		bikeModel = createBikeModel(getValidBikeModel());
		try {
			bikeModel = bikeModelService.findModel(bikeModel.getBikeModelId());
			bikeModel.setName(null);
			bikeModelService.updateBikeModel(bikeModel);
		} catch (InputValidationException e) {
			exceptionCatched = true;
			removeBikeModel(bikeModel.getBikeModelId());
		}
		assertTrue(exceptionCatched);

		// Check bike model name not empty
		exceptionCatched = false;
		bikeModel = createBikeModel(getValidBikeModel());
		try {
			bikeModel = bikeModelService.findModel(bikeModel.getBikeModelId());
			bikeModel.setName("");
			bikeModelService.updateBikeModel(bikeModel);
		} catch (InputValidationException e) {
			exceptionCatched = true;
			removeBikeModel(bikeModel.getBikeModelId());
		}
		assertTrue(exceptionCatched);

		// Check bike model description not null
		exceptionCatched = false;
		bikeModel = createBikeModel(getValidBikeModel());
		try {
			bikeModel = bikeModelService.findModel(bikeModel.getBikeModelId());
			bikeModel.setDescription(null);
			bikeModelService.updateBikeModel(bikeModel);
		} catch (InputValidationException e) {
			exceptionCatched = true;
			removeBikeModel(bikeModel.getBikeModelId());
		}
		assertTrue(exceptionCatched);

		// Check bike model description not empty
		exceptionCatched = false;
		bikeModel = createBikeModel(getValidBikeModel());
		try {
			bikeModel = bikeModelService.findModel(bikeModel.getBikeModelId());
			bikeModel.setDescription("");
			bikeModelService.updateBikeModel(bikeModel);
		} catch (InputValidationException e) {
			exceptionCatched = true;
			removeBikeModel(bikeModel.getBikeModelId());
		}
		assertTrue(exceptionCatched);

		// Check bike model price per day > 0
		exceptionCatched = false;
		bikeModel = createBikeModel(getValidBikeModel());
		try {
			bikeModel = bikeModelService.findModel(bikeModel.getBikeModelId());
			bikeModel.setPricePerDay(-20F);
			bikeModelService.updateBikeModel(bikeModel);
		} catch (InputValidationException e) {
			exceptionCatched = true;
			removeBikeModel(bikeModel.getBikeModelId());
		}
		assertTrue(exceptionCatched);

		// Check bike model total units > 0
		exceptionCatched = false;
		bikeModel = createBikeModel(getValidBikeModel());
		try {
			bikeModel = bikeModelService.findModel(bikeModel.getBikeModelId());
			bikeModel.setTotalUnits(-20);
			bikeModelService.updateBikeModel(bikeModel);
		} catch (InputValidationException e) {
			exceptionCatched = true;
			removeBikeModel(bikeModel.getBikeModelId());
		}
		assertTrue(exceptionCatched);

		// Check bike model rentable from date is not updated to an earlier date
		exceptionCatched = false;
		bikeModel = createBikeModel(getValidBikeModel());
		try {
			bikeModel = bikeModelService.findModel(bikeModel.getBikeModelId());
			Calendar date = bikeModel.getRentableFrom();
			Calendar startRental = Calendar.getInstance();
			Calendar endRental = Calendar.getInstance();
			startRental.add(Calendar.DATE, 2);
			endRental.add(Calendar.DATE, 8);
			Reservation reservation = bikeModelService.rentBike(bikeModel.getBikeModelId(), USER_EMAIL,
					VALID_CREDIT_CARD_NUMBER, 13, startRental, endRental);
			if (existsReservation(bikeModel.getBikeModelId())) {
				date.add(Calendar.DATE, -2);
				removeReservation(reservation.getRentalId());
			}
			bikeModelService.updateBikeModel(bikeModel);
		} catch (InvalidRentableFromException e) {
			exceptionCatched = true;
			removeBikeModel(bikeModel.getBikeModelId());
		}

		assertTrue(exceptionCatched);
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testFindNonExistentBikeModel() throws InstanceNotFoundException {

		bikeModelService.findModel(NON_EXISTENT_BIKEMODEL_ID);

	}

	@Test
	public void testUpdateBike() throws InputValidationException, InstanceNotFoundException,
			InvalidRentableFromException, InvalidNewRentableFromException {

		BikeModel bikeModel = createBikeModel(getValidBikeModel());
		Calendar rentableFrom = Calendar.getInstance();
		rentableFrom.add(Calendar.DATE, 1);
		try {
			BikeModel bikeModelToUpdate = new BikeModel(bikeModel.getBikeModelId(), "new name", "new description",
					rentableFrom, 40, 20.50F, Calendar.getInstance());
			updateBikeModel(bikeModelToUpdate);

			BikeModel updatedBikeModel = bikeModelService.findModel(bikeModel.getBikeModelId());

			bikeModelToUpdate.setCreationDate(bikeModel.getCreationDate());
			assertEquals(bikeModelToUpdate.getCreationDate(), updatedBikeModel.getCreationDate());

		} finally {
			// Clear Database
			removeBikeModel(bikeModel.getBikeModelId());
		}

	}

	@Test(expected = InstanceNotFoundException.class)
	public void testUpdateNonExistentBikeModel() throws InputValidationException, InstanceNotFoundException,
			InvalidRentableFromException, InvalidNewRentableFromException {

		BikeModel bikeModel = getValidBikeModel();
		bikeModel.setBikeModelId(NON_EXISTENT_BIKEMODEL_ID);
		bikeModel.setCreationDate(Calendar.getInstance());
		bikeModelService.updateBikeModel(bikeModel);

	}

	@Test(expected = InvalidNewRentableFromException.class)
	public void testUpdateInvalidRentableFrom() throws InvalidRentableFromException, InstanceNotFoundException,
			InputValidationException, InvalidRentalTimeException, InvalidStartRentalException, NotEnoughUnitsException,
			InvalidNewRentableFromException {

		BikeModel bikeModel = createBikeModel(getValidBikeModel());
		Reservation reservation = null;
		Calendar startRental = Calendar.getInstance();
		Calendar endRental = Calendar.getInstance();
		startRental.add(Calendar.DATE, 6);
		endRental.add(Calendar.DATE, 8);
		try {
			reservation = bikeModelService.rentBike(bikeModel.getBikeModelId(), USER_EMAIL, VALID_CREDIT_CARD_NUMBER,
					BIKES_RENTED, startRental, endRental);
			Calendar newDate = Calendar.getInstance();
			newDate.add(Calendar.DATE, 3);
			reservation.getBikeModelId();
			bikeModel.setRentableFrom(newDate);
			bikeModelService.updateBikeModel(bikeModel);
		} finally {
			removeBikeModel(bikeModel.getBikeModelId());
		}

	}

	@Test(expected = InstanceNotFoundException.class)
	public void testRemoveBikeModel() throws InstanceNotFoundException, InvalidRentableFromException {

		BikeModel bikeModel = createBikeModel(getValidBikeModel());
		boolean exceptionCatched = false;
		try {
			removeBikeModel(bikeModel.getBikeModelId());
		} catch (InstanceNotFoundException e) {
			exceptionCatched = true;
		}
		assertTrue(!exceptionCatched);

		bikeModelService.findModel(bikeModel.getBikeModelId());
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testRemoveNonExistentBikeModel() throws InstanceNotFoundException {

		removeBikeModel(NON_EXISTENT_BIKEMODEL_ID);
	}

	@Test
	public void testFindBikeModels() throws InvalidRentableFromException, InstanceNotFoundException {
		// Add bike models
		List<BikeModel> bikeModels = new LinkedList<BikeModel>();

		BikeModel bikeModel1 = createBikeModel(getValidBikeModelDesc("xeo 1"));
		bikeModels.add(bikeModel1);
		BikeModel bikeModel2 = createBikeModel(getValidBikeModelDesc("aurum 1"));
		bikeModels.add(bikeModel2);
		BikeModel bikeModel3 = createBikeModel(getValidBikeModelDesc("aurum 12"));
		bikeModels.add(bikeModel3);

		try {
			List<BikeModel> foundBikeModels = bikeModelService.findByKeyWords("1 X", null);
			assertEquals(foundBikeModels.get(0), bikeModels.get(0));
			assertEquals(1, foundBikeModels.size());
			foundBikeModels.clear();
			foundBikeModels = bikeModelService.findByKeyWords("1", null);
			assertEquals(foundBikeModels.get(0), bikeModels.get(0));
			assertEquals(foundBikeModels.get(1), bikeModels.get(1));
			assertEquals(foundBikeModels.get(2), bikeModels.get(2));
			assertEquals(3, foundBikeModels.size());
			foundBikeModels.clear();
			foundBikeModels = bikeModelService.findByKeyWords("bike 5", null);
			assertEquals(0, foundBikeModels.size());
		} finally {
			// Clear Database
			for (BikeModel bikeModel : bikeModels) {
				removeBikeModel(bikeModel.getBikeModelId());
			}
		}

	}

	@Test
	public void testRentBikeModelAndFindReservation()
			throws InstanceNotFoundException, InputValidationException, InvalidRentableFromException,
			InvalidRentalTimeException, InvalidStartRentalException, NotEnoughUnitsException {

		Calendar rentableFrom = Calendar.getInstance();
		rentableFrom.add(Calendar.DATE, 1);
		BikeModel bikeModel = createBikeModel(getValidBikeModelToRent(rentableFrom, 20));
		Reservation reservation = null;

		try {
			/* Rent Bike Model */
			Calendar startRental = Calendar.getInstance();
			Calendar endRental = Calendar.getInstance();
			startRental.add(Calendar.DATE, 2);
			endRental.add(Calendar.DATE, 8);

			reservation = bikeModelService.rentBike(bikeModel.getBikeModelId(), USER_EMAIL, VALID_CREDIT_CARD_NUMBER,
					12, startRental, endRental);

			Reservation foundReservation = findReservation(reservation.getRentalId());

			assertEquals(reservation, foundReservation);
			assertEquals(VALID_CREDIT_CARD_NUMBER, foundReservation.getCreditCard());
			assertEquals(USER_EMAIL, foundReservation.getUserEmail());
			assertEquals(bikeModel.getBikeModelId(), foundReservation.getBikeModelId());
			assertTrue(foundReservation.getBikesToRent() == 12);
			assertTrue((foundReservation.getStartRental().compareTo(startRental) >= 0)
					&& (foundReservation.getEndRental().compareTo(endRental) >= 0));

		} finally {
			/* Clear database: remove reservation (if created) and bike model. */
			if (reservation != null) {
				removeReservation(reservation.getRentalId());
			}
			removeBikeModel(bikeModel.getBikeModelId());
		}
	}

	@Test
	public void testAddReservationAndFindByUserEmail()
			throws InputValidationException, InstanceNotFoundException, InvalidRentableFromException,
			InvalidRentalTimeException, InvalidStartRentalException, NotEnoughUnitsException {

		List<Reservation> reservations = new ArrayList<Reservation>();
		Calendar rentableFrom = Calendar.getInstance();
		rentableFrom.add(Calendar.DATE, 1);
		BikeModel bikeModel = createBikeModel(getValidBikeModelToRent(rentableFrom, 50));
		try {

			Calendar startRental = Calendar.getInstance();
			Calendar endRental = Calendar.getInstance();
			startRental.add(Calendar.DATE, 2);
			endRental.add(Calendar.DATE, 8);

			Reservation reservation1 = bikeModelService.rentBike(bikeModel.getBikeModelId(), USER_EMAIL,
					VALID_CREDIT_CARD_NUMBER, 12, startRental, endRental);
			Reservation reservation2 = bikeModelService.rentBike(bikeModel.getBikeModelId(), USER_EMAIL,
					VALID_CREDIT_CARD_NUMBER, 14, startRental, endRental);
			Reservation reservation3 = bikeModelService.rentBike(bikeModel.getBikeModelId(), USER_EMAIL,
					VALID_CREDIT_CARD_NUMBER, 14, startRental, endRental);
			reservations.add(reservation1);
			reservations.add(reservation2);
			reservations.add(reservation3);
			List<Reservation> foundReservations = bikeModelService.findReservations(USER_EMAIL);
			assertEquals(reservations, foundReservations);

			foundReservations = bikeModelService.findReservations("y");
			assertEquals(0, foundReservations.size());

		} finally {
			// Clear Database
			for (Reservation reservation : reservations) {
				removeReservation(reservation.getRentalId());
			}
			removeBikeModel(bikeModel.getBikeModelId());
		}
	}

	@Test(expected = InputValidationException.class)
	public void testRentBikeModelWithInvalidCreditCard()
			throws InputValidationException, InstanceNotFoundException, InvalidRentableFromException,
			InvalidRentalTimeException, InvalidStartRentalException, NotEnoughUnitsException {

		BikeModel bikeModel = createBikeModel(getValidBikeModel());
		try {
			Calendar endRental = bikeModel.getRentableFrom();
			endRental.add(Calendar.DATE, 5);
			bikeModelService.rentBike(bikeModel.getBikeModelId(), USER_EMAIL, INVALID_CREDIT_CARD_NUMBER, BIKES_RENTED,
					bikeModel.getRentableFrom(), endRental);
		} finally {
			/* Clear database. */
			removeBikeModel(bikeModel.getBikeModelId());
		}

	}

	@Test(expected = InstanceNotFoundException.class)
	public void testRentNonExistentBikeModel()
			throws InputValidationException, InstanceNotFoundException, InvalidRentableFromException,
			InvalidRentalTimeException, InvalidStartRentalException, NotEnoughUnitsException {
		Calendar startRental = Calendar.getInstance();
		Calendar endRental = Calendar.getInstance();
		Reservation reservation = bikeModelService.rentBike(NON_EXISTENT_BIKEMODEL_ID, USER_EMAIL,
				VALID_CREDIT_CARD_NUMBER, 10, startRental, endRental);

		removeReservation(reservation.getRentalId());
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testFindNonExistentReservation() throws InstanceNotFoundException {

		findReservation(NON_EXISTENT_RESERVATION_ID);

	}

	@Test
	public void testRateBikeModel() throws InputValidationException, InstanceNotFoundException, AlreadyRatedException,
			InvalidRentableFromException, InvalidRentalTimeException, InvalidStartRentalException,
			InvalidNewRentableFromException, NotEnoughUnitsException, InvalidRatingUserException,
			InvalidRatingDateException, SQLException {
		Calendar rentableFrom = Calendar.getInstance();
		rentableFrom.add(Calendar.DATE, -7);
		BikeModel bikeModel = createUncheckedBikeModel(getValidBikeModelRFrom(rentableFrom));
		Reservation reservation = null, reservation2 = null, reservation3 = null, reservation4 = null;

		try {
			Calendar startRental = bikeModel.getRentableFrom();
			startRental.add(Calendar.DATE, 2);
			Calendar endRental = Calendar.getInstance();
			endRental.setTime(startRental.getTime());
			endRental.add(Calendar.DATE, 5);
			reservation = uncheckedRentBike(bikeModel.getBikeModelId(), USER_EMAIL, VALID_CREDIT_CARD_NUMBER,
					BIKES_RENTED, startRental, endRental);
			uncheckedRateReservation(reservation.getRentalId(), USER_EMAIL, 3F);
			reservation2 = uncheckedRentBike(bikeModel.getBikeModelId(), USER_EMAIL, VALID_CREDIT_CARD_NUMBER,
					BIKES_RENTED, startRental, endRental);
			uncheckedRateReservation(reservation2.getRentalId(), USER_EMAIL, 8F);
			reservation3 = uncheckedRentBike(bikeModel.getBikeModelId(), USER_EMAIL, VALID_CREDIT_CARD_NUMBER,
					BIKES_RENTED, startRental, endRental);
			uncheckedRateReservation(reservation3.getRentalId(), USER_EMAIL, 7F);
			reservation4 = uncheckedRentBike(bikeModel.getBikeModelId(), USER_EMAIL, VALID_CREDIT_CARD_NUMBER,
					BIKES_RENTED, startRental, endRental);
			uncheckedRateReservation(reservation4.getRentalId(), USER_EMAIL, 10F);
			BikeModel ratedBikeModel = bikeModelService.findModel(bikeModel.getBikeModelId());
			float avgScore = ratedBikeModel.getAvgScore();
			assertTrue(avgScore == 7F);
		} finally {
			removeReservation(reservation.getRentalId());
			removeReservation(reservation2.getRentalId());
			removeReservation(reservation3.getRentalId());
			removeReservation(reservation4.getRentalId());
			removeBikeModel(bikeModel.getBikeModelId());
		}
	}

	@Test(expected = AlreadyRatedException.class)
	public void testAlreadyRatedReservation() throws InstanceNotFoundException, AlreadyRatedException,
			InputValidationException, InvalidRentalTimeException, NotEnoughUnitsException, InvalidStartRentalException,
			InvalidRentableFromException, InvalidNewRentableFromException, InvalidRatingUserException,
			InvalidRatingDateException, SQLException {
		Calendar rentableFrom = Calendar.getInstance();
		rentableFrom.add(Calendar.DATE, -7);
		BikeModel bikeModel = createUncheckedBikeModel(getValidBikeModelRFrom(rentableFrom));
		Reservation reservation = null;

		try {
			Calendar startRental = bikeModel.getRentableFrom();
			startRental.add(Calendar.DATE, 1);
			Calendar endRental = Calendar.getInstance();
			endRental.setTime(startRental.getTime());
			endRental.add(Calendar.DATE, 5);
			reservation = uncheckedRentBike(bikeModel.getBikeModelId(), USER_EMAIL, VALID_CREDIT_CARD_NUMBER,
					BIKES_RENTED, startRental, endRental);
			uncheckedRateReservation(reservation.getRentalId(), USER_EMAIL, 3F);
			uncheckedRateReservation(reservation.getRentalId(), USER_EMAIL, 8F);
		} finally {
			removeBikeModel(bikeModel.getBikeModelId());
		}
	}

	@Test(expected = InvalidRentalTimeException.class)
	public void testLengthyReservation() throws InvalidRentableFromException, InstanceNotFoundException,
			InputValidationException, InvalidRentalTimeException, InvalidStartRentalException, NotEnoughUnitsException {
		Calendar rentableFrom = Calendar.getInstance();
		rentableFrom.add(Calendar.DATE, -7);
		BikeModel bikeModel = createBikeModel(getValidBikeModel());

		try {
			Calendar startRental = bikeModel.getRentableFrom();
			Calendar endRental = Calendar.getInstance();
			endRental.setTime(startRental.getTime());
			endRental.add(Calendar.DATE, 20);
			bikeModelService.rentBike(bikeModel.getBikeModelId(), USER_EMAIL, VALID_CREDIT_CARD_NUMBER, BIKES_RENTED,
					startRental, endRental);
		} finally {
			removeBikeModel(bikeModel.getBikeModelId());
		}
	}

	@Test(expected = InvalidRatingUserException.class)
	public void testInvalidUserRateReservation() throws InstanceNotFoundException, AlreadyRatedException,
			InputValidationException, InvalidRentalTimeException, NotEnoughUnitsException, InvalidStartRentalException,
			InvalidRentableFromException, InvalidNewRentableFromException, InvalidRatingUserException,
			InvalidRatingDateException, SQLException {
		BikeModel bikeModel = createUncheckedBikeModel(getValidBikeModel());

		try {
			Calendar startRental = bikeModel.getRentableFrom();
			Date startDate = startRental.getTime();
			Calendar endRental = Calendar.getInstance();
			endRental.setTime(startDate);
			endRental.add(Calendar.DATE, 5);
			Reservation reservation = bikeModelService.rentBike(bikeModel.getBikeModelId(), USER_EMAIL,
					VALID_CREDIT_CARD_NUMBER, BIKES_RENTED, startRental, endRental);
			uncheckedRateReservation(reservation.getRentalId(), "notRealUser@yudc.es", 3F);
			BikeModel ratedBikeModel = bikeModelService.findModel(bikeModel.getBikeModelId());
			float avgScore = ratedBikeModel.getAvgScore();
			assertTrue(avgScore == 7F);
		} finally {
			removeBikeModel(bikeModel.getBikeModelId());
		}
	}

	@Test(expected = InputValidationException.class)
	public void testInvalidScore() throws InputValidationException, InstanceNotFoundException, AlreadyRatedException,
			InvalidRentalTimeException, NotEnoughUnitsException, InvalidStartRentalException,
			InvalidRentableFromException, InvalidNewRentableFromException, InvalidRatingUserException,
			InvalidRatingDateException {

		BikeModel bikeModel = createBikeModel(getValidBikeModel());
		Reservation reservation = null;

		try {
			Calendar startRental = bikeModel.getRentableFrom();
			Date startDate = startRental.getTime();
			Calendar endRental = Calendar.getInstance();
			endRental.setTime(startDate);
			endRental.add(Calendar.DATE, 5);
			reservation = bikeModelService.rentBike(bikeModel.getBikeModelId(), USER_EMAIL, VALID_CREDIT_CARD_NUMBER,
					BIKES_RENTED, startRental, endRental);
			bikeModelService.rateReservation(reservation.getRentalId(), USER_EMAIL, 11F);
		} finally {
			removeReservation(reservation.getRentalId());
			removeBikeModel(bikeModel.getBikeModelId());
		}
	}

	@Test
	public void testfindByKeywordsRentableFrom() throws InvalidRentableFromException, InstanceNotFoundException {
		List<BikeModel> bikeModels = new LinkedList<BikeModel>();
		List<BikeModel> foundBikes = new LinkedList<BikeModel>();
		Calendar rentableFrom1 = Calendar.getInstance();
		rentableFrom1.add(Calendar.DATE, 30);
		Calendar rentableFrom2 = Calendar.getInstance();
		rentableFrom2.add(Calendar.DATE, 29);
		Calendar rentableFrom3 = Calendar.getInstance();
		rentableFrom3.add(Calendar.DATE, 28);
		Calendar rentableFrom4 = Calendar.getInstance();
		rentableFrom4.add(Calendar.DATE, 27);
		BikeModel bikeModel1 = createBikeModel(getValidBikeModelRFrom(rentableFrom1));
		BikeModel bikeModel2 = createBikeModel(getValidBikeModelRFrom(rentableFrom2));
		BikeModel bikeModel3 = createBikeModel(getValidBikeModelRFrom(rentableFrom3));
		BikeModel bikeModel4 = createBikeModel(getValidBikeModelRFrom(rentableFrom4));
		bikeModels.add(bikeModel1);
		bikeModels.add(bikeModel2);
		bikeModels.add(bikeModel3);
		bikeModels.add(bikeModel4);

		try {
			foundBikes = bikeModelService.findByKeyWords("description", bikeModel1.getRentableFrom());
			assertEquals(bikeModels, foundBikes);
		} finally {
			foundBikes.clear();
			for (BikeModel bikeModel : bikeModels) {
				removeBikeModel(bikeModel.getBikeModelId());
			}
		}
	}

	@Test(expected = InvalidRatingDateException.class)
	public void testInvalidRatingDate() throws InvalidRentableFromException, InstanceNotFoundException,
			InputValidationException, InvalidNewRentableFromException, AlreadyRatedException,
			InvalidRatingUserException, InvalidRatingDateException, InvalidRentalTimeException,
			InvalidStartRentalException, NotEnoughUnitsException {
		BikeModel bikeModel = createBikeModel(getValidBikeModel());
		Reservation reservation = null;
		try {
			Calendar startRental = bikeModel.getRentableFrom();
			Date startDate = startRental.getTime();
			Calendar endRental = Calendar.getInstance();
			endRental.setTime(startDate);
			endRental.add(Calendar.DATE, 5);
			reservation = bikeModelService.rentBike(bikeModel.getBikeModelId(), USER_EMAIL, VALID_CREDIT_CARD_NUMBER,
					BIKES_RENTED, startRental, endRental);
			bikeModelService.rateReservation(reservation.getRentalId(), USER_EMAIL, 3F);
		} finally {
			removeReservation(reservation.getRentalId());
			removeBikeModel(bikeModel.getBikeModelId());
		}
	}

}
