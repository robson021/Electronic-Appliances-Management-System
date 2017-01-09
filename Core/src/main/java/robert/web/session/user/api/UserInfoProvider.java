package robert.web.session.user.api;

import org.springframework.security.web.csrf.CsrfToken;

public interface UserInfoProvider {

	void setIds(long id, String email);

	String getEmail();

	long getId();

	void enableAdminPrivileges();

	boolean isAdmin();

	void setNewCsrfToken(CsrfToken csrfToken);

	CsrfToken getCsrfToken();

	void invalidateData();
}
