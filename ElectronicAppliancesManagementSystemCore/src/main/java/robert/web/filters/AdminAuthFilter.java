package robert.web.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import robert.exceptions.NotAnAdminException;
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
	public final void doLogic(HttpServletRequest request, HttpServletResponse response) {
		if (!isAdminUri(request.getRequestURI())) {
			return;
		}

		try {
			checkAdminPrivileges();
		} catch (NotAnAdminException e) {
			log.debug(e);
			invalidateSessionAndSendRedirect(response, request);
		}

	}

	private boolean isAdminUri(String requestURI) {
		return apm.match(ADMIN_URI, requestURI);
	}

	private void checkAdminPrivileges() throws NotAnAdminException {
		if (
				this.userInfoProvider.getEmail() == null
						|| (!this.userInfoProvider.isAdmin())) {
			throw new NotAnAdminException("User " + this.userInfoProvider.getEmail() + " is not an admin.");
		}
	}
}
