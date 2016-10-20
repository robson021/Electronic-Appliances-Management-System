package robert.web.svc.rest.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import robert.db.dao.AdminDao;
import robert.exceptions.UserNotFoundException;
import robert.utils.api.AppLogger;
import robert.web.session.user.api.UserInfoProvider;
import robert.web.svc.rest.ctrl.api.AdminPanelCtrl;
import robert.web.svc.rest.responses.asm.UserAssembler;
import robert.web.svc.rest.responses.data.UserDR;

import java.util.List;

@RestController
public class AdminPanelController implements AdminPanelCtrl {

	private final AppLogger log;

	private final UserInfoProvider userInfoProvider;

	private final AdminDao adminDao;

	@Autowired
	public AdminPanelController(AppLogger log, UserInfoProvider userInfoProvider, AdminDao adminDao) {
		this.log = log;
		this.userInfoProvider = userInfoProvider;
		this.adminDao = adminDao;
	}

	@Override
	@RequestMapping(GET_ALL_INACTIVE_ACCOUNTS_URL)
	public List<UserDR> getAllInactiveAccounts() {
		log.debug(GET_ALL_INACTIVE_ACCOUNTS_URL, "request from:", userInfoProvider.getEmail());
		return UserAssembler.convertToUserDR(adminDao.getAllInactiveUsers());
	}

	@Override
	@RequestMapping(GET_ALL_ACTIVE_ACCOUNTS_URL)
	public List<UserDR> getAllActiveAccounts() {
		log.debug(GET_ALL_ACTIVE_ACCOUNTS_URL, "request from:", userInfoProvider.getEmail());
		return UserAssembler.convertToUserDR(adminDao.getAllActiveUsers());
	}

	@Override
	@RequestMapping(value = ACTIVATE_ACCOUNT, method = RequestMethod.POST)
	public HttpStatus activateUserAccount(@PathVariable(EMAIL) String email) {
		log.debug(ACTIVATE_ACCOUNT, "request from:", userInfoProvider.getEmail());
		try {
			adminDao.activateUserAccount(email);
		} catch (UserNotFoundException e) {
			log.debug(e);
			return HttpStatus.NOT_ACCEPTABLE;
		}
		log.debug("User has been activated:", email);
		return HttpStatus.OK;
	}
}
