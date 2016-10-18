package robert.web.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class BasicAuthFilter implements Filter {

	@Override
	public final void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public final void destroy() {
	}

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

	public abstract void doLogic(HttpServletRequest request, HttpServletResponse response);
}
