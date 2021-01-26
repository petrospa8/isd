package es.udc.ws.bikeModels.model.bikeModel;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

/**
 * A factory to get <code>SqlBikeModelDao</code> objects.
 * <p>
 * Required configuration parameters:
 * <ul>
 * <li><code>SqlBikeModelDaoFactory.className</code>: it must specify the full
 * class name of the class implementing <code>SqlBikeModelDao</code>.</li>
 * </ul>
 */
public class SqlBikeModelDaoFactory {

	private final static String CLASS_NAME_PARAMETER = "SqlBikeModelDaoFactory.className";
	private static SqlBikeModelDao dao = null;

	private SqlBikeModelDaoFactory() {
	}

	@SuppressWarnings("rawtypes")
	private static SqlBikeModelDao getInstance() {
		try {
			String daoClassName = ConfigurationParametersManager.getParameter(CLASS_NAME_PARAMETER);
			Class daoClass = Class.forName(daoClassName);
			return (SqlBikeModelDao) daoClass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	public synchronized static SqlBikeModelDao getDao() {

		if (dao == null) {
			dao = getInstance();
		}
		return dao;

	}
}