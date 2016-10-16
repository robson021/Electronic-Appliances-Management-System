package robert.enums;

import java.util.regex.Pattern;

public interface Validation {

	short MIN_PASSWORD_LENGTH = 8;

	Pattern VALID_EMAIL_ADDRESS_REGEX = //
			Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
}
