package robert.web.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import robert.enums.Validation;
import robert.svc.api.CsrfTokenService;
import robert.utils.api.AppLogger;
import robert.web.session.user.api.UserInfoProvider;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public final class BasicAuthFilterImpl extends BasicAuthFilter {

	private final AppLogger log;

	private final UserInfoProvider userInfoProvider;

	private final CsrfTokenService tokenService;

	private final AntPathMatcher apm = new AntPathMatcher();

	@Autowired
	public BasicAuthFilterImpl(AppLogger log, UserInfoProvider userInfoProvider, CsrfTokenService tokenService) {
		this.log = log;
		this.userInfoProvider = userInfoProvider;
		this.tokenService = tokenService;
	}


	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		log.debug("New", BasicAuthFilterImpl.class, "initiated");
	}

	@Override
	public void destroy() {
		log.debug("Destroying filter:", BasicAuthFilterImpl.class);
	}

	@Override
	public final void doLogic(HttpServletRequest request, HttpServletResponse response) {
		if (isValidationNotEnabledOnThisURI(request.getRequestURI())) {
			return;
		}
		if (isUserAndTokenValid(userInfoProvider.getEmail(), request)) {
			CsrfToken csrfToken = tokenService.generateToken(request);
			tokenService.saveToken(csrfToken, request, response);
			userInfoProvider.setNewCsrfToken(csrfToken);
		} else {
			invalidateSessionAndSendRedirect(response, request);
		}
	}

	private boolean isValidationNotEnabledOnThisURI(String requestURI) {
		for (String pattern : Validation.NO_AUTH_URIS) {
			if (apm.match(pattern, requestURI))
				return true;
		}
		return false;
	}

	private boolean isUserAndTokenValid(String email, HttpServletRequest request) {
		return email != null && compareTokens(tokenService.loadToken(request));
	}

	private void invalidateSessionAndSendRedirect(HttpServletResponse response, HttpServletRequest request) {
		try {
			response.sendRedirect("/login");
		} catch (IOException ignored) {
		} finally {
			request.getSession().invalidate();
		}
	}

	private boolean compareTokens(CsrfToken givenToken) {
		CsrfToken currentToken = userInfoProvider.getCsrfToken();
		return tokenService.validateTokens(currentToken, givenToken);
	}
}
