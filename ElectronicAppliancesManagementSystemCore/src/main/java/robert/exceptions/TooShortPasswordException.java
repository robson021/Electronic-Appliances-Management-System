package robert.exceptions;

import robert.enums.Validation;

public class TooShortPasswordException extends Exception {
	public TooShortPasswordException(int passwdLength) {
		super("Password is too short. Expected length of: " + Validation.MIN_PASSWORD_LENGTH //
				+ " received: " + passwdLength);
	}
}
