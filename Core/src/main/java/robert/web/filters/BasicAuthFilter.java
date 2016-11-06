package robert.web.filters;

import org.springframework.util.AntPathMatcher;
import robert.exceptions.AuthException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class BasicAuthFilter implements Filter {

	protected static final AntPathMatcher apm = new AntPathMatcher();

	@Override
	public abstract void init(FilterConfig filterConfig) throws ServletException;

	@Override
	public abstract void destroy();

	@Override
	public final void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		try {
			doLogic(request, response);
			filterChain.doFilter(servletRequest, response);
		} catch (Throwable ignored) {
			this.invalidateSessionAndSendRedirect(response, request); // comment out for chrome non x-orgin requests
		}
	}

	private void invalidateSessionAndSendRedirect(HttpServletResponse response, HttpServletRequest request) {
		try {
			response.sendRedirect("/");
		} catch (IOException ignored) {
		} finally {
			request.getSession().invalidate();
		}
	}

	public abstract void doLogic(HttpServletRequest request, HttpServletResponse response) throws AuthException;
}
