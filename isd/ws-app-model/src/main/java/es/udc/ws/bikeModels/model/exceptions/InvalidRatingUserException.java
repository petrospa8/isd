package es.udc.ws.bikeModels.model.exceptions;

@SuppressWarnings("serial")
public class InvalidRatingUserException extends Exception {

	private String supposedUser;
	private String wrongUser;

	public InvalidRatingUserException(String supposedUser, String wrongUser) {

		super("The user should be " + supposedUser + " but is " + wrongUser + ".");

		this.supposedUser = supposedUser;
		this.wrongUser = wrongUser;
	}

	public String getSupposedUser() {
		return supposedUser;
	}

	public String getWrongUser() {
		return wrongUser;
	}
}
