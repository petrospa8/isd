package es.udc.ws.bikeModels.model.reservation;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class SqlReservationDAOFactory {

	private final static String CLASS_NAME_PARAMETER = "SqlReservationDAOFactory.className";
	private static SqlReservationDAO dao = null;

	private SqlReservationDAOFactory() {

	}

	@SuppressWarnings("rawtypes")
	private static SqlReservationDAO getInstance() {
		try {
			String daoClassName = ConfigurationParametersManager.getParameter(CLASS_NAME_PARAMETER);
			Class daoClass = Class.forName(daoClassName);
			return (SqlReservationDAO) daoClass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public synchronized static SqlReservationDAO getDao() {

		if (dao == null) {
			dao = getInstance();
		}
		return dao;
	}
}
