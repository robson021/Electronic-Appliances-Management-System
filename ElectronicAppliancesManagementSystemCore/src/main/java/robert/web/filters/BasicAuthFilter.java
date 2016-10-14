package robert.web.filters;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletResponse;

public interface BasicAuthFilter extends Filter {
	void doLogic(HttpServletResponse response);
}
