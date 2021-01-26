package es.udc.ws.bikeModel.client.admin.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import es.udc.ws.bikeModel.client.admin.service.ClientBikeModelService;
import es.udc.ws.bikeModel.client.admin.service.ClientBikeModelServiceFactory;
import es.udc.ws.bikeModel.client.admin.service.dto.ClientBikeModelDto;
import es.udc.ws.bikeModel.client.admin.service.exceptions.*;
import es.udc.ws.bikeModel.client.admin.utils.*;
import es.udc.ws.util.exceptions.*;

public class BikeModelServiceClient {

	public static void main(String[] args) {

		if (args.length == 0) {
			printUsageAndExit();
		}
		ClientBikeModelService clientBikeModelService = ClientBikeModelServiceFactory.getService();
		if ("-addBike".equalsIgnoreCase(args[0])) {
			validateArgs(args, 6, new int[] { 4 }, new int[] { 5 });
			validateDate(args[3]);
			Calendar rentableFrom = DateConversor.getCalendar(args[3]);

			try {
				Long bikeModelId = clientBikeModelService.addBikeModel(new ClientBikeModelDto(null, args[1], args[2],
						rentableFrom, Integer.parseInt(args[4]), Float.valueOf(args[5])));

				System.out.println("bikeModelId=" + bikeModelId + " created successfully");
			} catch (InputValidationException | ClientInvalidRentableFromException ex) {
				System.out.println(ex.getMessage());
			} catch (Exception ex) {
				ex.printStackTrace(System.err);
			}
		} else if ("-findBike".equalsIgnoreCase(args[0])) {
			validateArgs(args, 2, new int[] { 1 }, new int[] {});

			try {
				ClientBikeModelDto bikeModelDto = clientBikeModelService.findBike(Long.parseLong(args[1]));
				String printableAverage = (bikeModelDto.getTimesRated() > 0)
						? String.valueOf(bikeModelDto.getAvgScore())
						: "N/A";
				System.out.println("Data: name='" + bikeModelDto.getName() + "'; averageScore=" + printableAverage
						+ "; timesRated=" + bikeModelDto.getTimesRated() + "; description: '"
						+ bikeModelDto.getDescription() + "'; totalUnits=" + bikeModelDto.getTotalUnits()
						+ "; pricePerDay=" + bikeModelDto.getPricePerDay());
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (InstanceNotFoundException e) {
				System.out.println(e.getMessage());
			}
		}

		else if ("-updateBike".equalsIgnoreCase(args[0])) {
			validateArgs(args, 7, new int[] { 5 }, new int[] { 6 });
			validateDate(args[4]);
			Calendar rentableFrom = DateConversor.getCalendar(args[4]);
			try {
				clientBikeModelService.updateBikeModel(new ClientBikeModelDto(Long.valueOf(args[1]), args[2], args[3],
						rentableFrom, Integer.parseInt(args[5]), Float.valueOf(args[6])));

				System.out.println("bikeModelId " + args[1] + " updated sucessfully");

			} catch (InputValidationException | ClientInvalidRentableFromException
					| ClientInvalidNewRentableFromException | InstanceNotFoundException ex) {
				System.out.println(ex.getMessage());
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
		System.err.println("Usage:\n"
				+ "    [addBike] BikeModelServiceClient -addBike <name> <description> <rentableFrom> <totalUnits> <pricePerDay>\n"
				+ "    [updateBike] BikeModelServiceClient -updateBike <bikeModelId> <name> <description> <rentableFrom> <totalUnits> <pricePerDay>\n"
				+ "    [findBike] BikeModelServiceClient -findBike <bikeModelId>\n");
	}
}
