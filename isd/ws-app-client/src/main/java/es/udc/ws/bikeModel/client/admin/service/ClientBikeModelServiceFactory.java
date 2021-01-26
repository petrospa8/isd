package es.udc.ws.bikeModel.client.admin.service;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class ClientBikeModelServiceFactory {

	private final static String CLASS_NAME_PARAMETER = "ClientBikeModelServiceFactoryAdmin.className";
	private static Class<ClientBikeModelService> serviceClass = null;

	private ClientBikeModelServiceFactory() {
	}

	@SuppressWarnings("unchecked")
	private synchronized static Class<ClientBikeModelService> getServiceClass() {

		if (serviceClass == null) {
			try {
				String serviceClassName = ConfigurationParametersManager.getParameter(CLASS_NAME_PARAMETER);
				serviceClass = (Class<ClientBikeModelService>) Class.forName(serviceClassName);

			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return serviceClass;
	}

	public static ClientBikeModelService getService() {
		try {
			return (ClientBikeModelService) getServiceClass().newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
