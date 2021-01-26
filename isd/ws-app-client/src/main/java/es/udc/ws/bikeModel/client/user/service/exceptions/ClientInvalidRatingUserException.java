package es.udc.ws.bikeModel.client.user.service.exceptions;

@SuppressWarnings("serial")
public class ClientInvalidRatingUserException extends Exception {

	private String supposedUser;
	private String wrongUser;

	public ClientInvalidRatingUserException(String supposedUser, String wrongUser) {

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
