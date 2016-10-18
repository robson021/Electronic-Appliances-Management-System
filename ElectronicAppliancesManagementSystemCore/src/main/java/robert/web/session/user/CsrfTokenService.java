package robert.web.session.user;

import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class CsrfTokenService {

	private final CookieCsrfTokenRepository tokenRepository = new CookieCsrfTokenRepository();

	public CsrfToken loadToken(HttpServletRequest request) {
		return tokenRepository.loadToken(request);
	}

	public CsrfToken generateToken(HttpServletRequest request) {
		return tokenRepository.generateToken(request);
	}

	public boolean validateTokens(CsrfToken tokenA, CsrfToken tokenB) {
		return !(tokenA == null || tokenB == null) && tokenA.equals(tokenB);
	}

	public void saveToken(CsrfToken csrfToken, HttpServletRequest request, HttpServletResponse response) {
		tokenRepository.saveToken(csrfToken, request, response);
	}
}
