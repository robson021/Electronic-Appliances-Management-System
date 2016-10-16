package robert.exceptions;

public class InvalidPasswordException extends Throwable {
	public InvalidPasswordException(String email) {
		super("Password for user " + email + " does not match.");
	}
}
