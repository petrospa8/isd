package es.udc.ws.bikeModel.restservice.servlets;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.udc.ws.bikeModel.dto.ServiceBikeModelDto;
import es.udc.ws.bikeModel.restservice.json.JsonServiceBikeModelDtoConversor;
import es.udc.ws.bikeModel.restservice.json.JsonServiceExceptionConversor;
import es.udc.ws.bikeModel.serviceutil.BikeModelToBikeModelDtoConversor;
import es.udc.ws.bikeModel.serviceutil.DateConversor;
import es.udc.ws.bikeModels.model.bikeModel.BikeModel;
import es.udc.ws.bikeModels.model.bikeModelService.BikeModelServiceFactory;
import es.udc.ws.bikeModels.model.exceptions.InvalidNewRentableFromException;
import es.udc.ws.bikeModels.model.exceptions.InvalidRentableFromException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.exceptions.ParsingException;
import es.udc.ws.util.servlet.ServletUtils;

@SuppressWarnings("serial")
public class BikeModelServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = ServletUtils.normalizePath(req.getPathInfo());
		if (path != null && path.length() > 0) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
					JsonServiceExceptionConversor.toInputValidationException(
							new InputValidationException("Invalid Request: " + "invalid path " + path)),
					null);
			return;
		}
		ServiceBikeModelDto bikeModelDto;
		try {
			bikeModelDto = JsonServiceBikeModelDtoConversor.toServiceBikeModelDto(req.getInputStream());
		} catch (ParsingException ex) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST, JsonServiceExceptionConversor
					.toInputValidationException(new InputValidationException(ex.getMessage())), null);
			return;
		}
		BikeModel bikeModel = BikeModelToBikeModelDtoConversor.toBikeModel(bikeModelDto);
		try {
			bikeModel = BikeModelServiceFactory.getService().addBikeModel(bikeModel);
		} catch (InputValidationException ex) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
					JsonServiceExceptionConversor.toInputValidationException(ex), null);
			return;
		} catch (InvalidRentableFromException ex) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
					JsonServiceExceptionConversor.toInvalidRentableFromException(ex), null);
			return;
		}
		bikeModelDto = BikeModelToBikeModelDtoConversor.toBikeModelDto(bikeModel);

		String bikeModelURL = ServletUtils.normalizePath(req.getRequestURL().toString()) + "/"
				+ bikeModel.getBikeModelId();
		Map<String, String> headers = new HashMap<>(1);
		headers.put("Location", bikeModelURL);

		ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CREATED,
				JsonServiceBikeModelDtoConversor.toObjectNode(bikeModelDto), headers);
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String path = ServletUtils.normalizePath(req.getPathInfo());
		if (path == null || path.length() == 0) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
					JsonServiceExceptionConversor.toInputValidationException(
							new InputValidationException("Invalid Request: " + "invalid bike model id")),
					null);
			return;
		}
		String bikeModelIdAsString = path.substring(1);
		Long bikeModelId;
		try {
			bikeModelId = Long.valueOf(bikeModelIdAsString);
		} catch (NumberFormatException ex) {
			ServletUtils
					.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
							JsonServiceExceptionConversor.toInputValidationException(new InputValidationException(
									"Invalid Request: " + "invalid bike model id (" + bikeModelIdAsString + ")")),
							null);
			return;
		}

		ServiceBikeModelDto bikeModelDto;
		try {
			bikeModelDto = JsonServiceBikeModelDtoConversor.toServiceBikeModelDto(req.getInputStream());
		} catch (ParsingException ex) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST, JsonServiceExceptionConversor
					.toInputValidationException(new InputValidationException(ex.getMessage())), null);
			return;
		}
		if (!bikeModelId.equals(bikeModelDto.getBikeModelId())) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
					JsonServiceExceptionConversor.toInputValidationException(
							new InputValidationException("Invalid Request: " + "invalid bike model id")),
					null);
			return;
		}
		BikeModel bikeModel = BikeModelToBikeModelDtoConversor.toBikeModel(bikeModelDto);
		try {
			BikeModelServiceFactory.getService().updateBikeModel(bikeModel);
		} catch (InputValidationException ex) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
					JsonServiceExceptionConversor.toInputValidationException(ex), null);
			return;
		} catch (InstanceNotFoundException ex) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
					JsonServiceExceptionConversor.toInstanceNotFoundException(ex), null);
			return;
		} catch (InvalidNewRentableFromException ex) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
					JsonServiceExceptionConversor.toInvalidNewRentableFromException(ex), null);
			return;
		} catch (InvalidRentableFromException ex) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
					JsonServiceExceptionConversor.toInvalidRentableFromException(ex), null);
			return;
		}
		ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NO_CONTENT, null, null);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = ServletUtils.normalizePath(req.getPathInfo());
		if (path == null || path.length() == 0) {
			String bikeModelIdString = req.getParameter("bikeModelId");
			if (bikeModelIdString == null) {
				String keyWords = req.getParameter("keyWords");
				String rentableString = req.getParameter("rentableFrom");
				System.out.println("rentableString:" + rentableString + "1");
				if (rentableString == null || rentableString.equals(" ")) {
					ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
							JsonServiceExceptionConversor.toInputValidationException(new InputValidationException(
									"Invalid Request: " + "parameter 'rentableFrom' is mandatory")),
							null);
					return;
				}
				Calendar rentableFrom = DateConversor.getCalendar(rentableString);

				List<BikeModel> bikeModels = BikeModelServiceFactory.getService().findByKeyWords(keyWords,
						rentableFrom);
				List<ServiceBikeModelDto> bikeModelDtos = BikeModelToBikeModelDtoConversor.toBikeModelDtos(bikeModels);
				ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
						JsonServiceBikeModelDtoConversor.toArrayNode(bikeModelDtos), null);
			} else {
				try {
					BikeModel bikeModel = BikeModelServiceFactory.getService()
							.findModel(Long.parseLong(bikeModelIdString));
					ServiceBikeModelDto bikeModelDto = BikeModelToBikeModelDtoConversor.toBikeModelDto(bikeModel);
					ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
							JsonServiceBikeModelDtoConversor.toObjectNode(bikeModelDto), null);
				} catch (InstanceNotFoundException ex) {
					ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
							JsonServiceExceptionConversor.toInstanceNotFoundException(ex), null);
					return;
				}
			}
		} else {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
					JsonServiceExceptionConversor.toInputValidationException(
							new InputValidationException("Invalid Request: " + "invalid path " + path)),
					null);
		}
	}

}
