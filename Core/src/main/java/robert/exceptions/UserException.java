package robert.exceptions;

public class UserException extends Exception {

	public UserException() {
		super("Password does not match the pattern.");
	}

	public UserException(String email, String msg) {
		super("User " + email + " " + msg);
	}

	public UserException(String email) {
		super("Password for user " + email + " does not match.");
	}
}
