package robert.web.session.user.api;

import org.springframework.security.web.csrf.CsrfToken;

public interface UserInfoProvider {

	void setEmail(String email);

	String getEmail();

	void enableAdminPrivileges();

	boolean isAdmin();

	void setNewCsrfToken(CsrfToken csrfToken);

	CsrfToken getCsrfToken();
}
