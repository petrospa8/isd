package es.udc.ws.bikeModels.model.bikeModel;

import java.sql.Connection;
import java.util.Calendar;
import java.util.List;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

public interface SqlBikeModelDao {

	public BikeModel create(Connection connection, BikeModel bikeModel);

	public BikeModel find(Connection connection, Long bikeModelId) throws InstanceNotFoundException;

	public List<BikeModel> findByKeywords(Connection connection, String keywords, Calendar fromDate);

	public void update(Connection connection, BikeModel bikeModel) throws InstanceNotFoundException;

	public void remove(Connection connection, Long bikeModelId) throws InstanceNotFoundException;
}