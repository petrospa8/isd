package es.udc.ws.bikeModels.model.bikeModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import es.udc.ws.util.exceptions.InstanceNotFoundException;;

/**
 * A partial implementation of <code>SQLBikeModelDAO</code> that leaves
 * <code>create(Connection, BikeModel)</code> as abstract.
 */
public abstract class AbstractSqlBikeModelDao implements SqlBikeModelDao {

	protected AbstractSqlBikeModelDao() {
	}

	@Override
	public BikeModel find(Connection connection, Long bikeModelId) throws InstanceNotFoundException {

		/* Create "queryString". */
		String queryString = "SELECT name, description," + " rentableFrom, totalUnits, pricePerDay,"
				+ " creationDate, timesRated, avgScore FROM BikeModel WHERE bikeModelId = ?";

		try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

			/* Fill "preparedStatement". */
			int i = 1;
			preparedStatement.setLong(i++, bikeModelId.longValue());

			/* Execute query. */
			ResultSet resultSet = preparedStatement.executeQuery();

			if (!resultSet.next()) {
				throw new InstanceNotFoundException(bikeModelId, BikeModel.class.getName());
			}

			/* Get results. */
			i = 1;
			String name = resultSet.getString(i++);
			String description = resultSet.getString(i++);
			Calendar rentableFrom = Calendar.getInstance();
			rentableFrom.setTime(resultSet.getTimestamp(i++));
			int totalUnits = resultSet.getInt(i++);
			float pricePerDay = resultSet.getFloat(i++);
			Calendar creationDate = Calendar.getInstance();
			creationDate.setTime(resultSet.getTimestamp(i++));
			int timesRated = resultSet.getInt(i++);
			float avgScore = resultSet.getFloat(i++);
			/* Return bikeModel. */
			BikeModel bikeModel = new BikeModel(bikeModelId, name, description, rentableFrom, totalUnits, pricePerDay,
					creationDate);
			bikeModel.setTimesRated(timesRated);
			bikeModel.setAvgScore(avgScore);
			return bikeModel;

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public List<BikeModel> findByKeywords(Connection connection, String keywords, Calendar fromDate) {

		/* Create "queryString". */
		String[] words = keywords != null ? keywords.split(" ") : null;
		String queryString = "SELECT bikeModelId, name, description, " + " rentableFrom, totalUnits, pricePerDay,"
				+ " timesRated, avgScore, creationDate FROM BikeModel";
		if (words != null && words.length > 0) {
			queryString += " WHERE";
			for (int i = 0; i < words.length; i++) {
				if (i > 0) {
					queryString += " AND";
				}
				queryString += " LOWER(description) LIKE LOWER(?)";
			}
			if (fromDate != null)
				queryString += " AND rentableFrom <= (?)";
		}
		queryString += " ORDER BY name";
		try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

			if (words != null) {
				/* Fill "preparedStatement". */
				int i = 0;
				for (i = 0; i < words.length; i++) {
					preparedStatement.setString(i + 1, "%" + words[i] + "%");
				}
			}
			if (fromDate != null) {
				int i = words.length + 1;
				preparedStatement.setTimestamp(i++, new Timestamp(fromDate.getTime().getTime()));
			}

			/* Execute query. */
			ResultSet resultSet = preparedStatement.executeQuery();

			/* Read bikeModels. */
			List<BikeModel> bikeModels = new ArrayList<BikeModel>();

			while (resultSet.next()) {
				int i = 1;
				Long bikeModelId = new Long(resultSet.getLong(i++));
				String name = resultSet.getString(i++);
				String description = resultSet.getString(i++);
				Calendar rentableFrom = Calendar.getInstance();
				rentableFrom.setTime(resultSet.getTimestamp(i++));
				int totalUnits = resultSet.getInt(i++);
				float pricePerDay = resultSet.getFloat(i++);
				int timesRated = resultSet.getInt(i++);
				float avgScore = resultSet.getFloat(i++);
				Calendar creationDate = Calendar.getInstance();
				creationDate.setTime(resultSet.getTimestamp(i++));

				BikeModel foundBikeModel = new BikeModel(bikeModelId, name, description, rentableFrom, totalUnits,
						pricePerDay, creationDate);
				foundBikeModel.setTimesRated(timesRated);
				foundBikeModel.setAvgScore(avgScore);
				bikeModels.add(foundBikeModel);

			}

			/* Return bikeModels. */
			return bikeModels;

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public void update(Connection connection, BikeModel bikeModel) throws InstanceNotFoundException {

		/* Create "queryString". */
		String queryString = "UPDATE BikeModel" + " SET name = ?, description = ?, rentableFrom = ?, totalUnits = ?, "
				+ "pricePerDay = ?, timesRated = ?, avgScore = ? WHERE bikeModelId = ?";

		try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

			/* Fill "preparedStatement". */
			int i = 1;

			preparedStatement.setString(i++, bikeModel.getName());
			preparedStatement.setString(i++, bikeModel.getDescription());
			preparedStatement.setTimestamp(i++, new Timestamp(bikeModel.getRentableFrom().getTimeInMillis()));
			preparedStatement.setInt(i++, bikeModel.getTotalUnits());
			preparedStatement.setFloat(i++, bikeModel.getPricePerDay());
			preparedStatement.setInt(i++, bikeModel.getTimesRated());
			preparedStatement.setFloat(i++, bikeModel.getAvgScore());
			preparedStatement.setLong(i++, bikeModel.getBikeModelId());

			/* Execute query. */
			int updatedRows = preparedStatement.executeUpdate();

			if (updatedRows == 0) {
				throw new InstanceNotFoundException(bikeModel.getBikeModelId(), BikeModel.class.getName());
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public void remove(Connection connection, Long bikeModelId) throws InstanceNotFoundException {

		/* Create "queryString". */
		String queryString = "DELETE FROM BikeModel WHERE" + " bikeModelId = ?";

		try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

			/* Fill "preparedStatement". */
			int i = 1;
			preparedStatement.setLong(i++, bikeModelId);

			/* Execute query. */
			int removedRows = preparedStatement.executeUpdate();

			if (removedRows == 0) {
				throw new InstanceNotFoundException(bikeModelId, BikeModel.class.getName());
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}
}
