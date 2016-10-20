package robert.web.filters;

import org.springframework.util.AntPathMatcher;

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
		doLogic(request, response);
		try {
			filterChain.doFilter(servletRequest, response);
		} catch (Throwable ignored) {
		}
	}

	protected void invalidateSessionAndSendRedirect(HttpServletResponse response, HttpServletRequest request) {
		try {
			response.sendRedirect("/login");
		} catch (IOException ignored) {
		} finally {
			request.getSession().invalidate();
		}
	}

	public abstract void doLogic(HttpServletRequest request, HttpServletResponse response);
}
