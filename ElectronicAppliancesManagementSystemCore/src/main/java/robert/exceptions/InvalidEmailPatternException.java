package robert.exceptions;

public class InvalidEmailPatternException extends Exception {
    public InvalidEmailPatternException(String invalidEmail) {
		super("Invalid email regex exceptions. Found: " + invalidEmail);
	}
}
