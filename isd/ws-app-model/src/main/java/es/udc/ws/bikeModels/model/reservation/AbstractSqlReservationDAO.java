package es.udc.ws.bikeModels.model.reservation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

public abstract class AbstractSqlReservationDAO implements SqlReservationDAO {

	protected AbstractSqlReservationDAO() {
	}

	@Override
	public Reservation find(Connection connection, Long rentalId) throws InstanceNotFoundException {

		/* Create queryString */
		String queryString = "SELECT userEmail, creditCard, startRental, endRental, "
				+ "bikesToRent, bikeModelId, creationDate, score" + " FROM Reservation WHERE rentalId = ?";

		try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
			/* Fill "preparedStatement". */
			int i = 1;
			preparedStatement.setLong(i++, rentalId.longValue());

			/* Execute query. */
			ResultSet resultSet = preparedStatement.executeQuery();

			if (!resultSet.next()) {
				throw new InstanceNotFoundException(rentalId, Reservation.class.getName());
			}

			/* Get results. */
			i = 1;
			String userEmail = resultSet.getString(i++);
			String creditCard = resultSet.getString(i++);
			Calendar startRental = Calendar.getInstance();
			startRental.setTime(resultSet.getTimestamp(i++));
			Calendar endRental = Calendar.getInstance();
			endRental.setTime(resultSet.getTimestamp(i++));
			int bikesToRent = resultSet.getInt(i++);
			Long bikeModelId = resultSet.getLong(i++);
			Calendar creationDate = Calendar.getInstance();
			creationDate.setTime(resultSet.getTimestamp(i++));
			float score = resultSet.getFloat(i++);

			/* Return reservation. */
			Reservation reservation = new Reservation(rentalId, userEmail, creditCard, startRental, endRental,
					bikesToRent, bikeModelId, creationDate);
			reservation.setScore(score);
			return reservation;

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void update(Connection connection, Reservation reservation) throws InstanceNotFoundException {

		/* Create queryString */
		String queryString = "UPDATE Reservation " + "SET startRental = ?, endRental = ?, bikesToRent = ?,"
				+ "bikeModelId = ?, creationDate = ?, score = ? WHERE rentalId = ?";

		try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
			/* Fill preparedStatemenent */
			int i = 1;

			preparedStatement.setTimestamp(i++, new Timestamp(reservation.getStartRental().getTimeInMillis()));
			preparedStatement.setTimestamp(i++, new Timestamp(reservation.getEndRental().getTimeInMillis()));
			preparedStatement.setInt(i++, reservation.getBikesToRent());
			preparedStatement.setLong(i++, reservation.getBikeModelId());
			preparedStatement.setTimestamp(i++, new Timestamp(reservation.getCreationDate().getTimeInMillis()));
			preparedStatement.setFloat(i++, reservation.getScore());
			preparedStatement.setLong(i++, reservation.getRentalId());

			/* Execute query */
			int updatedRows = preparedStatement.executeUpdate();

			if (updatedRows == 0) {
				throw new InstanceNotFoundException(reservation.getRentalId(), Reservation.class.getName());
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void remove(Connection connection, Long rentalId) throws InstanceNotFoundException {
		/* Create queryString */
		String queryString = "DELETE FROM Reservation WHERE rentalId = ?";

		try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

			/* Fill preparedStatement */
			int i = 1;
			preparedStatement.setLong(i++, rentalId);

			/* Execute query */
			int removedRows = preparedStatement.executeUpdate();

			if (removedRows == 0) {
				throw new InstanceNotFoundException(rentalId, Reservation.class.getName());
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<Reservation> findByUser(Connection connection, String userEmail) {
		/* Create "queryString" */
		String queryString = "SELECT rentalId, creditCard, startRental, endRental, "
				+ "bikesToRent, bikeModelId, creationDate, score" + " FROM Reservation WHERE userEmail = ?";

		try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
			int n = 1;
			preparedStatement.setString(n++, userEmail);

			/* Execute query */
			ResultSet resultSet = preparedStatement.executeQuery();

			/* Read Reservation */
			List<Reservation> reservation = new ArrayList<Reservation>();

			while (resultSet.next()) {
				int i = 1;
				Long rentalId = new Long(resultSet.getLong(i++));
				String creditCard = resultSet.getString(i++);
				Calendar startRental = Calendar.getInstance();
				startRental.setTime(resultSet.getTimestamp(i++));
				Calendar endRental = Calendar.getInstance();
				endRental.setTime(resultSet.getTimestamp(i++));
				int bikesToRent = resultSet.getInt(i++);
				Long bikeModelId = resultSet.getLong(i++);
				Calendar creationDate = Calendar.getInstance();
				creationDate.setTime(resultSet.getTimestamp(i++));
				float score = resultSet.getFloat(i++);
				Reservation foundReservation = new Reservation(rentalId, userEmail, creditCard, startRental, endRental,
						bikesToRent, bikeModelId, creationDate);
				foundReservation.setScore(score);
				reservation.add(foundReservation);
			}

			/* Return reservations. */
			return reservation;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean exists(Connection connection, Long bikeModelId) {
		/* Create queryString */
		String queryString = "SELECT * FROM Reservation WHERE bikeModelId = ?";
		try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
			int n = 1;
			preparedStatement.setLong(n++, bikeModelId);

			/* Execute query */
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next())
				return true;
			return false;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}