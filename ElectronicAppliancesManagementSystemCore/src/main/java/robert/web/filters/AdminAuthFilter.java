package robert.web.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import robert.exceptions.AuthException;
import robert.utils.api.AppLogger;
import robert.web.session.user.api.UserInfoProvider;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static robert.enums.Validation.ADMIN_URI;

@Component
public final class AdminAuthFilter extends BasicAuthFilter {

	private final AppLogger log;

	private final UserInfoProvider userInfoProvider;

	@Autowired
	public AdminAuthFilter(AppLogger log, UserInfoProvider userInfoProvider) {
		this.log = log;
		this.userInfoProvider = userInfoProvider;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		log.debug("Initiated filter:", AdminAuthFilter.class);
	}

	@Override
	public void destroy() {
		log.debug("Destroyed:", AdminAuthFilter.class);
	}

	@Override
	public void doLogic(HttpServletRequest request, HttpServletResponse response) throws AuthException {
		if (!isAdminUri(request.getRequestURI())) {
			return;
		}
		checkAdminPrivileges();
	}

	private boolean isAdminUri(String requestURI) {
		return apm.match(ADMIN_URI, requestURI);
	}

	private void checkAdminPrivileges() throws AuthException {
		if (!this.userInfoProvider.isAdmin()) {
			AuthException exception = new AuthException("User " +
					this.userInfoProvider.getEmail() +
					" is not an admin."
			);
			log.debug(exception);
			throw exception;
		}
	}
}
