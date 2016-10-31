package robert.web.svc.rest.ctrl.api;

import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface RegisterAndLoginCtrl extends BasicParams {

	String REGISTER_URL = "/register/{" + EMAIL + "}/{" + PASSWORD + "}/{" + NAME + "}/{" + SURNAME + "}/";

	String LOGIN_URL = "/login/{" + EMAIL + "}/{" + PASSWORD + "}/";

	HttpStatus registerNewUser(String email, String password, String name, String surname);

	HttpStatus loginUser(String email, String password,
						 HttpServletRequest request,
						 HttpServletResponse response);
}
