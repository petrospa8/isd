package es.udc.ws.bikeModel.client.user.service.exceptions;

@SuppressWarnings("serial")
public class ClientNotEnoughUnitsException extends Exception {

	private int totalUnits;
	private int bikesToRent;

	public ClientNotEnoughUnitsException(int totalUnits, int bikesToRent) {

		super("Not enough units in bike model. " + "There are only " + totalUnits + " units remaining but "
				+ bikesToRent + " were requested");
		this.totalUnits = totalUnits;
	}

	public int getTotalUnits() {
		return totalUnits;
	}

	public int getBikesToRent() {
		return bikesToRent;
	}
}
