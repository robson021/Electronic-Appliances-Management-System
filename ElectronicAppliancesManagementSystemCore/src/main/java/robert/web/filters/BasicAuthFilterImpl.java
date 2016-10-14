package robert.web.filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BasicAuthFilterImpl implements BasicAuthFilter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		doLogic(response);
		filterChain.doFilter(servletRequest, response);
	}

	@Override
	public void destroy() {

	}

	@Override
	public void doLogic(HttpServletResponse response) {
		//todo set login and URLs
	}
}
