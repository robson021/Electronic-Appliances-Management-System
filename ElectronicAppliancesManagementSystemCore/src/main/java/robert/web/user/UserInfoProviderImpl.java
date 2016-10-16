package robert.web.user;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserInfoProviderImpl implements UserInfoProvider {

	private String email = null;

	private boolean adminPrivileges = false;

	@Override
	public void setEmail(String email) {
		if (this.email == null) {
			this.email = email.trim();
		}
	}

	@Override
	public String getEmail() {
		return this.email;
	}

	@Override
	public void enableAdminPrivileges() {
		this.adminPrivileges = true;
	}

	@Override
	public boolean isAdmin() {
		return this.adminPrivileges;
	}
}
