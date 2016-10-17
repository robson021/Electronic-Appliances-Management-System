package utils;

import robert.web.filters.BasicAuthFilter;

import javax.servlet.http.HttpServletResponse;

public class NoAuthFilter extends BasicAuthFilter {

	@Override
	public void doLogic(HttpServletResponse response) {
		// od nothing
	}
}
