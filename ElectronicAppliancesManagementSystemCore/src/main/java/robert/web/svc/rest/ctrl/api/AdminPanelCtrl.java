package robert.web.svc.rest.ctrl.api;

import robert.web.svc.rest.responses.data.UserDR;

import java.util.List;

public interface AdminPanelCtrl {

	String ADMIN_PREFIX = "/admin/";

	String GET_ALL_INACTIVE_ACCOUNTS_URL = ADMIN_PREFIX + "all-inactive-accounts/";

	String GET_ALL_ACTIVE_ACCOUNTS_URL = ADMIN_PREFIX + "all-active-accounts/";

	List<UserDR> getAllInactiveAccounts();

	List<UserDR> getAllActiveAccounts();

}
