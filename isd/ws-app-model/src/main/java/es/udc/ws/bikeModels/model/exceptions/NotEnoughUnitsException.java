package es.udc.ws.bikeModels.model.exceptions;

@SuppressWarnings("serial")
public class NotEnoughUnitsException extends Exception {

	private int totalUnits;
	private int bikesToRent;

	public NotEnoughUnitsException(int totalUnits, int bikesToRent) {

		super("Not enough units in bike model." + "There are only " + totalUnits + " units remaining but " + bikesToRent
				+ " were requested");
		this.totalUnits = totalUnits;
		this.bikesToRent = bikesToRent;
	}

	public int getTotalUnits() {
		return totalUnits;
	}

	public int getBikesToRent() {
		return bikesToRent;
	}
}