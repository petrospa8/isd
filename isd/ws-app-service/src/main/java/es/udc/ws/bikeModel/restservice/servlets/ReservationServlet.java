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

import es.udc.ws.bikeModel.dto.ServiceReservationDto;
import es.udc.ws.bikeModel.restservice.json.JsonServiceExceptionConversor;
import es.udc.ws.bikeModel.restservice.json.JsonServiceReservationDtoConversor;
import es.udc.ws.bikeModel.serviceutil.ReservationToReservationDtoConversor;
import es.udc.ws.bikeModel.serviceutil.DateConversor;
import es.udc.ws.bikeModels.model.bikeModelService.BikeModelServiceFactory;
import es.udc.ws.bikeModels.model.exceptions.*;
import es.udc.ws.bikeModels.model.reservation.Reservation;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.servlet.ServletUtils;

@SuppressWarnings("serial")
public class ReservationServlet extends HttpServlet {

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
		String bikeModelIdParameter = req.getParameter("bikeModelId");
		if (bikeModelIdParameter == null) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
					JsonServiceExceptionConversor.toInputValidationException(
							new InputValidationException("Invalid Request: " + "parameter 'bikeModelId' is mandatory")),
					null);
			return;
		}
		Long bikeModelId;
		try {
			bikeModelId = Long.valueOf(bikeModelIdParameter);
		} catch (NumberFormatException ex) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
					JsonServiceExceptionConversor.toInputValidationException(new InputValidationException(
							"Invalid Request: " + "parameter 'bikeModelId' is invalid '" + bikeModelIdParameter + "'")),
					null);

			return;
		}
		String userEmail = req.getParameter("userEmail");
		if (userEmail == null) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
					JsonServiceExceptionConversor.toInputValidationException(
							new InputValidationException("Invalid Request: " + "parameter 'userEmail' is mandatory")),
					null);
			return;
		}
		String creditCard = req.getParameter("creditCard");
		if (creditCard == null) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
					JsonServiceExceptionConversor.toInputValidationException(
							new InputValidationException("Invalid Request: " + "parameter 'creditCard' is mandatory")),
					null);

			return;
		}
		int bikesToRent = Integer.parseInt(req.getParameter("bikesToRent"));
		if (bikesToRent <= 0) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
					JsonServiceExceptionConversor.toInputValidationException(new InputValidationException(
							"Invalid Request: " + "parameter 'bikesToRent' is mandatory" + "and must be above zero.")),
					null);
			return;
		}
		String startRentalString = req.getParameter("startRental");
		if (startRentalString == null) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
					JsonServiceExceptionConversor.toInputValidationException(
							new InputValidationException("Invalid Request: " + "parameter 'startRental' is mandatory")),
					null);

			return;
		}
		String endRentalString = req.getParameter("endRental");
		if (endRentalString == null) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
					JsonServiceExceptionConversor.toInputValidationException(
							new InputValidationException("Invalid Request: " + "parameter 'endRental' is mandatory")),
					null);

			return;
		}
		Reservation reservation = null;
		try {

			Calendar startRental = DateConversor.getCalendar(startRentalString);
			Calendar endRental = DateConversor.getCalendar(endRentalString);
			reservation = BikeModelServiceFactory.getService().rentBike(bikeModelId, userEmail, creditCard, bikesToRent,
					startRental, endRental);
		} catch (InstanceNotFoundException ex) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
					JsonServiceExceptionConversor.toInstanceNotFoundException(ex), null);
			return;
		} catch (InputValidationException ex) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
					JsonServiceExceptionConversor.toInputValidationException(ex), null);
			return;
		} catch (InvalidRentalTimeException ex) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
					JsonServiceExceptionConversor.toInvalidRentalTimeException(ex), null);
			return;
		} catch (InvalidStartRentalException ex) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
					JsonServiceExceptionConversor.toInvalidStartRentalException(ex), null);
			return;
		} catch (NotEnoughUnitsException ex) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
					JsonServiceExceptionConversor.toNotEnoughUnitsException(ex), null);
			return;
		}
		ServiceReservationDto reservationDto = ReservationToReservationDtoConversor.toReservationDto(reservation);

		String reservationURL = ServletUtils.normalizePath(req.getRequestURI().toString()) + "/"
				+ reservation.getRentalId().toString();

		Map<String, String> headers = new HashMap<>(1);
		headers.put("Location", reservationURL);

		ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CREATED,
				JsonServiceReservationDtoConversor.toObjectNode(reservationDto), headers);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = ServletUtils.normalizePath(req.getPathInfo());
		if (path == null || path.length() == 0) {
			List<Reservation> reservations;

			String userEmail = req.getParameter("userEmail");
			reservations = BikeModelServiceFactory.getService().findReservations(userEmail);
			List<ServiceReservationDto> reservationDtos = ReservationToReservationDtoConversor
					.toReservationDtos(reservations);
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
					JsonServiceReservationDtoConversor.toArrayNode(reservationDtos), null);
		} else {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
					JsonServiceExceptionConversor.toInputValidationException(
							new InputValidationException("Invalid Request: " + "invalid path " + path)),
					null);
		}
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String path = ServletUtils.normalizePath(req.getPathInfo());
		if (path == null || path.length() == 0) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
					JsonServiceExceptionConversor.toInputValidationException(
							new InputValidationException("Invalid Request: " + "invalid rental id")),
					null);
			return;
		}
		String rentalIdAsString = path.substring(1);
		Long rentalId;
		try {
			rentalId = Long.valueOf(rentalIdAsString);
		} catch (NumberFormatException ex) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
					JsonServiceExceptionConversor.toInputValidationException(new InputValidationException(
							"Invalid Request: " + "invalid rental id (" + rentalIdAsString + ")")),
					null);
			return;
		}
		String userEmail = req.getParameter("userEmail");
		if (userEmail == null) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
					JsonServiceExceptionConversor.toInputValidationException(
							new InputValidationException("Invalid Request: " + "parameter 'userEmail' is mandatory")),
					null);
			return;
		}
		float score = Float.valueOf(req.getParameter("score"));
		if (score <= 0 || score >= 10) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
					JsonServiceExceptionConversor.toInputValidationException(
							new InputValidationException("Invalid Request: " + "parameter 'avgScore' is mandatory"
									+ "and must be above zero and below 10.")),
					null);
			return;
		}
		try {
			BikeModelServiceFactory.getService().rateReservation(rentalId, userEmail, score);
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
		} catch (AlreadyRatedException ex) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
					JsonServiceExceptionConversor.toAlreadyRatedException(ex), null);
			return;
		} catch (InvalidRatingUserException ex) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
					JsonServiceExceptionConversor.toInvalidRatingUserException(ex), null);
			return;
		} catch (InvalidRentableFromException ex) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
					JsonServiceExceptionConversor.toInvalidRentableFromException(ex), null);
			return;
		} catch (InvalidRatingDateException ex) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
					JsonServiceExceptionConversor.toInvalidRatingDateException(ex), null);
			return;
		}
		ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NO_CONTENT, null, null);
	}
}
