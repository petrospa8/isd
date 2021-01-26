package es.udc.ws.bikeModel.client.admin.service.rest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.udc.ws.bikeModel.client.admin.service.ClientBikeModelService;
import es.udc.ws.bikeModel.client.admin.service.dto.ClientBikeModelDto;
import es.udc.ws.bikeModel.client.admin.service.exceptions.*;
import es.udc.ws.bikeModel.client.admin.service.rest.json.JsonClientBikeModelDtoConversor;
import es.udc.ws.bikeModel.client.admin.service.rest.json.JsonClientExceptionConversor;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

public class RestClientBikeModelService implements ClientBikeModelService {

	private final static String ENDPOINT_ADDRESS_PARAMETER = "RestClientBikeModelService.endpointAddress";
	private String endpointAddress;

	@Override
	public Long addBikeModel(ClientBikeModelDto bikeModel) throws Exception {

		try {
			HttpResponse response = Request.Post(getEndpointAddress() + "bikeModel")
					.bodyStream(toInputStream(bikeModel), ContentType.create("application/json")).execute()
					.returnResponse();
			validateStatusCode(HttpStatus.SC_CREATED, response);
			return JsonClientBikeModelDtoConversor.toClientBikeModelDto(response.getEntity().getContent())
					.getBikeModelId();

		} catch (InputValidationException | ClientInvalidRentableFromException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void updateBikeModel(ClientBikeModelDto bikeModel) throws Exception {

		try {

			HttpResponse response = Request.Put(getEndpointAddress() + "bikeModel/" + bikeModel.getBikeModelId())
					.bodyStream(toInputStream(bikeModel), ContentType.create("application/json")).execute()
					.returnResponse();

			validateStatusCode(HttpStatus.SC_NO_CONTENT, response);
		} catch (InputValidationException | ClientInvalidRentableFromException | ClientInvalidNewRentableFromException
				| InstanceNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public ClientBikeModelDto findBike(Long bikeModelId) throws InstanceNotFoundException {
		try {
			String stringId = new String(bikeModelId.toString());
			HttpResponse response = Request
					.Get(getEndpointAddress() + "bikeModel?bikeModelId=" + URLEncoder.encode(stringId, "UTF-8"))
					.execute().returnResponse();

			validateStatusCode(HttpStatus.SC_OK, response);

			return JsonClientBikeModelDtoConversor.toClientBikeModelDto(response.getEntity().getContent());

		} catch (InstanceNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private synchronized String getEndpointAddress() {
		if (endpointAddress == null) {
			endpointAddress = ConfigurationParametersManager.getParameter(ENDPOINT_ADDRESS_PARAMETER);
		}
		return endpointAddress;
	}

	private InputStream toInputStream(ClientBikeModelDto bikeModel) {

		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ObjectMapper objectMapper = ObjectMapperFactory.instance();
			objectMapper.writer(new DefaultPrettyPrinter()).writeValue(outputStream,
					JsonClientBikeModelDtoConversor.toObjectNode(bikeModel));
			return new ByteArrayInputStream(outputStream.toByteArray());

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	private void validateStatusCode(int successCode, HttpResponse response)
			throws InstanceNotFoundException, InputValidationException, ParsingException,
			ClientInvalidRentableFromException, ClientInvalidNewRentableFromException {

		try {
			int statusCode = response.getStatusLine().getStatusCode();
			/* Success? */
			if (statusCode == successCode) {
				return;
			}

			/* Handler error. */
			InputStream ex = response.getEntity().getContent();
			ObjectMapper objectMapper = ObjectMapperFactory.instance();
			JsonNode rootNode = objectMapper.readTree(ex);
			String exceptionString = rootNode.fieldNames().next();
			switch (exceptionString) {
			case "instanceNotFoundException":
				throw JsonClientExceptionConversor.fromInstanceNotFoundException(response.getEntity().getContent());
			case "inputValidationException":
				throw JsonClientExceptionConversor.fromInputValidationException(response.getEntity().getContent());
			case "invalidRentableFromException":
				throw JsonClientExceptionConversor.fromInvalidRentableFromException(response.getEntity().getContent());
			case "invalidNewRentableFromException":
				throw JsonClientExceptionConversor
						.fromInvalidNewRentableFromException(response.getEntity().getContent());
			default:
				throw new RuntimeException("HTTP error; status code = " + statusCode);

			}

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

}
