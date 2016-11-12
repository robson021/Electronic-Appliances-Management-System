package robert.enums;

import org.assertj.core.util.Lists;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import static robert.web.svc.rest.ctrl.api.AdminPanelCtrl.ADMIN_PREFIX;

public interface Validation {

	Pattern VALID_EMAIL_ADDRESS_REGEX = //
			Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

	Pattern VALID_PASSWORD_REGEX = //
			Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$");

	String ADMIN_URI = ADMIN_PREFIX + "**";

	List<String> NO_AUTH_URIS = Collections.unmodifiableList(    // TODO: replace with Guava?
			Lists.newArrayList("/", "/login/**", "/register/**", "/logout/**", "/guest/**", "/test/**")
	);

	List<String> NO_AUTH_FILES = Collections.unmodifiableList(
			Lists.newArrayList(".html", ".js", ".css")
	);

	short MAX_RESERVATION_TIME_IN_HOURS = 12;

	String MOCK_APPLIANCE_UNIQUE_CODE = "unique-code";

}
