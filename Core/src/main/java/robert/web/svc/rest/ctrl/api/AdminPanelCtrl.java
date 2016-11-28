package robert.web.svc.rest.ctrl.api;

import java.util.List;

import org.springframework.http.HttpStatus;

import robert.web.svc.rest.responses.json.UserDR;

public interface AdminPanelCtrl extends BasicParams {

	String ADMIN_PREFIX = "/admin/";

	String CHECK_IF_ADMIN = ADMIN_PREFIX + "am-i-admin/";

	String GET_ALL_INACTIVE_ACCOUNTS_URL = ADMIN_PREFIX + "all-inactive-accounts/";

	String GET_ALL_ACTIVE_ACCOUNTS_URL = ADMIN_PREFIX + "all-active-accounts/";

	String ACTIVATE_ACCOUNT = ADMIN_PREFIX + "activate-account/{" + EMAIL + "}/";

	String DEACTIVATE_ACCOUNT = ADMIN_PREFIX + "deactivate-account/{" + EMAIL + "}/";

	String DELETE_USER = ADMIN_PREFIX + "delete-user/{" + EMAIL + "}/";

	HttpStatus checkIfAdmin();

	List<UserDR> getAllInactiveAccounts();

	List<UserDR> getAllActiveAccounts();

	HttpStatus activateUserAccount(String email);

	HttpStatus deactivateUserAccount(String email);

	HttpStatus deleteUser(String email);
}
