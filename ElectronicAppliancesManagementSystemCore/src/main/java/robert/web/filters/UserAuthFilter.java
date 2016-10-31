package robert.web.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Component;
import robert.enums.Validation;
import robert.exceptions.AuthException;
import robert.svc.api.CsrfTokenService;
import robert.utils.api.AppLogger;
import robert.web.session.user.api.UserInfoProvider;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public final class UserAuthFilter extends BasicAuthFilter {

	private final AppLogger log;

	private final UserInfoProvider userInfoProvider;

	private final CsrfTokenService tokenService;

	@Autowired
	public UserAuthFilter(AppLogger log, UserInfoProvider userInfoProvider, CsrfTokenService tokenService) {
		this.log = log;
		this.userInfoProvider = userInfoProvider;
		this.tokenService = tokenService;
	}


	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		log.debug("New", UserAuthFilter.class, "initiated");
	}

	@Override
	public void destroy() {
		log.debug("Destroying filter:", UserAuthFilter.class);
	}

	@Override
	public final void doLogic(HttpServletRequest request, HttpServletResponse response) throws AuthException {
		if (isValidationNotEnabledOnThisURI(request.getRequestURI())) {
			return;
		}

		isUserAndTokenValid(userInfoProvider.getEmail(), request);
		CsrfToken csrfToken = tokenService.generateToken(request);
		tokenService.saveToken(csrfToken, request, response);
		userInfoProvider.setNewCsrfToken(csrfToken);

	}

	private boolean isValidationNotEnabledOnThisURI(String requestURI) {
		for (String pattern : Validation.NO_AUTH_URIS) {
			if (apm.match(pattern, requestURI))
				return true;
		}
		return false;
	}

	private void isUserAndTokenValid(String email, HttpServletRequest request) throws AuthException {
		if (email == null && !compareTokens(tokenService.loadToken(request))) {
			AuthException exception = new AuthException("User " + email + " is not valid.");
			log.debug(exception);
			throw exception;
		}
	}

	private boolean compareTokens(CsrfToken givenToken) {
		CsrfToken currentToken = userInfoProvider.getCsrfToken();
		return tokenService.validateTokens(currentToken, givenToken);
	}
}
