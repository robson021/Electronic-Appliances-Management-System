package utils;

import robert.web.filters.BasicAuthFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NoAuthFilter extends BasicAuthFilter {

	@Override
	public void doLogic(HttpServletRequest request, HttpServletResponse response) {
		// od nothing, no user auth in tests
	}
}
