package es.udc.ws.bikeModels.model.bikeModelService;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class BikeModelServiceFactory {

	private final static String CLASS_NAME_PARAMETER = "BikeModelServiceFactory.className";
	private static BikeModelService service = null;

	private BikeModelServiceFactory() {
	}

	@SuppressWarnings("rawtypes")
	private static BikeModelService getInstance() {
		try {
			String serviceClassName = ConfigurationParametersManager.getParameter(CLASS_NAME_PARAMETER);
			Class serviceClass = Class.forName(serviceClassName);
			return (BikeModelService) serviceClass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public synchronized static BikeModelService getService() {

		if (service == null) {
			service = getInstance();
		}
		return service;
	}
}
