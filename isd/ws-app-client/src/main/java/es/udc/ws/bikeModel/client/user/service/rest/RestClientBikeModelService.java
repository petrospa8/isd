package es.udc.ws.bikeModel.client.user.service.rest;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.udc.ws.bikeModel.client.user.service.ClientBikeModelService;
import es.udc.ws.bikeModel.client.user.service.dto.ClientBikeModelDto;
import es.udc.ws.bikeModel.client.user.service.dto.ClientReservationDto;
import es.udc.ws.bikeModel.client.user.service.exceptions.*;
import es.udc.ws.bikeModel.client.user.service.rest.json.JsonClientBikeModelDtoConversor;
import es.udc.ws.bikeModel.client.user.service.rest.json.JsonClientExceptionConversor;
import es.udc.ws.bikeModel.client.user.service.rest.json.JsonClientReservationDtoConversor;
import es.udc.ws.bikeModel.client.user.utils.*;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

public class RestClientBikeModelService implements ClientBikeModelService {

	private final static String ENDPOINT_ADDRESS_PARAMETER = "RestClientBikeModelService.endpointAddress";
	private String endpointAddress;

	@Override
	public List<ClientBikeModelDto> findByKeyWords(String keyWords, Calendar rentableFrom) {

		try {
			String encodedKeywords;
			if (keyWords.contentEquals(" ")) {
				encodedKeywords = "";
			} else {
				encodedKeywords = URLEncoder.encode(keyWords, "UTF-8");
			}
			HttpResponse response = Request
					.Get(getEndpointAddress() + "bikeModel/?keyWords=" + encodedKeywords + "&rentableFrom="
							+ URLEncoder.encode(DateConversor.getDate(rentableFrom), "UTF-8"))
					.execute().returnResponse();
			validateStatusCode(HttpStatus.SC_OK, response);

			return JsonClientBikeModelDtoConversor.toClientBikeModelDtos(response.getEntity().getContent());

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public List<ClientReservationDto> findReservations(String userEmail) {

		try {

			HttpResponse response = Request
					.Get(getEndpointAddress() + "reservation/?userEmail=" + URLEncoder.encode(userEmail, "UTF-8"))
					.execute().returnResponse();

			validateStatusCode(HttpStatus.SC_OK, response);
			return JsonClientReservationDtoConversor.toClientReservationDtos(response.getEntity().getContent());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public void rateReservation(Long rentalId, String userEmail, float score) throws Exception {

		try {
			HttpResponse response = Request.Put(getEndpointAddress() + "reservation/" + rentalId + "?userEmail="
					+ URLEncoder.encode(userEmail, "UTF-8") + "&score="
					+ URLEncoder.encode(Float.toString(score), "UTF-8")).execute().returnResponse();

			validateStatusCode(HttpStatus.SC_NO_CONTENT, response);
		} catch (InputValidationException | ClientAlreadyRatedException | ClientInvalidRatingUserException
				| ClientInvalidRatingDateException | InstanceNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public ClientReservationDto rentBike(Long bikeModelId, String userEmail, String creditCard, int bikesToRent,
			Calendar startRental, Calendar endRental) throws Exception {

		try {

			HttpResponse response = Request.Post(getEndpointAddress() + "reservation/")
					.bodyForm(Form.form().add("bikeModelId", Long.toString(bikeModelId)).add("userEmail", userEmail)
							.add("creditCard", creditCard).add("startRental", DateConversor.getDate(startRental))
							.add("endRental", DateConversor.getDate(endRental))
							.add("bikesToRent", Integer.toString(bikesToRent)).build())
					.execute().returnResponse();
			validateStatusCode(HttpStatus.SC_CREATED, response);
			return JsonClientReservationDtoConversor.toClientReservationDto(response.getEntity().getContent());

		} catch (InputValidationException | InstanceNotFoundException | ClientNotEnoughUnitsException
				| ClientInvalidRentalTimeException | ClientInvalidStartRentalException e) {
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

	private void validateStatusCode(int successCode, HttpResponse response)
			throws InstanceNotFoundException, InputValidationException, ParsingException, UnsupportedOperationException,
			ClientAlreadyRatedException, ClientInvalidRatingUserException, ClientInvalidRentalTimeException,
			ClientInvalidStartRentalException, ClientNotEnoughUnitsException, ClientInvalidRatingDateException {

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
			case "alreadyRatedException":
				throw JsonClientExceptionConversor.fromAlreadyRatedException(response.getEntity().getContent());
			case "invalidRatingUserException":
				throw JsonClientExceptionConversor.fromInvalidRatingUserException(response.getEntity().getContent());
			case "invalidRentalTimeException":
				throw JsonClientExceptionConversor.fromInvalidRentalTimeException(response.getEntity().getContent());
			case "invalidStartRentalException":
				throw JsonClientExceptionConversor.fromInvalidStartRentalException(response.getEntity().getContent());
			case "notEnoughUnitsException":
				throw JsonClientExceptionConversor.fromNotEnoughUnitsException(response.getEntity().getContent());
			case "invalidRatingDateException":
				throw JsonClientExceptionConversor.fromInvalidRatingDateException(response.getEntity().getContent());
			default:
				throw new RuntimeException("HTTP error; status code = " + statusCode);

			}

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

}
