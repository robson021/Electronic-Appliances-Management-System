package robert.web.session.user;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import robert.web.session.user.api.UserInfoProvider;

@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserInfoProviderImpl implements UserInfoProvider {

	private String email = null;

	private long id = -1;

	private boolean adminPrivileges = false;

	private CsrfToken csrfToken = null;

	@Override
	public void setIds(long id, String email) {
		if ( this.email == null && this.id < 0 ) { // set only once
			this.email = email.trim();
			this.id = id;
		}
	}

	@Override
	public String getEmail() {
		return this.email;
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public void enableAdminPrivileges() {
		this.adminPrivileges = true;
	}

	@Override
	public boolean isAdmin() {
		return this.adminPrivileges;
	}

	@Override
	public void setNewCsrfToken(CsrfToken csrfToken) {
		this.csrfToken = csrfToken;
	}

	@Override
	public CsrfToken getCsrfToken() {
		return this.csrfToken;
	}

	@Override
	public void invalidateData() {
		this.email = null;
		this.adminPrivileges = false;
		this.csrfToken = null;
	}
}
