package robert.svc.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.csrf.CsrfToken;

public interface CsrfTokenService {

    CsrfToken loadToken(HttpServletRequest request);

    CsrfToken generateToken(HttpServletRequest request);

    boolean validateTokens(CsrfToken tokenA, CsrfToken tokenB);

    void saveToken(CsrfToken csrfToken, HttpServletRequest request, HttpServletResponse response);
}
