package robert.web.svc.rest.api;

import org.springframework.http.HttpStatus;

public interface RegisterAndLoginCtrl {

	String EMAIL = "email";

	String PASSWORD = "password";

	String NAME = "name";

	String SURNAME = "surname";

	String REGISTER_URL = "/register/{" + EMAIL + "}/{" + PASSWORD + "}/{" + NAME + "}/{" + SURNAME + "}";

	HttpStatus registerNewUser(String email, String password, String name, String surname);
}
