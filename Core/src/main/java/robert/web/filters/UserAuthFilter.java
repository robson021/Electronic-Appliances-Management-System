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
	public void doLogic(HttpServletRequest request, HttpServletResponse response) throws AuthException {
		if (isValidationNotEnabledOnThisURI(request.getRequestURI())) {
			return;
		}

		isUserAndTokenValid(userInfoProvider.getEmail(), request);
		CsrfToken csrfToken = tokenService.generateToken(request);
		tokenService.saveToken(csrfToken, request, response);
		userInfoProvider.setNewCsrfToken(csrfToken);

		log.debug("User", userInfoProvider.getEmail(), "is valid.");

	}

	private boolean isValidationNotEnabledOnThisURI(String requestURI) {
		for (String pattern : Validation.NO_AUTH_URIS) {
			if (apm.match(pattern, requestURI))
				return true;
		}
		return isFileRequest(requestURI);
	}

	private void isUserAndTokenValid(String email, HttpServletRequest request) throws AuthException {

		if (email == null) {
			AuthException exception = new AuthException("User is not logged in.");
			log.debug(exception);
			throw exception;
		}

		if (!compareTokens(tokenService.loadToken(request))) {
			AuthException exception = new AuthException("User " + email + " has invalid csrf token.");
			log.debug(exception);
			throw exception;
		}
	}

	private boolean compareTokens(CsrfToken givenToken) {
		CsrfToken currentToken = userInfoProvider.getCsrfToken();
		return tokenService.validateTokens(currentToken, givenToken);
	}

	private boolean isFileRequest(String uri) {
		for (String pattern : Validation.NO_AUTH_FILES) {
			if (uri.endsWith(pattern)) {
				return true;
			}
		}
		return false;
	}
}
