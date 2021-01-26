package es.udc.ws.bikeModel.client.user.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.List;

import es.udc.ws.bikeModel.client.user.service.ClientBikeModelService;
import es.udc.ws.bikeModel.client.user.service.ClientBikeModelServiceFactory;
import es.udc.ws.bikeModel.client.user.service.dto.ClientBikeModelDto;
import es.udc.ws.bikeModel.client.user.service.dto.ClientReservationDto;
import es.udc.ws.bikeModel.client.user.service.exceptions.*;
import es.udc.ws.bikeModel.client.user.utils.*;
import es.udc.ws.util.exceptions.*;

public class BikeModelServiceClient {

	public static void main(String[] args) {

		if (args.length == 0) {
			printUsageAndExit();
		}
		ClientBikeModelService clientBikeModelService = ClientBikeModelServiceFactory.getService();
		if ("-findBikes".equalsIgnoreCase(args[0])) {
			validateArgs(args, 3, new int[] {}, new int[] {});

			try {
				validateRentableFrom(args[2]);
			} catch (ParseException e) {
				System.out.println(
						new InputValidationException("Invalid Request: " + "parameter 'rentableFrom' is mandatory")
								.getMessage());
				return;
			}

			Calendar rentableFrom = DateConversor.getCalendar(args[2]);
			// [findBikes] BikeModelServiceClient -findBikes <keyWords> <rentableFrom>

			try {
				List<ClientBikeModelDto> bikeModels = clientBikeModelService.findByKeyWords(args[1], rentableFrom);
				System.out.println("Found " + bikeModels.size() + " bikeModel(s) with keywords '" + args[1] + "'");
				for (int i = 0; i < bikeModels.size(); i++) {
					ClientBikeModelDto bikeModelDto = bikeModels.get(i);
					String printableAverage = (bikeModelDto.getTimesRated() > 0)
							? String.valueOf(bikeModelDto.getAvgScore())
							: "N/A";
					System.out.println("bikeModelId: " + bikeModelDto.getBikeModelId() + ", averageScore: "
							+ printableAverage + ", timesRated: " + bikeModelDto.getTimesRated());
				}

			} catch (Exception ex) {
				ex.printStackTrace(System.err);
			}

		} else if ("-reserve".equalsIgnoreCase(args[0])) {
			validateArgs(args, 7, new int[] { 4 }, new int[] {});
			validateDate(args[5]);
			validateDate(args[6]);

			// [reserve] BikeModelServiceClient -reserve <bikeModelId> <userEmail>
			// <creditCard>
			// <bikesRented> <startRental> <endRental>

			Calendar startRental = DateConversor.getCalendar(args[5]);
			Calendar endRental = DateConversor.getCalendar(args[6]);

			ClientReservationDto reservation;
			try {
				reservation = clientBikeModelService.rentBike(Long.parseLong(args[1]), args[2], args[3],
						Integer.parseInt(args[4]), startRental, endRental);
				System.out.println(
						"bikeModelId " + args[1] + " rented sucessfully (rentalId=" + reservation.getRentalId() + ")");

			} catch (InstanceNotFoundException | InputValidationException | ClientInvalidRentalTimeException
					| ClientInvalidStartRentalException | ClientNotEnoughUnitsException ex) {
				System.out.println(ex.getMessage());
			} catch (Exception ex) {
				ex.printStackTrace(System.err);
			}

		} else if ("-rateReservation".equalsIgnoreCase(args[0])) {

			validateArgs(args, 4, new int[] {}, new int[] { 3 });
			// [rateReservation] BikeModelServiceClient -rateReservation <rentalId>
			// <userEmail> <avgScore>

			try {
				clientBikeModelService.rateReservation(Long.parseLong(args[1]), args[2], Float.valueOf(args[3]));

				System.out.println("reservationId " + args[1] + " rated sucessfully with a score of " + args[3]);

			} catch (InstanceNotFoundException | InputValidationException | ClientAlreadyRatedException
					| ClientInvalidRatingUserException | ClientInvalidRatingDateException ex) {
				System.out.println(ex.getMessage());
			} catch (Exception ex) {
				ex.printStackTrace(System.err);
			}

		} else if ("-findReservations".equalsIgnoreCase(args[0])) {

			validateArgs(args, 2, new int[] {}, new int[] {});

			try {
				List<ClientReservationDto> reservations = clientBikeModelService.findReservations(args[1]);
				System.out.println("The reservations for the user " + args[1] + " are :");
				for (int i = 0; i < reservations.size(); i++) {
					ClientReservationDto reservation = reservations.get(i);
					long daysBetween = ChronoUnit.DAYS.between(reservation.getStartRental().toInstant(),
							reservation.getEndRental().toInstant()) + 1;
					String printableScore = (reservation.getScore() >= 0) ? String.valueOf(reservation.getScore())
							: "N/A";
					System.out.println("reservationId: " + reservation.getRentalId() + ". Score: " + printableScore
							+ ". Start of rental: " + DateConversor.getDate(reservation.getStartRental())
							+ ". Duration: " + daysBetween);
				}
			} catch (Exception ex) {
				ex.printStackTrace(System.err);
			}
		}

	}

	private static void validateDate(String dateString) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			sdf.parse(dateString);
		} catch (ParseException e) {
			System.out.println(e.getMessage());
		}
	}

	private static void validateRentableFrom(String dateString) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.parse(dateString);
	}

	private static void validateArgs(String[] args, int expectedArgs, int[] numericArguments, int[] floatArguments) {
		if (expectedArgs != args.length) {
			printUsageAndExit();
		}
		for (int i = 0; i < numericArguments.length; i++) {
			int position = numericArguments[i];
			try {
				Integer.parseInt(args[position]);
			} catch (NumberFormatException n) {
				printUsageAndExit();
			}
		}
		for (int i = 0; i < floatArguments.length; i++) {
			int position = floatArguments[i];
			try {
				Float.parseFloat(args[position]);
			} catch (NumberFormatException n) {
				printUsageAndExit();
			}
		}
	}

	public static void printUsageAndExit() {
		printUsage();
		System.exit(-1);
	}

	public static void printUsage() {
		System.err.println("Usage:\n" + "    [findBikes] BikeModelServiceClient -findBikes <keyWords> <rentableFrom>\n"
				+ "    [rateReservation] BikeModelServiceClient -rateReservation <rentalId> <userEmail> <score>\n"
				+ "    [reserve] BikeModelServiceClient -reserve <bikeModelId> <userEmail> <creditCard> <bikesRented> <startRental> <endRental>\n"
				+ "    [findReservations] BikeModelServiceClient -findReservations <userEmail>\n");
	}
}
