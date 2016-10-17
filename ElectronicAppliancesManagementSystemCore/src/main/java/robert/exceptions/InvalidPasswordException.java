package robert.exceptions;

public class InvalidPasswordException extends Exception {

	public InvalidPasswordException() {
		super("Password does not match the pattern.");
	}

	public InvalidPasswordException(String email) {
		super("Password for user " + email + " does not match.");
	}
}
