package robert.exception;

public class InvalidEmailPatternException extends Exception {
    public InvalidEmailPatternException(String invalidEmail) {
        super("Invalid email regex exception. Found: " + invalidEmail);
    }
}
