package robert.web.filters;

import robert.web.user.UserInfoProvider;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class BasicAuthFilter implements Filter {

	private final UserInfoProvider userInfoProvider;

	public BasicAuthFilter(UserInfoProvider uip) {
		this.userInfoProvider = uip;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		doLogic(response);
		filterChain.doFilter(servletRequest, response);
	}

	abstract void doLogic(HttpServletResponse response);
}
