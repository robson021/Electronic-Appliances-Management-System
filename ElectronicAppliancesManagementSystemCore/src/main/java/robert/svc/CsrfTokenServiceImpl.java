package robert.svc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Service;

import robert.svc.api.CsrfTokenService;

@Service
public class CsrfTokenServiceImpl implements CsrfTokenService {

    private final CookieCsrfTokenRepository tokenRepository = new CookieCsrfTokenRepository();

    @Override
    public CsrfToken loadToken(HttpServletRequest request) {
        return tokenRepository.loadToken(request);
    }

    @Override
    public CsrfToken generateToken(HttpServletRequest request) {
        return tokenRepository.generateToken(request);
    }

    @Override
    public boolean validateTokens(CsrfToken tokenA, CsrfToken tokenB) {
        return !(tokenA == null || tokenB == null) && tokenA.equals(tokenB);
    }

    @Override
    public void saveToken(CsrfToken csrfToken, HttpServletRequest request, HttpServletResponse response) {
        tokenRepository.saveToken(csrfToken, request, response);
    }
}
