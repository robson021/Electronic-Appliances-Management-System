package robert.web.svc.rest.ctrl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import robert.db.dao.AdminDao;
import robert.exceptions.UserNotFoundException;
import robert.svc.api.MailService;
import robert.utils.api.AppLogger;
import robert.web.session.user.api.UserInfoProvider;
import robert.web.svc.rest.ctrl.api.AdminPanelCtrl;
import robert.web.svc.rest.responses.asm.UserAssembler;
import robert.web.svc.rest.responses.json.UserDTO;

@RestController
public class AdminPanelController implements AdminPanelCtrl {

	private final AppLogger log;

	private final UserInfoProvider userInfoProvider;

	private final AdminDao adminDao;

	private final MailService mailService;

	@Autowired
	public AdminPanelController(AppLogger log, UserInfoProvider userInfoProvider, AdminDao adminDao, MailService mailService) {
		this.log = log;
		this.userInfoProvider = userInfoProvider;
		this.adminDao = adminDao;
		this.mailService = mailService;
	}

	@Override
	@RequestMapping(CHECK_IF_ADMIN)
	public HttpStatus checkIfAdmin() {
		if (userInfoProvider.isAdmin()) {
			return HttpStatus.OK;
		}
		return HttpStatus.UNAUTHORIZED;
	}

	@Override
	@RequestMapping(GET_ALL_INACTIVE_ACCOUNTS_URL)
	public List<UserDTO> getAllInactiveAccounts() {
		log.debug(GET_ALL_INACTIVE_ACCOUNTS_URL, "request from:", userInfoProvider.getEmail());
		return UserAssembler.convertToUserDTO(adminDao.getAllInactiveUsers());
	}

	@Override
	@RequestMapping(GET_ALL_ACTIVE_ACCOUNTS_URL)
	public List<UserDTO> getAllActiveAccounts() {
		log.debug(GET_ALL_ACTIVE_ACCOUNTS_URL, "request from:", userInfoProvider.getEmail());
		return UserAssembler.convertToUserDTO(adminDao.getAllActiveUsers());
	}

	@Override
	@RequestMapping(value = ACTIVATE_ACCOUNT, method = RequestMethod.POST)
	public HttpStatus activateUserAccount(@PathVariable(EMAIL) String email) {
		log.debug("Activate user", email, "request from:", userInfoProvider.getEmail());
		try {
			adminDao.activateUserAccount(email);
			mailService.sendEmail(email,
					"Account activation",
					"Your account has been activated.",
					null);
		} catch (UserNotFoundException e) {
			log.debug(e);
			return HttpStatus.NOT_ACCEPTABLE;
		}
		log.debug("User has been activated:", email);
		return HttpStatus.OK;
	}

	@Override
	@RequestMapping(value = DEACTIVATE_ACCOUNT, method = RequestMethod.POST)
	public HttpStatus deactivateUserAccount(@PathVariable(EMAIL) String email) {
		log.debug("Deactivate user", email, "- request from:", userInfoProvider.getEmail());
		try {
			adminDao.deactivateUserAccount(email);
			mailService.sendEmail(email,
					"Account deactivation",
					"Your account has been deactivated.",
					null);
		} catch (UserNotFoundException e) {
			log.debug(e);
			return HttpStatus.NOT_ACCEPTABLE;
		}
		log.debug("User has been deactivated:", email);
		return HttpStatus.OK;
	}

	@Override
	@RequestMapping(value = DELETE_USER, method = RequestMethod.DELETE)
	public HttpStatus deleteUser(@PathVariable(EMAIL) String email) {
		log.debug("Delete user:", email, "- request from:", userInfoProvider.getEmail());
		adminDao.deleteUser(email);
		return HttpStatus.OK;
	}
}
