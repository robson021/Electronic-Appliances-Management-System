package robert.web.svc.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import robert.db.dao.UserDao;
import robert.db.entity.User;
import robert.exceptions.InvalidEmailPatternException;
import robert.exceptions.TooShortPasswordException;
import robert.utils.api.AppLogger;
import robert.web.svc.rest.api.RegisterAndLoginCtrl;
import robert.web.user.UserInfoProvider;

@RestController
public class RegisterAndLoginController implements RegisterAndLoginCtrl {

	private final AppLogger log;

	private final UserInfoProvider userInfoProvider;

	private final UserDao userDao;

	@Autowired
	public RegisterAndLoginController(AppLogger log, UserInfoProvider userInfoProvider, UserDao userDao) {
		this.log = log;
		this.userInfoProvider = userInfoProvider;
		this.userDao = userDao;
	}

	@Override
	@RequestMapping(value = REGISTER_URL)
	public HttpStatus registerNewUser(@PathVariable(value = EMAIL) String email, //
									  @PathVariable(value = PASSWORD) String password, //
									  @PathVariable(value = NAME) String name, //
									  @PathVariable(value = SURNAME) String surname) {

		log.debug("New registration request from: " + email);

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
			log.debug("Invalid email pattern. Given email:", email);
			return HttpStatus.FORBIDDEN;
		} catch (TooShortPasswordException e) {
			log.debug("Invalid password");
			return HttpStatus.FORBIDDEN;
		} catch (Exception e) {
			log.debug("Error occurred while saving the entity", user);
		}

		userInfoProvider.setEmail(email);
		log.debug("Registration of", email, "has been been successful");
		return HttpStatus.OK;
	}
}
