package robert.web.svc.rest.ctrl.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import robert.web.svc.rest.responses.dto.BasicDTO;
import robert.web.svc.rest.responses.dto.UserCredentialsDTO;

public interface RegisterAndLoginCtrl extends BasicParams {

	String REGISTER_URL = "/register/";

	String LOGIN_URL = "/login/";

	String LOGOUT_URL = "/logout/";

	BasicDTO registerNewUser(UserCredentialsDTO userData);

	BasicDTO loginUser(UserCredentialsDTO userData,
						 HttpServletRequest request,
						 HttpServletResponse response);

	void logoutUser(HttpSession session);

}
