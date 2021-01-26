package es.udc.ws.bikeModels.model.reservation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class Jdbc3CcSqlReservationDAO extends AbstractSqlReservationDAO {

	@Override
	public Reservation create(Connection connection, Reservation reservation) {

		/* Create "queryString" */
		String queryString = "INSERT INTO Reservation" + " (userEmail, creditCard, startRental, endRental, "
				+ "bikesToRent, bikeModelId, creationDate, score) " + " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

		try (PreparedStatement preparedStatement = connection.prepareStatement(queryString,
				Statement.RETURN_GENERATED_KEYS)) {

			/* Fill "preparedStatement" */
			int i = 1;
			preparedStatement.setString(i++, reservation.getUserEmail());
			preparedStatement.setString(i++, reservation.getCreditCard());
			Timestamp sDate = reservation.getStartRental() != null
					? new Timestamp(reservation.getStartRental().getTime().getTime())
					: null;
			preparedStatement.setTimestamp(i++, sDate);
			Timestamp eDate = reservation.getEndRental() != null
					? new Timestamp(reservation.getEndRental().getTime().getTime())
					: null;
			preparedStatement.setTimestamp(i++, eDate);
			preparedStatement.setInt(i++, reservation.getBikesToRent());
			preparedStatement.setLong(i++, reservation.getBikeModelId());
			Timestamp cDate = reservation.getCreationDate() != null
					? new Timestamp(reservation.getCreationDate().getTime().getTime())
					: null;
			preparedStatement.setTimestamp(i++, cDate);
			preparedStatement.setFloat(i++, reservation.getScore());

			/* Execute query */
			preparedStatement.executeUpdate();

			/* Get generated identifier */
			ResultSet resultSet = preparedStatement.getGeneratedKeys();

			if (!resultSet.next()) {
				throw new SQLException("JDBC driver did not return generated key.");
			}
			Long rentalId = resultSet.getLong(1);

			/* Return reservation. */
			return new Reservation(rentalId, reservation.getUserEmail(), reservation.getCreditCard(),
					reservation.getStartRental(), reservation.getEndRental(), reservation.getBikesToRent(),
					reservation.getBikeModelId(), reservation.getCreationDate());

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
