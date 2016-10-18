package robert.web.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Component;
import robert.enums.Validation;
import robert.web.session.user.CsrfTokenService;
import robert.web.session.user.api.UserInfoProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class BasicAuthFilterImpl extends BasicAuthFilter {

	private final UserInfoProvider userInfoProvider;

	private final CsrfTokenService tokenService;

	@Autowired
	public BasicAuthFilterImpl(UserInfoProvider userInfoProvider, CsrfTokenService tokenService) {
		this.userInfoProvider = userInfoProvider;
		this.tokenService = tokenService;
	}


	@Override
	public void doLogic(HttpServletRequest request, HttpServletResponse response) {  // TODO
		if (!isValidationEnabledOnThisURI(request.getRequestURI())) {
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

	private boolean isValidationEnabledOnThisURI(String requestURI) {
		return !Validation.NO_AUTH_URIS.contains(requestURI);
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
		return currentToken.equals(givenToken);
	}
}
