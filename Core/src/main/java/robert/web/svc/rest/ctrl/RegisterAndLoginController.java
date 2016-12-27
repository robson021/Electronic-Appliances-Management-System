package robert.web.svc.rest.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import robert.db.dao.UserDao;
import robert.db.entity.User;
import robert.exceptions.InvalidEmailPatternException;
import robert.exceptions.UserException;
import robert.exceptions.UserNotFoundException;
import robert.svc.api.CsrfTokenService;
import robert.svc.api.MailService;
import robert.utils.api.AppLogger;
import robert.web.session.user.api.UserInfoProvider;
import robert.web.svc.rest.ctrl.api.RegisterAndLoginCtrl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
public class RegisterAndLoginController implements RegisterAndLoginCtrl {

	private final AppLogger log;

	private final UserInfoProvider userInfoProvider;

	private final CsrfTokenService tokenService;

	private final UserDao userDao;

	private final MailService mailService;

	@Autowired
	public RegisterAndLoginController(AppLogger log, UserInfoProvider userInfoProvider, CsrfTokenService tokenService, UserDao userDao, MailService mailService) {
		this.log = log;
		this.userInfoProvider = userInfoProvider;
		this.tokenService = tokenService;
		this.userDao = userDao;
		this.mailService = mailService;
	}

	@Override
	@RequestMapping(value = REGISTER_URL, method = RequestMethod.PUT)
	public HttpStatus registerNewUser(@PathVariable(value = EMAIL) String email, //
									  @PathVariable(value = PASSWORD) String password, //
									  @PathVariable(value = NAME) String name, //
									  @PathVariable(value = SURNAME) String surname) {

		log.debug("New registration request from:", email);

		if (userDao.findUserByEmail(email) != null) {
			log.debug("User", email, "is already registered!");
			return HttpStatus.CONFLICT;
		}

		User user = new User();
		try {
			user.setEmail(email);
			user.setPassword(password);
			user.setName(name);
			user.setSurname(surname);
			userDao.saveUser(user);
		} catch (InvalidEmailPatternException e) {
			log.debug(e);
			return HttpStatus.FORBIDDEN;
		} catch (UserException e) {
			log.debug(e);
			return HttpStatus.FORBIDDEN;
		} catch (Exception e) {
			log.error(e);
			return HttpStatus.BAD_REQUEST;
		}

		userInfoProvider.setEmail(email);

		mailService.sendEmail(email, "Account Registration", "Your account has been registered.", null);

		log.info("Registration of", email, "has been been successful.");
		return HttpStatus.OK;
	}

	@Override
	@RequestMapping(value = LOGIN_URL, method = RequestMethod.POST)
	public HttpStatus loginUser(@PathVariable(value = EMAIL) String email, //
								@PathVariable(value = PASSWORD) String password, //
								HttpServletRequest request, //
								HttpServletResponse response) {

		log.info("Login request from:", email);
		User user = userDao.findUserByEmail(email);

		try {
			validateUser(user, password);
		} catch (UserNotFoundException e) {
			log.debug(e);
			return HttpStatus.NOT_FOUND;
		} catch (UserException e) {
			log.debug(e);
			return HttpStatus.UNAUTHORIZED;
		}

		userInfoProvider.setEmail(user.getEmail());
		if (user.isAdminPrivileged()) {
			userInfoProvider.enableAdminPrivileges();
		}

		CsrfToken csrfToken = tokenService.generateToken(request);
		tokenService.saveToken(csrfToken, request, response);
		userInfoProvider.setNewCsrfToken(csrfToken);

		log.debug("User", user.getEmail(), "has been logged in.");
		return HttpStatus.OK;
	}

	@Override
	@RequestMapping(value = LOGOUT_URL, method = RequestMethod.POST)
	public void logoutUser(HttpSession session) {
		String email = userInfoProvider.getEmail();
		userInfoProvider.invalidateData();
		session.invalidate();
		log.debug("Logged out user:", email);
	}

	private void validateUser(User user, String password) throws UserNotFoundException, UserException {
		if (user == null) {
			throw new UserNotFoundException();
		}
		if (!user.getPassword().equals(password)) {
			throw new UserException("Passwords for user " + user.getEmail() + " do not match.");
		}
		if (!user.getActivated()) {
			throw new UserException(user.getEmail(), "is not activated.");
		}
	}
}
