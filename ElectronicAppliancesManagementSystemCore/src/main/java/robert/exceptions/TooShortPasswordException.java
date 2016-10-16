package robert.exceptions;

import robert.enums.Consts;

public class TooShortPasswordException extends Exception {
	public TooShortPasswordException(int passwdLength) {
		super("Password is too short. Expected length of: " + Consts.MIN_PASSWORD_LENGTH //
				+ " received: " + passwdLength);
	}
}
