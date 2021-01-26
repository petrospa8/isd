package es.udc.ws.bikeModels.model.reservation;

import java.sql.Connection;
import java.util.List;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

public interface SqlReservationDAO {

	public Reservation create(Connection connection, Reservation reservation);

	public Reservation find(Connection connection, Long rentalId) throws InstanceNotFoundException;

	public void update(Connection connection, Reservation reservation) throws InstanceNotFoundException;

	public void remove(Connection connection, Long reservationId) throws InstanceNotFoundException;

	public List<Reservation> findByUser(Connection connection, String userEmail);

	public boolean exists(Connection connection, Long bikeModelId);
}
