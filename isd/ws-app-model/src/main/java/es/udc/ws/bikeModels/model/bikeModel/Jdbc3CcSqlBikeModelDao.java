package es.udc.ws.bikeModels.model.bikeModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;

public class Jdbc3CcSqlBikeModelDao extends AbstractSqlBikeModelDao {

	@Override
	public BikeModel create(Connection connection, BikeModel bikeModel) {

		/* Create "queryString". */
		String queryString = "INSERT INTO BikeModel" + " (name, description, rentableFrom, totalUnits, pricePerDay, "
				+ "timesRated, avgScore, creationDate)" + " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

		try (PreparedStatement preparedStatement = connection.prepareStatement(queryString,
				Statement.RETURN_GENERATED_KEYS)) {

			/* Fill "preparedStatement". */
			int i = 1;
			preparedStatement.setString(i++, bikeModel.getName());
			preparedStatement.setString(i++, bikeModel.getDescription());
			Timestamp rentableFrom = bikeModel.getRentableFrom() != null
					? new Timestamp(bikeModel.getRentableFrom().getTime().getTime())
					: null;
			preparedStatement.setTimestamp(i++, rentableFrom);
			preparedStatement.setInt(i++, bikeModel.getTotalUnits());
			preparedStatement.setFloat(i++, bikeModel.getPricePerDay());
			preparedStatement.setInt(i++, bikeModel.getTimesRated());
			preparedStatement.setFloat(i++, bikeModel.getAvgScore());
			Calendar creationDate = Calendar.getInstance();
			Timestamp creationStamp = new Timestamp(creationDate.getTime().getTime());
			preparedStatement.setTimestamp(i++, creationStamp);

			/* Execute query. */
			preparedStatement.executeUpdate();

			/* Get generated identifier. */
			ResultSet resultSet = preparedStatement.getGeneratedKeys();

			if (!resultSet.next()) {
				throw new SQLException("JDBC driver did not return generated key.");
			}
			Long bikeModelId = resultSet.getLong(1);
			/* Return bikeModel. */
			return new BikeModel(bikeModelId, bikeModel.getName(), bikeModel.getDescription(),
					bikeModel.getRentableFrom(), bikeModel.getTotalUnits(), bikeModel.getPricePerDay(),
					bikeModel.getCreationDate());

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}
}
