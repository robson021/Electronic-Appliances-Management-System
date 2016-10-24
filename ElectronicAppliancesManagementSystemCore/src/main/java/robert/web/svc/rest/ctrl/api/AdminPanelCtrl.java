package robert.web.svc.rest.ctrl.api;

import org.springframework.http.HttpStatus;
import robert.web.svc.rest.responses.data.UserDR;

import java.util.List;

public interface AdminPanelCtrl extends BasicUserParams {

	String ADMIN_PREFIX = "/admin/";

	String GET_ALL_INACTIVE_ACCOUNTS_URL = ADMIN_PREFIX + "all-inactive-accounts/";

	String GET_ALL_ACTIVE_ACCOUNTS_URL = ADMIN_PREFIX + "all-active-accounts/";

	String ACTIVATE_ACCOUNT = ADMIN_PREFIX + "activate-account/{" + EMAIL + "}/";

	String DEACTIVATE_ACCOUNT = ADMIN_PREFIX + "deactivate-account/{" + EMAIL + "}/";

	List<UserDR> getAllInactiveAccounts();

	List<UserDR> getAllActiveAccounts();

	HttpStatus activateUserAccount(String email);

	HttpStatus deactivateUserAccount(String email);

}
