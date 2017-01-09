package robert.web.svc.rest.ctrl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.RequestBody;
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
import robert.web.svc.rest.responses.dto.BasicDTO;
import robert.web.svc.rest.responses.dto.UserCredentialsDTO;

@RestController
public class RegisterAndLoginController implements RegisterAndLoginCtrl {

    private final AppLogger log;

    private final UserInfoProvider userInfoProvider;

    private final CsrfTokenService tokenService;

    private final UserDao userDao;

    private final MailService mailService;

    @Autowired
    public RegisterAndLoginController(AppLogger log, UserInfoProvider userInfoProvider, CsrfTokenService tokenService, UserDao userDao,
            MailService mailService) {
        this.log = log;
        this.userInfoProvider = userInfoProvider;
        this.tokenService = tokenService;
        this.userDao = userDao;
        this.mailService = mailService;
    }

    @Override
    @RequestMapping(value = REGISTER_URL, method = RequestMethod.PUT)
    public BasicDTO registerNewUser(@RequestBody UserCredentialsDTO userData) {
        String email = userData.getEmail();
        log.debug("New registration request from:", userData.getEmail());

        if ( userDao.findUserByEmail(userData.getEmail()) != null ) {
            log.debug("User", userData.getEmail(), "is already registered!");
            return new BasicDTO("E-mail is already taken.");
        }
        try {
            User user = new User();
            user.setEmail(userData.getEmail());
            user.setPassword(userData.getPassword());
            user.setName(userData.getName());
            user.setSurname(userData.getSurname());
            userDao.saveUser(user);
        } catch (InvalidEmailPatternException | UserException e) {
            log.debug(e);
            return new BasicDTO("Invalid password.");
        }
        mailService.sendEmail(email, "Account Registration", "Your account has been registered.", null);

        log.info("Registration of", email, "has been been successful.");
        return new BasicDTO("OK");
    }

    @Override
    @RequestMapping(value = LOGIN_URL, method = RequestMethod.POST)
    public BasicDTO loginUser(@RequestBody UserCredentialsDTO userData, HttpServletRequest request, //
            HttpServletResponse response) {

        log.info("Login request from:", userData.getEmail());
        User user = userDao.findUserByEmail(userData.getEmail());

        try {
            validateUser(user, userData.getPassword());
        } catch (UserNotFoundException e) {
            log.debug(e);
            return new BasicDTO("User not found.");
        } catch (UserException e) {
            log.debug(e);
            return new BasicDTO("Invalid password.");
        }

        userInfoProvider.setIds(user.getId(), user.getEmail());
        if ( user.isAdminPrivileged() ) {
            userInfoProvider.enableAdminPrivileges();
        }

        CsrfToken csrfToken = tokenService.generateToken(request);
        tokenService.saveToken(csrfToken, request, response);
        userInfoProvider.setNewCsrfToken(csrfToken);

        log.debug("User", user.getEmail(), "has been logged in.");
        return new BasicDTO("OK");
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
        if ( user == null ) {
            throw new UserNotFoundException();
        }
        if ( !user.getPassword()
                .equals(password) ) {
            throw new UserException("Passwords for user " + user.getEmail() + " do not match.");
        }
        if ( !user.getActivated() ) {
            throw new UserException(user.getEmail(), "is not activated.");
        }
    }
}
